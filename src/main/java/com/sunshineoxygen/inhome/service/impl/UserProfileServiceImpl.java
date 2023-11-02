package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.config.S3Client;
import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.exception.ApplicationException;
import com.sunshineoxygen.inhome.exception.EmailAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.ExceptionType;
import com.sunshineoxygen.inhome.exception.PhoneAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.TokenNotFoundException;
import com.sunshineoxygen.inhome.model.Address;
import com.sunshineoxygen.inhome.model.DoctorProfessionalProfile;
import com.sunshineoxygen.inhome.model.DoctorServiceDetail;
import com.sunshineoxygen.inhome.model.DoctorTimeSlots;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserProfile;
import com.sunshineoxygen.inhome.repository.IDoctorProfessionalProfileRepository;
import com.sunshineoxygen.inhome.repository.IDoctorServiceDetailRepository;
import com.sunshineoxygen.inhome.repository.IDoctorTimeSlotsRepository;
import com.sunshineoxygen.inhome.repository.IUserProfileRepository;
import com.sunshineoxygen.inhome.repository.IUserRepository;
import com.sunshineoxygen.inhome.service.IAddressService;
import com.sunshineoxygen.inhome.service.IUserProfileService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.ui.dto.AddressDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorProfessionalProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorServiceDetailDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorTimeSlotDTO;
import com.sunshineoxygen.inhome.ui.dto.UpsertDoctorTimeSlotsDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileUpdateDTO;
import com.sunshineoxygen.inhome.utils.FileUtil;
import com.sunshineoxygen.inhome.utils.HttpDownloader;
import com.sunshineoxygen.inhome.utils.SafeFile;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import com.sunshineoxygen.inhome.utils.UploadUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl extends BaseServiceImpl<UserProfile, UserProfileDTO, UUID> implements IUserProfileService {

    @Autowired
    IUserProfileRepository userProfileRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IUserService userService;

    @Autowired
    IAddressService addressService;

    @Autowired
    IDoctorProfessionalProfileRepository doctorProfessionalProfileRepository;

    @Autowired
    IDoctorServiceDetailRepository doctorServiceDetailRepository;

    @Autowired
    IDoctorTimeSlotsRepository doctorTimeSlotsRepository;

    @Autowired
    Mapper mapper;


    @Override
    @Transactional
    public UserProfile createUserProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    @Override
    public UserProfile getUserProfile(User user) {
        return userProfileRepository.getUserProfileByUser(user);
    }

    @Override
    public DoctorProfessionalProfile getDoctorProfessionalProfile(User user){
        return doctorProfessionalProfileRepository.getDoctorProfessionalProfileByUser(user);
    }

    @Override
    public ListResponse<DoctorServiceDetailDTO> getServices(DynamicBean bean) throws Exception {
        User user = userService.getUserFromJwt(bean);
        ListResponse<DoctorServiceDetailDTO> services =null;
        if(user != null){
            List<DoctorServiceDetail> doctorServices = doctorServiceDetailRepository.findAllByUserAndStatus(user, Status.ACTIVE);
            if(doctorServices != null && doctorServices.size()>0){
                List<DoctorServiceDetailDTO> servicesDto = new ArrayList<>(doctorServices.size());
                servicesDto.addAll(doctorServices.stream().map(item -> mapper.map(item,DoctorServiceDetailDTO.class)).collect(Collectors.toList()));
                services = new ListResponse<>();
                services.setCount(new Long(doctorServices.size()));
                services.setItems(servicesDto);
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return services;
    }

    @Override
    @Transactional
    public DoctorServiceDetailDTO upsertService(DynamicBean bean, DoctorServiceDetailDTO doctorServiceDetailDTO) throws Exception {
        User user = userService.getUserFromJwt(bean);
        DoctorServiceDetailDTO result = null;
        if(user != null) {
            DoctorServiceDetail doctorServiceDetail = mapper.map(doctorServiceDetailDTO,DoctorServiceDetail.class);
            doctorServiceDetail.setUser(user);
            doctorServiceDetailRepository.save(doctorServiceDetail);
            result = mapper.map(doctorServiceDetail, DoctorServiceDetailDTO.class);

        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return result;
    }

    @Override
    @Transactional
    public DynamicBean deleteService(DynamicBean bean,UUID id) throws Exception {
        User user = userService.getUserFromJwt(bean);
        if(user != null){
            DoctorServiceDetail doctorServiceDetail = doctorServiceDetailRepository.findDoctorServiceDetailByUserAndStatusAndId(user,Status.ACTIVE,id);
            if(doctorServiceDetail != null){
                doctorServiceDetail.setStatus(Status.PASSIVE);
                bean.addProperty("message","Service deleted successfully");
                doctorServiceDetailRepository.save(doctorServiceDetail);
            }else{
                bean.addProperty("error","User has not a service!");
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return bean;
    }

    @Override
    public ListResponse<DoctorTimeSlotDTO> getServiceTimeSlots(DynamicBean bean) throws Exception {
        User user = userService.getUserFromJwt(bean);
        ListResponse<DoctorTimeSlotDTO> services =null;
        if(user != null){
            DoctorServiceDetail doctorServiceDetail = doctorServiceDetailRepository.findDoctorServiceDetailByUserAndStatusAndId(user,Status.ACTIVE,UUID.fromString(bean.get("serviceId")));
            if(doctorServiceDetail != null){
                List<DoctorTimeSlots> timeSlots = doctorTimeSlotsRepository.findAllByDoctorServiceDetailAndStatus(doctorServiceDetail,Status.ACTIVE);
                if(!CollectionUtils.isEmpty(timeSlots)){
                    services = new ListResponse<>();
                    List<DoctorTimeSlotDTO> doctorServiceDetailDTOS = new ArrayList<>(timeSlots.size());
                    for(DoctorTimeSlots doctorTimeSlot : timeSlots){
                        DoctorTimeSlotDTO dto = new DoctorTimeSlotDTO();
                        dto.setId(doctorTimeSlot.getId());
                        dto.setDoctorServiceDetail(mapper.map(doctorTimeSlot.getDoctorServiceDetail(),DoctorServiceDetailDTO.class));
                        dto.setDayOfWeek(doctorTimeSlot.getDayOfWeek());
                        dto.setStartTime(doctorTimeSlot.getStartTime());
                        dto.setEndTime(doctorTimeSlot.getEndTime());
                        doctorServiceDetailDTOS.add(dto);
                    }
                    services = new ListResponse<>();
                    services.setCount(new Long(doctorServiceDetailDTOS.size()));
                    services.setItems(doctorServiceDetailDTOS);
                }
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return services;
    }

    @Override
    @Transactional
    public ListResponse<DoctorTimeSlotDTO> upsertServiceTimeSlots(DynamicBean bean, UpsertDoctorTimeSlotsDTO doctorTimeSlots) throws Exception {
        User user = userService.getUserFromJwt(bean);
        ListResponse<DoctorTimeSlotDTO> services =null;
        if(user != null){
            DoctorServiceDetail doctorServiceDetail = doctorServiceDetailRepository.findDoctorServiceDetailByUserAndStatusAndId(user,Status.ACTIVE,doctorTimeSlots.getServiceId());
            if(doctorServiceDetail != null){
               for(DoctorTimeSlotDTO dto : doctorTimeSlots.timeSlots){
                   DoctorTimeSlots timeSlot = new DoctorTimeSlots();
                   timeSlot.setId(dto.getId());
                   timeSlot.setDoctorServiceDetail(doctorServiceDetail);
                   timeSlot.setStartTime(dto.getStartTime());
                   timeSlot.setEndTime(dto.getEndTime());
                   timeSlot.setDayOfWeek(dto.getDayOfWeek());
                   doctorTimeSlotsRepository.save(timeSlot);
               }
               bean.addProperty("serviceId",doctorServiceDetail.getId());
               services = this.getServiceTimeSlots(bean);
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return services;
    }

    @Override
    @Transactional
    public DynamicBean deleteServiceTimeSlot(DynamicBean bean, UUID id) throws Exception {
        User user = userService.getUserFromJwt(bean);
        if(user != null){
            DoctorTimeSlots doctorTimeSlots = doctorTimeSlotsRepository.findByIdAndStatus(id,Status.ACTIVE);
            if(doctorTimeSlots != null && doctorTimeSlots.getDoctorServiceDetail().getUser().getId().equals(user.getId())){
                doctorTimeSlots.setStatus(Status.PASSIVE);
                bean.addProperty("message","Time Slot deleted successfully");
                doctorTimeSlotsRepository.save(doctorTimeSlots);
            }else{
                bean.addProperty("error","User has not a slot!");
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return bean;
    }

    @Override
    public DoctorServiceDetailDTO getService(DynamicBean bean, UUID id) throws Exception {
        User user = userService.getUserFromJwt(bean);
        DoctorServiceDetailDTO doctorServiceDetailDTO =null;
        if(user != null){
            DoctorServiceDetail doctorServiceDetail = doctorServiceDetailRepository.findById(id).orElse(null);
            if(doctorServiceDetail != null && doctorServiceDetail.getUser().getId().equals(user.getId())){
                doctorServiceDetailDTO = mapper.map(doctorServiceDetail,DoctorServiceDetailDTO.class);
            }
        }
        return doctorServiceDetailDTO;
    }

    @Override
    @Transactional
    public AddressDTO setAddressByUser(DynamicBean bean, AddressDTO addressDTO) {
        User user = userService.getUserFromJwt(bean);
        AddressDTO address =null;
        if(user != null){
           UserProfile userProfile = getUserProfile(user);
           if(userProfile != null){
               address = addressService.save(mapper.map(addressDTO, Address.class));
               userProfile.setAddress(mapper.map(address, Address.class));
               userProfileRepository.save(userProfile);
               return  address;
           }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return addressDTO;
    }

    @Override
    public AddressDTO getAddress(DynamicBean bean) {
        User user = userService.getUserFromJwt(bean);
        AddressDTO address =null;
        if(user != null){
            UserProfile userProfile = getUserProfile(user);
            if(userProfile != null && userProfile.getAddress() != null){
                address = addressService.findByID(userProfile.getAddress().getId());
            }
        }
        return address;
    }

    @Override
    public Optional<UserProfile> findByNatid(String natId) {
        return userProfileRepository.findByNatid(natId);
    }

    @Override
    public UserProfileDTO getUserProfile(DynamicBean bean) throws ApplicationException, IOException {
        UserProfileDTO userProfileDTO = null;
        User user = userService.getUserFromJwt(bean);
        if(user != null){
            UserProfile userProfile = this.getUserProfile(user);
            if(userProfile != null){
                userProfileDTO = mapper.map(userProfile,UserProfileDTO.class);
                if(StringUtils.isNotEmpty(userProfile.getPhoto())){
                    byte[] image = Files.readAllBytes(Paths.get(userProfile.getPhoto()));
                    userProfileDTO.setPp(image);
                }
                userProfileDTO.setIsAllowedSms(user.getIsAllowedSms());
            }
        }
        return userProfileDTO;
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfile(DynamicBean bean, MultipartFile photo, UserProfileUpdateDTO userProfileUpdateDTO) throws IOException {
        User user = userService.getUserFromJwt(bean);
        UserProfileDTO userProfileDTO = null;
        if(user != null) {
            UserProfile userProfile = getUserProfile(user);
            if(userProfile != null ||( userProfileUpdateDTO.getId() != null && userProfile.getId().equals(userProfileUpdateDTO.getId()))){
                if(!user.getEmail().equals(userProfileUpdateDTO.getEmail())){
                    userRepository
                            .findOneByEmailIgnoreCaseAndUserType(userProfileUpdateDTO.getEmail(), user.getUserType())
                            .ifPresent(existingUser -> {throw new EmailAlreadyUsedException(ExceptionType.EMAIL_ALREADY_USE.getInfo().getTitle());}
                            );
                    user.setEmail(userProfileUpdateDTO.getEmail());
                }

                if(!user.getPrefixPhoneNumber().equals(userProfileUpdateDTO.getPrefixPhoneNumber()) || !user.getPhoneNumber().equals(userProfileUpdateDTO.getPhoneNumber())){
                    userRepository
                            .findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(userProfileUpdateDTO.getPrefixPhoneNumber(),userProfileUpdateDTO.getPhoneNumber(), user.getUserType())
                            .ifPresent(
                                    existingUser -> {
                                        throw new PhoneAlreadyUsedException(ExceptionType.PHONE_ALREADY_USE.getInfo().getTitle());
                                    }
                            );
                    user.setPrefixPhoneNumber(userProfileUpdateDTO.getPrefixPhoneNumber());
                    user.setPhoneNumber(userProfileUpdateDTO.getPhoneNumber());
                }

                if(isAnyChangeForUser(user,userProfileUpdateDTO)){
                    userRepository.save(user);
                }
                String url ="";
                if(photo != null){
                    String fileType = FileUtil.getSuffix(photo.getOriginalFilename());
                    byte [] byteArr=photo.getBytes();
                    File temp = File.createTempFile("temp","photo-".concat(user.getId().toString()).concat(".").concat(fileType));

                    try{
                        FileOutputStream osf = new FileOutputStream(temp);
                        osf.write(byteArr);
                        osf.flush();
                        osf.close();
                        url = temp.getPath();
                    }catch(Exception e) {e.printStackTrace();}
                    userProfile.setPhoto(url);
                }

                userProfile.setBirthday(userProfileUpdateDTO.getBirthday());
                userProfile.setGender(userProfileUpdateDTO.getGender());
                userProfile.setFullname(userProfileUpdateDTO.getFullName());
                userProfile.setNatid(userProfileUpdateDTO.getNatId());
                user.setIsAllowedSms(userProfileUpdateDTO.getIsAllowedSms());

                userProfileRepository.save(userProfile);

                userProfileDTO = mapper.map(userProfile,UserProfileDTO.class);
                if(StringUtils.isNotEmpty(userProfile.getPhoto())){
                    byte[] image = Files.readAllBytes(Paths.get(userProfile.getPhoto()));
                    userProfileDTO.setPp(image);
                }

            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return userProfileDTO;
    }

    @Override
    @Transactional
    public DoctorProfessionalProfileDTO upsertDoctorProfessionalProfile(DynamicBean bean, MultipartFile photo, DoctorProfessionalProfileDTO professionalProfileDTO) throws Exception {
        User user = userService.getUserFromJwt(bean);
        DoctorProfessionalProfileDTO doctorProfessionalProfileDTO = null;
        if(user != null) {
            DoctorProfessionalProfile doctorProfessionalProfile = mapper.map(professionalProfileDTO,DoctorProfessionalProfile.class);
            doctorProfessionalProfile.setUser(user);
            String url ="";
            if(photo != null){
                String fileType = FileUtil.getSuffix(photo.getOriginalFilename());
                byte [] byteArr=photo.getBytes();
                File temp = File.createTempFile("temp","photo-doctor".concat(user.getId().toString()).concat(".").concat(fileType));
                try{
                    FileOutputStream osf = new FileOutputStream(temp);
                    osf.write(byteArr);
                    osf.flush();
                    osf.close();
                    url = temp.getPath();
                }catch(Exception e) {e.printStackTrace();}
                doctorProfessionalProfile.setPhoto(url);
            }else{
                DoctorProfessionalProfile oldProfile = getDoctorProfessionalProfile(user);
                if(oldProfile != null && oldProfile.getPhoto() != null){
                    doctorProfessionalProfile.setPhoto(oldProfile.getPhoto());
                }
            }

            doctorProfessionalProfileRepository.save(doctorProfessionalProfile);
            doctorProfessionalProfileDTO = mapper.map(doctorProfessionalProfile, DoctorProfessionalProfileDTO.class);

        }
        return doctorProfessionalProfileDTO;
    }

    @Override
    public DoctorProfessionalProfileDTO getDoctorProfessionalProfile(DynamicBean bean) throws Exception {
        DoctorProfessionalProfileDTO doctorProfessionalProfileDTO = null;
        User user = userService.getUserFromJwt(bean);
        if(user != null){
            DoctorProfessionalProfile doctorProfessionalProfile = this.getDoctorProfessionalProfile(user);
            if(doctorProfessionalProfile != null){
                doctorProfessionalProfileDTO = mapper.map(doctorProfessionalProfile,DoctorProfessionalProfileDTO.class);
                if(StringUtils.isNotEmpty(doctorProfessionalProfile.getPhoto())){
                    byte[] image = Files.readAllBytes(Paths.get(doctorProfessionalProfile.getPhoto()));
                    doctorProfessionalProfileDTO.setPp(image);
                }
            }
        }
        return doctorProfessionalProfileDTO;
    }

    private boolean isAnyChangeForUser(User user, UserProfileUpdateDTO userProfileUpdateDTO){
        return !user.getEmail().equals(userProfileUpdateDTO.getEmail()) || !user.getPrefixPhoneNumber().equals(userProfileUpdateDTO.getPrefixPhoneNumber()) || !user.getPhoneNumber().equals(userProfileUpdateDTO.getPhoneNumber());
    }


}
