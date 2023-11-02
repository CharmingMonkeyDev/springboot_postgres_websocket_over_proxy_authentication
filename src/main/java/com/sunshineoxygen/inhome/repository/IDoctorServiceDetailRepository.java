package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.DoctorServiceDetail;
import com.sunshineoxygen.inhome.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDoctorServiceDetailRepository extends BaseRepository<DoctorServiceDetail, UUID> {

    List<DoctorServiceDetail> findAllByUserAndStatus(User user, Status status);

    DoctorServiceDetail findDoctorServiceDetailByUserAndStatusAndId(User user,Status status,UUID id);

}
