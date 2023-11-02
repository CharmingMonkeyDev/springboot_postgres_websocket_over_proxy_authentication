package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.UserFamily;
import com.sunshineoxygen.inhome.service.IBaseService;
import com.sunshineoxygen.inhome.service.IUserFamilyService;
import com.sunshineoxygen.inhome.ui.dto.UserFamilyDTO;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/family")
public class UserFamilyController extends BaseController<UUID, UserFamily> {
    @Autowired
    private IUserFamilyService userFamilyService;

    @PostMapping("/getAll")
    public ResponseEntity getAll(Principal principal) throws UnsupportedEncodingException {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        ListResponse<UserFamilyDTO> userFamilies = null;
        try {
            userFamilies = userFamilyService.getUserFamilies(bean);
        }catch (Exception ex){
            logger.error("[UserFamilyController][getAll][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(userFamilies);
    }

    @PostMapping("upsertFamilyMember")
    public ResponseEntity upsertFamilyRecord(Principal principal,@RequestPart @Nullable MultipartFile photo, @RequestPart UserFamilyDTO userFamilyDTO) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        UserFamilyDTO userFamily = null;
        try {
            userFamily = userFamilyService.upsertFamilyRecord(bean,photo,userFamilyDTO);
        }catch (Exception ex){
            logger.error("[UserFamilyController][upsertFamilyRecord][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(userFamily);
    }

    @DeleteMapping("deleteFamilyMember")
    public ResponseEntity deleteFamilyMember(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DynamicBean result = userFamilyService.deleteFamilyMember(bean,UUID.fromString(id));
        if(result.get("error") != null){
            logger.error("[UserFamilyController][deleteFamilyMember][EXCEPTION][ERROR] : {}", result.get("error"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else{
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/getFamilyMember")
    public ResponseEntity getFamilyMember(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        UserFamilyDTO userFamily = null;
        try {
            userFamily = userFamilyService.getFamilyMember(bean,UUID.fromString(id));
        }catch (Exception ex){
            logger.error("[UserFamilyController][getFamilyMember][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(userFamily);
    }

    @Override
    IBaseService getBaseService() {
        return userFamilyService;
    }
}
