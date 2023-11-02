package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class TokenInfoDTO {
    private static final long serialVersionUID = 1L;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private long refreshExpiresIn;
    private String tokenType;
    private String userType;
    private String userId;
    private String username;
    private String sessionState;
    private String notBeforePolicy;
    private String scope;

}
