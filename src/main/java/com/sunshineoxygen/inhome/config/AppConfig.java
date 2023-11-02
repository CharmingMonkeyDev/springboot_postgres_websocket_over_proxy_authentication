package com.sunshineoxygen.inhome.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AppConfig {

    @Value("${security.oauth2.resource.tokenInfoUri}")
    public String tokenInfoUri;

    @Value("${security.oauth2.client.client-id}")
    public String clientId;

    @Value("${security.oauth2.client.client-secret}")
    public String clientSecret;
}
