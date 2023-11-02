package com.sunshineoxygen.inhome.utils;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.ui.dto.TokenInfoDTO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class SecurityUtils {

    public static Authentication authenticateIfRequired() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return authentication;
    }

    public static DynamicBean getDecodedJwt(Principal principal) throws UnsupportedEncodingException {
        DynamicBean dynamicBean = new DynamicBean();
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        String jwt = ((OAuth2AuthenticationDetails)authentication.getDetails()).getTokenValue();
        String[] pieces = jwt.split("\\.");
        String b64payload = pieces[1];
        String jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");
        dynamicBean = JSONUtil.convertJSONToDynamicBean(jsonString);
        return dynamicBean;
    }


}
