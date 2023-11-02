package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.Menu;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.service.IBaseService;
import com.sunshineoxygen.inhome.service.IMenuService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.ui.dto.MenuDTO;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MenuController extends BaseController<UUID,Menu> {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserService userService;

    @PostMapping("/menu/getAllByType")
    public ResponseEntity getAllByType(Principal principal) throws UnsupportedEncodingException {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        ListResponse<MenuDTO> response =  null;
        try {
            response  = menuService.getAllActiveMenuByUserTypeId(bean);
        }catch (Exception ex){
            logger.error("[MENUCONTROLLER][GETALL][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(response);
    }

    @Override
    IBaseService getBaseService() {
        return menuService;
    }
}
