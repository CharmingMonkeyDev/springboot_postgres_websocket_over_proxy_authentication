package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ServiceCategoryDTO {
    private UUID id;
    private String name;
    private String description;
}
