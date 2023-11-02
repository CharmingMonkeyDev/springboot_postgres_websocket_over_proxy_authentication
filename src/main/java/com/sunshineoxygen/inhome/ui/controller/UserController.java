package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.service.IBaseService;
import com.sunshineoxygen.inhome.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController extends BaseController<UUID, User>{

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;
    @Override
    IBaseService getBaseService() {
        return userService;
    }
}
