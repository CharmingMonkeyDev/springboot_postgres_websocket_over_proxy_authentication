package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.UserType;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserTypeRepository extends BaseRepository<UserType, UUID> {

    Optional<UserType> findUserTypeByShortCodeAndStatus(String shortcode, Status status);

}
