package com.sunshineoxygen.inhome.ui.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sunshineoxygen.inhome.utils.JsonDateDeserializer;
import com.sunshineoxygen.inhome.utils.JsonDateSerializer;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class UserProfileDTO {

    private UUID id;

    private AddressDTO address;

    private String fullname;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp birthday;

    private String natid;

    private byte[] pp;

    private String gender;

    private Boolean isAllowedSms;
}
