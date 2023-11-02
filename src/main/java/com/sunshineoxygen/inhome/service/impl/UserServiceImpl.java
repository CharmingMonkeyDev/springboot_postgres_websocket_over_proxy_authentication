package com.sunshineoxygen.inhome.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
//import com.sun.media.sound.InvalidDataException;
import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.exception.ApplicationException;
import com.sunshineoxygen.inhome.exception.EmailAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.ExceptionType;
import com.sunshineoxygen.inhome.exception.PhoneAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.TokenNotFoundException;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.OTPCode;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserType;
import com.sunshineoxygen.inhome.repository.IOTPCodeRepository;
import com.sunshineoxygen.inhome.repository.IUserRepository;
import com.sunshineoxygen.inhome.service.IOtpProvider;
import com.sunshineoxygen.inhome.service.IUserProfileService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.service.IUserTypeService;
import com.sunshineoxygen.inhome.service.email.IEmailService;
import com.sunshineoxygen.inhome.service.sms.ISmsService;
import com.sunshineoxygen.inhome.service.sms.SmsServiceFactory;
import com.sunshineoxygen.inhome.ui.dto.*;
import com.sunshineoxygen.inhome.utils.Constants;
import com.sunshineoxygen.inhome.utils.DateUtil;
import com.sunshineoxygen.inhome.utils.Formatter;
import com.sunshineoxygen.inhome.utils.JSONUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, User, UUID> implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${aws.accessKey}")
    private  String accessKey;

    @Value("${aws.secretKey}")
    private  String secretKey;

    @Autowired
    private Environment env;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserTypeService userTypeService;

    @Autowired
    private OTPProviderImpl otpProviderImpl;

    @Autowired
    private IOtpProvider iOtpProvider;

    @Autowired
    IEmailService emailService;

    @Autowired
    IUserProfileService userProfileService;


    @Override
    public Optional<User> findOneByEmailIgnoreCaseAndUserType(String email, UserType userType) {
        return userRepository.findOneByEmailIgnoreCaseAndUserType(email,userType);
    }

    @Override
    public Optional<User> findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(String prefixPhoneNumber, String phoneNumber, UserType userType) {
        return userRepository.findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(prefixPhoneNumber,phoneNumber,userType);
    }

    @Transactional(timeout = 60)
    @Override
    public DynamicBean getOtpUser(UserCredentialDTO user) throws Exception {
        DynamicBean result = getOtpAplUser(user, Constants.USER);
        return result;
    }

    @Transactional(timeout = 60)
    @Override
    public DynamicBean getOtpDoctor(UserCredentialDTO user) throws Exception {
        DynamicBean result = getOtpAplUser(user, Constants.DOCTOR);
        return result;
    }

    private DynamicBean getOtpAplUser(UserCredentialDTO user, String userTypeStr) throws Exception {
        DynamicBean result = null;
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(userTypeStr, Status.ACTIVE).orElse(null);
        User currentUser = this.userRepository.findOneByEmailIgnoreCaseAndUserType(user.getUsername(), userType).orElse(null);
        if(currentUser != null){
            String otpCode = otpProviderImpl.generateOTP(currentUser.getEmail(), currentUser.getPrefixPhoneNumber().concat(currentUser.getPhoneNumber()),true,6,currentUser.getId());
            Date expireDate = DateUtil.secondsAdd(new Date(),300);
            DynamicBean token = getUserTokenForOtp(user,userTypeStr);
            if(sendOtpToIdentityServer(token,otpCode,expireDate)){
                    result = new DynamicBean();
                    if((BooleanUtils.toBooleanDefaultIfNull(user.getRequestSms(),false) || BooleanUtils.toBooleanDefaultIfNull(currentUser.getIsAllowedSms(),false)) && currentUser.getPrefixPhoneNumber() != null && currentUser.getPhoneNumber() != null){
                        sendOtpSms(currentUser,otpCode);
                        saveOtpCode( null,currentUser.getPrefixPhoneNumber()+currentUser.getPhoneNumber(), otpCode,expireDate,currentUser.getId());
                        result.addProperty("resultMessage","Otp sent via SMS!");
                    }else{
                        sendOtpEmail(currentUser,otpCode);
                        saveOtpCode(user.getUsername(),null, otpCode,expireDate,currentUser.getId());
                        result.addProperty("resultMessage","Otp sent via Email!");
                    }
                    logout(token);

            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return result;
    }


    @Transactional(timeout = 60)
    @Override
    public UserCredentialInfoDTO verifyOtpUser (UserCredentialDTO user) throws Exception {
        UserCredentialInfoDTO result = verifyOtpAplUser(user,Constants.USER);
        return result;
    }

    @Transactional(timeout = 60)
    @Override
    public UserCredentialInfoDTO verifyOtpDoctor (UserCredentialDTO user) throws Exception {
        UserCredentialInfoDTO result = verifyOtpAplUser(user,Constants.DOCTOR);
        return result;
    }

    @Override
    public User getUserFromJwt(DynamicBean jwtBean) throws IllegalArgumentException {
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(jwtBean.get("usertype"),Status.ACTIVE).orElse(null);
        if(userType == null)
            throw new IllegalArgumentException("invalid token!");
        User user = this.findOneByEmailIgnoreCaseAndUserType(jwtBean.get("email"),userType).orElse(null);
        if(user == null)
            throw new IllegalArgumentException("invalid user");
        return user;
    }

    private UserCredentialInfoDTO verifyOtpAplUser(UserCredentialDTO user,String userTypeStr) throws UnirestException {
        UserCredentialInfoDTO result =null;
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(userTypeStr, Status.ACTIVE).orElse(null);
        DynamicBean bean = getUserTokenForOtp(user,userTypeStr);
        if (StringUtils.isNotEmpty(bean.get("access_token")) && !bean.getPropertyAsBoolean("is_2fa_valid")) {
            User currentUser = this.userRepository.findOneByEmailIgnoreCaseAndUserType(user.getUsername(),userType).orElse(null);
            if (this.iOtpProvider.verifyOtp(user.getOtp(), currentUser.getPrefixPhoneNumber().concat(currentUser.getPhoneNumber()), user.getUsername(),currentUser.getId()) &&
                    verifyOtpToIdentityServer(bean, user.getOtp())) {
                this.iOtpProvider.changeStatusOtpRecord(user.getOtp(), currentUser.getPrefixPhoneNumber().concat(currentUser.getPhoneNumber()), user.getUsername(), Status.PASSIVE,currentUser.getId());
                result = UserCredentialInfoDTO.mapDynamicBeanToUserCredentialInfoDto(bean);
                result.setUserType(currentUser.getUserType().getShortCode());
            }
        } else {
            throw new TokenNotFoundException(402,bean.get("error_description"));
        }
        return result;
    }

    private void sendOtpEmail(User user, String otpCode) {
        DynamicBean bean = new DynamicBean();
        bean.addProperty("user",user);
        bean.addProperty("otpCode",otpCode);
        emailService.sendEmailFromTemplate(bean,"mail/otpvalidation","email.otpvalidation.title");
    }

    private void sendOtpSms(User user, String otpCode){
        DynamicBean bean = new DynamicBean();
        bean.addProperty("accessKey",accessKey);
        bean.addProperty("secretKey",secretKey);
        bean.addProperty("simulation",false);
        ISmsService service = SmsServiceFactory.get(bean);
        service.send("+"+user.getPrefixPhoneNumber()+user.getPhoneNumber(),"This is your One Time Password:"+otpCode);
    }

    private void sendResetPasswordEmail(User user, String url) {
        DynamicBean bean = new DynamicBean();
        bean.addProperty("user",user);
        bean.addProperty("url",url);
        emailService.sendEmailFromTemplate(bean,"mail/resetpassword","email.resetpassword.title");
    }

    private void saveOtpCode(String email,String phoneNumber, String otpCode,Date expireDate,UUID userId) {
        OTPCode code = new OTPCode();
        code.setCode(otpCode);
        code.setEmail(email);
        code.setPhoneNumber(phoneNumber);
        code.setExpiredDate(expireDate);
        code.setUserId(userId);
        this.iOtpProvider.save(code);
    }

    private boolean verifyOtpToIdentityServer(DynamicBean userToken,String otpCode) throws UnirestException {
        DynamicBean request = new DynamicBean();
        request.addProperty("twofacode",otpCode);
        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.verifyOtp"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ userToken.get("access_token"))
                .body(JSONUtil.convertDynamicBeanToJsonString(request)).asJson();
        return  response.getStatus()==200;
    }

    private boolean sendOtpToIdentityServer(DynamicBean userToken,String otpCode,Date expireDate) throws Exception {
        DynamicBean request = new DynamicBean();
        request.addProperty("twofacode",otpCode);
        request.addProperty("twofaExpireTime", Formatter.format(expireDate,"dateTime"));
        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.getOtp"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ userToken.get("access_token"))
                .body(JSONUtil.convertDynamicBeanToJsonString(request)).asJson();
        return  response.getStatus()==200;
    }

    @Override
    public void logout(DynamicBean token) throws UnirestException {
        HttpResponse<String> response = Unirest.post(env.getProperty("app.logout"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ token.get("access_token"))
                .asString();
    }

    @Override
    public TokenInfoDTO getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        return null;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (Status.ACTIVE.equals(existingUser.getStatus())) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        //this.clearUserCaches(existingUser);
        return true;
    }

    private CustomerDto prepareCustomerDto(User user){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(user.getEmail().toLowerCase());
        customerDto.setUsername(user.getEmail().toLowerCase());
        customerDto.setPhoneNumber(user.getPrefixPhoneNumber().concat(user.getPhoneNumber()));
        customerDto.setUsertype(user.getUserType().getName());
        return customerDto;
    }

    private CustomerDto prepareCustomerDto(UserRegisterDTO userRegisterDTO){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(userRegisterDTO.getEmail().toLowerCase());
        customerDto.setUsername(userRegisterDTO.getEmail().toLowerCase());
        customerDto.setPassword(userRegisterDTO.getPassword());
        customerDto.setPhoneNumber(userRegisterDTO.getPrefixPhoneNumber().concat(userRegisterDTO.getPhoneNumber()));
        customerDto.setUsertype(userRegisterDTO.getTypeUser());
        return customerDto;
    }

    private DynamicBean getUserTokenForOtp(UserCredentialDTO userDto,String userType) throws UnirestException{
        DynamicBean bean = null;
        DynamicBean request = new DynamicBean();
        request.addProperty("clientId",env.getProperty("security.oauth2.client.client-id"));
        request.addProperty("clientSecret",env.getProperty("security.oauth2.client.client-secret"));
        request.addProperty("username",userDto.getUsername());
        request.addProperty("password",userDto.getPassword());
        request.addProperty("email",userDto.getUsername());
        request.addProperty("userType",userType);

        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.loginurlwithotp"))
                .header("Content-Type", "application/json")
                .header("usertype",userType)
                .body(JSONUtil.convertDynamicBeanToJsonString(request)).asJson();
        if(response.getStatus()==200){
            bean = JSONUtil.convertJSONToDynamicBean(response.getBody().toString());
        }else{
            bean = JSONUtil.convertJSONToDynamicBean(response.getBody().toString());
            throw new TokenNotFoundException(bean.getPropertyAsInteger("code"),bean.get("messages"));
        }
        return bean;
    }

    private DynamicBean getSuperUserToken() throws UnirestException {

        DynamicBean bean = null;
        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.loginurlwithoutotp"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .basicAuth(env.getProperty("app.oauth2.client-id"),env.getProperty("app.oauth2.client-secret"))
                .body("grant_type=client_credentials").asJson();
        if(response.getStatus()==200){
           bean = JSONUtil.convertJSONToDynamicBean(response.getBody().toString());
        }
        return bean;
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    private boolean createCustomerOnAuthServer(CustomerDto customerDto, DynamicBean tokenAdmin) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.createuser"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ tokenAdmin.get("access_token"))
                .body(JSONUtil.toJsonString(customerDto,CustomerDto.class)).asJson();
        if(response.getStatus() == 200){
            JSONObject extId = response.getBody().getObject();
            updateUserExtId(extId.getInt("id"),customerDto);
        }
        return  response.getStatus()==200;

    }


    private void updateUserExtId(Integer extId,CustomerDto customerDto){
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(customerDto.getUsertype(), Status.ACTIVE).orElse(null);
        if(userType != null){
            User currentUser = this.userRepository.findOneByEmailIgnoreCaseAndUserType(customerDto.getEmail(),userType).orElse(null);
            if(currentUser != null){
                currentUser.setExtId(extId);
                this.userRepository.save(currentUser);
            }

        }
    }

    private boolean updatePasswordForCustomer(CustomerDto customerDto,DynamicBean tokenAdmin) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(env.getProperty("app.updatepassword"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ tokenAdmin.get("access_token"))
                .body(JSONUtil.toJsonString(customerDto,CustomerDto.class)).asJson();
        return  response.getStatus()==200;
    }


    @Override
    @Transactional
    public void createUser(UserRegisterDTO user) throws Exception {
            createAplUser(user, Constants.USER);
    }

    @Override
    @Transactional
    public void createDoctor(UserRegisterDTO user) throws Exception {
        createAplUser(user, Constants.DOCTOR);
    }

    private void createAplUser(UserRegisterDTO user, String userTypeStr) throws ApplicationException {
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(userTypeStr, Status.ACTIVE).orElse(null);

        userRepository
            .findOneByEmailIgnoreCaseAndUserType(user.getEmail(), userType)
            .ifPresent(
                    existingUser -> {
                        //boolean removed = removeNonActivatedUser(existingUser);
                        //if (!removed) {
                            throw new EmailAlreadyUsedException(ExceptionType.EMAIL_ALREADY_USE.getInfo().getTitle());
                        //}
                    }
            );
        userRepository
                .findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(user.getPrefixPhoneNumber(),user.getPhoneNumber(), userType)
                .ifPresent(
                        existingUser -> {
                            //boolean removed = removeNonActivatedUser(existingUser);
                            //if (!removed) {
                            throw new PhoneAlreadyUsedException(ExceptionType.PHONE_ALREADY_USE.getInfo().getTitle());
                            //}
                        }
                );
        user.setTypeUser(userTypeStr);
        CustomerDto customerDto = prepareCustomerDto(user);
        try{
            DynamicBean tokenBean = getSuperUserToken();
            if(tokenBean != null && createCustomerOnAuthServer(customerDto,tokenBean) && userType != null){
                User newUser = new User();
                newUser.setEmail(user.getEmail().toLowerCase());
                newUser.setPhoneNumber(user.getPhoneNumber());
                newUser.setPrefixPhoneNumber(user.getPrefixPhoneNumber());
                newUser.setLangKey(user.getLangKey()!= null ? user.getLangKey() : "en");
                newUser.setUserType(userType);
                this.save(newUser);
                userProfileService.createUserProfile(newUser);
                UserCredentialDTO userCredentialDTO = this.mapUserRegisterToCredentialDto(user);
                if("DOCTOR".equals(userType.getName()))
                    this.getOtpDoctor(userCredentialDTO);
                else
                    this.getOtpUser(userCredentialDTO);
            }else{
                throw  new ApplicationException("User did not create successfully.");
            }
        }catch (UnirestException unirestException){
            throw  new ApplicationException("Identity Server Problem");

        }catch (Exception ex){
            throw new ApplicationException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean resetPasswordUser(String email) throws Exception {
        boolean isOk = resetPasswordAplUser(email,Constants.USER);
        return isOk;
    }

    @Override
    @Transactional
    public boolean resetPasswordDoctor(String email) throws Exception {
        boolean isOk = resetPasswordAplUser(email,Constants.DOCTOR);
        return isOk;
    }

    private boolean resetPasswordAplUser(String email,String userTypeStr) throws NoSuchAlgorithmException {
        boolean isOk = false;
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(userTypeStr, Status.ACTIVE).orElse(null);
        User currentUser = this.userRepository.findOneByEmailIgnoreCaseAndUserType(email,userType).orElse(null);
        if(currentUser != null){
            String token = otpProviderImpl.generateOTP(email, null,false,9,null);
            String url = env.getProperty("app.frontend-url").concat("/").concat(userTypeStr.toLowerCase()).concat("/recoverypassword?token=").concat(token);
            sendResetPasswordEmail(currentUser,url);

            currentUser.setActivationKey(token);
            this.userRepository.save(currentUser);

            isOk=true;
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return isOk;
    }

    @Override
    @Transactional
    public boolean recoveryPassword(RecoveryPasswordDTO dto) throws Exception {
        boolean isOk=false;
        User currentUser = this.userRepository.findOneByActivationKey(dto.getToken()).orElse(null);
        if(currentUser!=null){
            try {
                DynamicBean tokenBean = getSuperUserToken();
                CustomerDto customerDto = prepareCustomerDto(currentUser);
                customerDto.setPassword(dto.getPassword());
                isOk = updatePasswordForCustomer(customerDto,tokenBean);
                if(isOk){
                    currentUser.setActivationKey(null);
                    this.userRepository.save(currentUser);
                }
                logout(tokenBean);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else{
            throw new TokenNotFoundException(403,"Token not found!");
        }
        return isOk;
    }

    private UserCredentialDTO mapUserRegisterToCredentialDto(UserRegisterDTO userRegisterDTO){
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUsername(userRegisterDTO.getEmail());
        userCredentialDTO.setUsername(userRegisterDTO.getEmail());
        userCredentialDTO.setPassword(userRegisterDTO.getPassword());
        return userCredentialDTO;
    }


}
