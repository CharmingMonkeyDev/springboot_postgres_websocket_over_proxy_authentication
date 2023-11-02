package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserFamily;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IUserFamilyRepository  extends BaseRepository<UserFamily, UUID> {

    List<UserFamily> findAllByUserAndStatus(User user, Status status);

    UserFamily findByUserAndStatusAndId(User user, Status status, UUID id);

}
