package com.sunshineoxygen.inhome.ui.controller;

import com.google.gson.JsonObject;
import com.sunshineoxygen.inhome.exception.EmailAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.PhoneAlreadyUsedException;
import com.sunshineoxygen.inhome.exception.TokenNotFoundException;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.ui.dto.RecoveryPasswordDTO;
import com.sunshineoxygen.inhome.ui.dto.TokenInfoDTO;
import com.sunshineoxygen.inhome.ui.dto.UserCredentialDTO;
import com.sunshineoxygen.inhome.ui.dto.UserCredentialInfoDTO;
import com.sunshineoxygen.inhome.ui.dto.UserRegisterDTO;
import com.sunshineoxygen.inhome.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private IUserService userService;

    @PostMapping("/createuser")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegisterDTO user) {
        try {
            userService.createUser(user);
        }catch (EmailAlreadyUsedException ex){
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][EXCEPTION][ERROR] : {}", ex);
           throw ex;
        }
        catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createdoctor")
    public ResponseEntity<String> createDoctor(@Valid @RequestBody UserRegisterDTO user) {
        try {
            userService.createDoctor(user);
        }catch (EmailAlreadyUsedException ex){
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][EXCEPTION][ERROR] : {}", ex);
            throw ex;
        }catch (PhoneAlreadyUsedException ex){
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][EXCEPTION][ERROR] : {}", ex);
            throw ex;
        }
        catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credential/user/otp")
    public ResponseEntity<?> getOtpUser(@Valid @RequestBody UserCredentialDTO user,
                                              HttpServletRequest request) throws Exception {
        DynamicBean info = null;
        try {
            info = userService.getOtpUser(user);
        } catch (TokenNotFoundException e) {
            logger.error("[USERCREDETIALCONTROLLER][GETOTP][TOKENNOTFOUNDEXCEPTION][ERROR] : {}", e);
            throw e;
        } catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][GETOTP][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//		return ResponseEntity.ok().headers(responseHeaders).body(user); // Diger kullanım
        return ResponseEntity.ok().body(info.getProperties());
    }

    @PostMapping("/credential/doctor/otp")
    public ResponseEntity<?> getOtpDoctor(@Valid @RequestBody UserCredentialDTO user,
                                        HttpServletRequest request) throws Exception {
        DynamicBean info = null;
        try {
            info = userService.getOtpDoctor(user);
        } catch (TokenNotFoundException e) {
            logger.error("[USERCREDETIALCONTROLLER][GETOTP][TOKENNOTFOUNDEXCEPTION][ERROR] : {}", e);
            throw e;
        } catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][GETOTP][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//		return ResponseEntity.ok().headers(responseHeaders).body(user); // Diger kullanım
        return ResponseEntity.ok().body(info.getProperties());
    }

    @PostMapping("/credential/user/verifyotp")
    public ResponseEntity<UserCredentialInfoDTO> verifyUserOtp(@Valid @RequestBody UserCredentialDTO user,
                                                    HttpServletRequest request) throws Exception {
        UserCredentialInfoDTO info = null;
        try {
            info = userService.verifyOtpUser(user);
            if(info == null)
                throw new TokenNotFoundException(409,"Invalid token");
        } catch (TokenNotFoundException e) {
            logger.error("[USERCREDETIALCONTROLLER][VERIFYOTP][TOKENNOTFOUNDEXCEPTION][ERROR] : {}", e);
            throw e;
        } catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][VERIFYOTP][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//		return ResponseEntity.ok().headers(responseHeaders).body(user); // Diger kullanım
        return new ResponseEntity<UserCredentialInfoDTO>(info, HttpStatus.OK);
    }

    @PostMapping("/credential/doctor/verifyotp")
    public ResponseEntity<UserCredentialInfoDTO> verifyDoctorOtp(@Valid @RequestBody UserCredentialDTO user,
                                                               HttpServletRequest request) throws Exception {
        UserCredentialInfoDTO info = null;
        try {
            info = userService.verifyOtpDoctor(user);
            if(info == null)
                throw new TokenNotFoundException(409,"Invalid token");
        } catch (TokenNotFoundException e) {
            logger.error("[USERCREDETIALCONTROLLER][VERIFYOTP][TOKENNOTFOUNDEXCEPTION][ERROR] : {}", e);
            throw e;
        } catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][VERIFYOTP][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
//		return ResponseEntity.ok().headers(responseHeaders).body(user); // Diger kullanım
        return new ResponseEntity<UserCredentialInfoDTO>(info, HttpStatus.OK);
    }

    @PostMapping("/user/resetpassword")
    public ResponseEntity<Void> resetUserPassword(@RequestParam("email") String email) throws Exception {
        boolean isOk = false;
        try{
            isOk  = userService.resetPasswordUser(email);
        }catch (TokenNotFoundException e){
            throw e;
        }
        return isOk ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/doctor/resetpassword")
    public ResponseEntity<Void> resetDoctorPassword(@RequestParam("email") String email) throws Exception {
        boolean isOk = false;
        try{
            isOk  = userService.resetPasswordDoctor(email);
        }catch (TokenNotFoundException e){
            throw e;
        }
        return isOk ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody RecoveryPasswordDTO recoveryPasswordDTO) throws Exception {
        boolean isOk = false;
        try{
            isOk  = userService.recoveryPassword(recoveryPasswordDTO);
        }catch (TokenNotFoundException e){
            throw e;
        }
        return isOk ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }



    @PostMapping("/accessToken")
    public ResponseEntity<TokenInfoDTO> getAccessTokenFromRefreshToken(@RequestBody String refreshToken)
            throws Exception {
        TokenInfoDTO result = null;
        try {
            result = userService.getAccessTokenFromRefreshToken(refreshToken);
        } catch (TokenNotFoundException e) {
            logger.error("[USERCREDETIALCONTROLLER][GETTOKEN][TOKENNOTFOUNDEXCEPTION][ERROR] : {}", e);
            throw e;
        } catch (Exception e) {
            logger.error("[USERCREDETIALCONTROLLER][GETACCESSTOKENFROMREFRESHTOKEN][EXCEPTION][ERROR] : {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<TokenInfoDTO>(result, HttpStatus.OK);
    }
}
