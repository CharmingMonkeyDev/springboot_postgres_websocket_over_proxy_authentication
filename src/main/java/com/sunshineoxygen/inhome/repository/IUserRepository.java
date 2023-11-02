package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserType;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends BaseRepository<User, UUID> {

    Optional<User> findOneByEmailIgnoreCaseAndUserType(String email, UserType userType);

    Optional<User> findOneByPrefixPhoneNumberAndPhoneNumberAndUserType(String prefixPhoneNumber, String phoneNumber, UserType userType);

    Optional<User> findOneByActivationKey(String activationKey);

}
