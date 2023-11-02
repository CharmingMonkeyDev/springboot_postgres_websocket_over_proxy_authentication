package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.UserType;

import java.util.Optional;
import java.util.UUID;

public interface IUserTypeService  extends IBaseService<UserType,UserType, UUID> {

    Optional<UserType> findUserTypeByShortCodeAndStatus(String shortcode, Status status);

}
