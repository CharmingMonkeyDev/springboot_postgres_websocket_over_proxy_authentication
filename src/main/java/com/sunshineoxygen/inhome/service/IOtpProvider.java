package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.OTPCode;
import org.springframework.data.jpa.repository.Query;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public interface IOtpProvider extends IBaseService<OTPCode,OTPCode, UUID> {

    String generateOTP(String email, String phoneNumber, boolean removeOld,int len,UUID userId) throws NoSuchAlgorithmException;

    Optional<OTPCode> findByStatusAndEmailOrPhoneNumberAndUserId(Status status, String email, String phoneNumber,UUID userId);

    boolean verifyOtp(String otpCode, String phoneNumber, String email,UUID userId);

    void changeStatusOtpRecord(String otpCode, String phoneNumber, String email,Status status,UUID userId);

}
