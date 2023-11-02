package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class UserProfileUpdateDTO {

    private UUID id;

    @NotNull
    private String email;

    @NotNull
    private String prefixPhoneNumber;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String fullName;

    private Timestamp birthday;

    @NotNull
    private String natId;

    private String photo;

    private String gender;

    private Boolean isAllowedSms;
}
