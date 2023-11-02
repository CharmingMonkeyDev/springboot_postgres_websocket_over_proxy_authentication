package com.sunshineoxygen.inhome.config;

import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.utils.Constants;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    @Autowired
    IUserService userService;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        String from = null;//SecurityUtils.getCurrentLogin();
        return Optional.of(UUID.fromString(from == null ? Constants.SYSTEM : from));
    }
}