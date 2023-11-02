package com.sunshineoxygen.inhome.ui.dto;

import com.sunshineoxygen.inhome.model.DynamicBean;
import lombok.Data;

import java.util.List;

@Data
public class UserCredentialInfoDTO {
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private String tokenType;
    private List<String> userRole;
    private String userType;


    public static UserCredentialInfoDTO mapDynamicBeanToUserCredentialInfoDto(DynamicBean bean){
        UserCredentialInfoDTO dto = new UserCredentialInfoDTO();
        dto.setAccessToken(bean.get("access_token"));
        dto.setRefreshToken(bean.get("refresh_token"));
        dto.setExpiresIn(bean.getPropertyAsLong("expires_in"));
        dto.setUserRole(bean.getPropertyAsList("groups"));
        dto.setTokenType(bean.get("token_type"));
        return dto;

    }
}
