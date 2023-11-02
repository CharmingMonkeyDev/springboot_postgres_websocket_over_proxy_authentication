package com.sunshineoxygen.inhome.service;

//import com.sun.media.sound.InvalidDataException;
import com.sunshineoxygen.inhome.exception.EmailAlreadyUsedException;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserType;
import com.sunshineoxygen.inhome.ui.dto.RecoveryPasswordDTO;
import com.sunshineoxygen.inhome.ui.dto.TokenInfoDTO;
import com.sunshineoxygen.inhome.ui.dto.UserCredentialDTO;
import com.sunshineoxygen.inhome.ui.dto.UserCredentialInfoDTO;
import com.sunshineoxygen.inhome.ui.dto.UserRegisterDTO;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

public interface IUserService  extends IBaseService<User, User, UUID>{


    Optional<User> findOneByEmailIgnoreCaseAndUserType(String email, UserType userType);

    Optional<User> findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(String prefixPhoneNumber, String phoneNumber, UserType userType);

    DynamicBean getOtpUser(@Valid UserCredentialDTO user) throws Exception;

    DynamicBean getOtpDoctor(@Valid UserCredentialDTO user) throws Exception;

    TokenInfoDTO getAccessTokenFromRefreshToken(String refreshToken) throws Exception;

    void createUser(UserRegisterDTO user) throws Exception;

    void createDoctor(UserRegisterDTO user) throws EmailAlreadyUsedException,Exception ;

    boolean resetPasswordUser(String email) throws Exception;

    boolean resetPasswordDoctor(String email) throws Exception;

    boolean recoveryPassword(RecoveryPasswordDTO dto) throws Exception;

    UserCredentialInfoDTO verifyOtpUser(UserCredentialDTO user) throws Exception;

    UserCredentialInfoDTO verifyOtpDoctor(UserCredentialDTO user) throws Exception;

    User getUserFromJwt(DynamicBean jwtBean) throws IllegalArgumentException;

    void logout(DynamicBean token) throws Exception;
}
