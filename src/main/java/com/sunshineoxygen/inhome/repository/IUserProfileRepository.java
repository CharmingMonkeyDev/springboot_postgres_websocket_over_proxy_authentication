package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserProfileRepository extends BaseRepository<UserProfile, UUID> {

    UserProfile getUserProfileByUser(User user);

    Optional<UserProfile> findByNatid(String natId);

}
