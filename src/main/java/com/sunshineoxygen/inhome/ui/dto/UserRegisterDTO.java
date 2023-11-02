package com.sunshineoxygen.inhome.ui.dto;

import com.sunshineoxygen.inhome.enums.Status;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Data
public class UserRegisterDTO implements Serializable {

    private UUID id;

    @Email
    @Size(min = 5, max = 254)
    @NotNull
    private String email;
    @NotNull
    private String prefixPhoneNumber;
    @NotNull
    private String phoneNumber;

    @Size(min = 6, max = 14)
    private String password;

    private Status status = Status.PASSIVE;

    @Size(min = 2, max = 10)
    private String langKey;

    private String typeUser;

}
