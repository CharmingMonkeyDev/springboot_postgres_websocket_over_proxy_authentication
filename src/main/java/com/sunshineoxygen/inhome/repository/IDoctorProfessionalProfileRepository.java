package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.model.DoctorProfessionalProfile;
import com.sunshineoxygen.inhome.model.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDoctorProfessionalProfileRepository extends BaseRepository<DoctorProfessionalProfile, UUID> {

    DoctorProfessionalProfile getDoctorProfessionalProfileByUser(User user);

}
