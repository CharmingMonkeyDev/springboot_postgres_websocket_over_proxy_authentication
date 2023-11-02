package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.UserType;
import com.sunshineoxygen.inhome.repository.IUserTypeRepository;
import com.sunshineoxygen.inhome.service.IUserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserTypeServiceImpl extends BaseServiceImpl<UserType, UserType, UUID> implements IUserTypeService {


    @Autowired
    private IUserTypeRepository userTypeRepository;


    @Override
    public Optional<UserType> findUserTypeByShortCodeAndStatus(String shortcode, Status status) {
        return userTypeRepository.findUserTypeByShortCodeAndStatus(shortcode,status);
    }
}
