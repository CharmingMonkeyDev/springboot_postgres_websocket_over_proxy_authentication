package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.OTPCode;
import com.sunshineoxygen.inhome.repository.IOTPCodeRepository;
import com.sunshineoxygen.inhome.service.IOtpProvider;
import com.sunshineoxygen.inhome.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class OTPProviderImpl extends BaseServiceImpl<OTPCode,OTPCode, UUID> implements IOtpProvider {

    @Autowired
    private IOTPCodeRepository iotpCodeRepository;

    @Override
    public String generateOTP(String email, String phoneNumber,boolean removeOld,int len,UUID userId) throws NoSuchAlgorithmException {
        // Generate a random 6-digit OTP
        if(removeOld)
            removeOldOtp(email, phoneNumber,userId);
        SecureRandom sr = new SecureRandom();
        String result = (sr.nextInt(9)+1) +"";
        for(int i=0; i<len-2; i++) result += sr.nextInt(10);
        result += (sr.nextInt(9)+1);
        return result;
    }

    private void removeOldOtp(String email, String phoneNumber,UUID userId) {
        OTPCode existingCode = this.findByStatusAndEmailOrPhoneNumberAndUserId(Status.ACTIVE, email, phoneNumber,userId).orElse(null);
        if(existingCode != null){
            this.changeStatusOtpRecord(existingCode.getCode(), existingCode.getPhoneNumber(),existingCode.getEmail(),Status.PASSIVE,userId);
        }
    }


    @Override
    public Optional<OTPCode> findByStatusAndEmailOrPhoneNumberAndUserId(Status status, String email, String phoneNumber,UUID userId) {
        return iotpCodeRepository.findByStatusAndEmailOrPhoneNumberAndUserId(status,email,phoneNumber,userId);
    }

    @Override
    public boolean verifyOtp(String otpCode, String phoneNumber, String email,UUID userId) {
        OTPCode existingCode = this.findByStatusAndEmailOrPhoneNumberAndUserId(Status.ACTIVE,email,phoneNumber,userId).orElse(null);
        if(existingCode != null && existingCode.getExpiredDate().after(new Date()) && otpCode.equals(existingCode.getCode())){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void changeStatusOtpRecord(String otpCode, String phoneNumber, String email,Status status,UUID userId) {
        OTPCode existingCode = this.findByStatusAndEmailOrPhoneNumberAndUserId(Status.ACTIVE,email,phoneNumber,userId).orElse(null);
        if(existingCode != null){
            existingCode.setStatus(status);
            this.repository.save(existingCode);
        }
    }

}
