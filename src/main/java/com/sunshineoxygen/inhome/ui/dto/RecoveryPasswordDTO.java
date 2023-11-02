package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecoveryPasswordDTO {

    @NotNull
    private String token;
    @NotNull
    private String password;

}
