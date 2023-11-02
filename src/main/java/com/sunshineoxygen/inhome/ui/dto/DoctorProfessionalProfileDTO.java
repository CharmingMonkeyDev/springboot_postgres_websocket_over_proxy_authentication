package com.sunshineoxygen.inhome.ui.dto;
import com.sunshineoxygen.inhome.model.Currency;
import com.sunshineoxygen.inhome.model.ServiceCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class DoctorProfessionalProfileDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String photo;
    private byte[] pp;
    private String professionalDetails;
    private Integer yearsOfExperience;
    private String location;
    private String licenceNo;
    private Double consultationFee;
    private ServiceCategory serviceCategory;
    private Currency currency;
    private UUID serviceCategoryId;
    private UUID currencyId;
}
