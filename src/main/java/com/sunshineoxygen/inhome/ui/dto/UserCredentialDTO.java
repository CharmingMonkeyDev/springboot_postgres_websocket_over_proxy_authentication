package com.sunshineoxygen.inhome.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserCredentialDTO {

    private String username;

    private String password;

    private String type;

    private String otp;

    private String resultMessage;

    private Boolean requestSms;
}
