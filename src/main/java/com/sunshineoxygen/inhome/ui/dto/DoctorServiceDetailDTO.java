package com.sunshineoxygen.inhome.ui.dto;

import com.sunshineoxygen.inhome.model.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DoctorServiceDetailDTO {
    private UUID id;
    private ServiceCategoryDTO serviceCategory;
    private Currency currency;
    private UUID userId;
    private String serviceTitle;
    private Double serviceFee;
    private String description;
}
