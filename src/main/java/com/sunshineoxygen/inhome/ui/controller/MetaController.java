package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.Currency;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.ServiceCategory;
import com.sunshineoxygen.inhome.service.IMetaService;
import com.sunshineoxygen.inhome.ui.dto.ServiceCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/meta")
public class MetaController {

    final static Logger logger = LoggerFactory.getLogger(MetaController.class);

    @Autowired
    IMetaService metaService;

    @PostMapping("getCurencies")
    public ResponseEntity getCurencies(Principal principal)  {
        ListResponse<Currency> response= null;
        try {
            response = metaService.getAllActiveCurrencies();
        }catch (Exception ex){
            logger.error("[METACONTROLLER][GETCURRENCIES][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return  ResponseEntity.ok(response);
    }

    @PostMapping("getServiceCategories")
    public ResponseEntity getServiceCategories(Principal principal)  {
        ListResponse<ServiceCategoryDTO> response= null;
        try {
            response = metaService.getAllActiveServiceCategories();
        }catch (Exception ex){
            logger.error("[METACONTROLLER][GETSERVICECATEGORIES][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return  ResponseEntity.ok(response);
    }


}
