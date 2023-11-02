package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;

import javax.persistence.Column;
import java.util.UUID;

@Data
public class AddressDTO {

    private UUID id;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Float latitude;

    private Float longitude;
}
