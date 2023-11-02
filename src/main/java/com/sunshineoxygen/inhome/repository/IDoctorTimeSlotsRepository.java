package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.DoctorServiceDetail;
import com.sunshineoxygen.inhome.model.DoctorTimeSlots;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDoctorTimeSlotsRepository extends BaseRepository<DoctorTimeSlots, UUID> {

    List<DoctorTimeSlots> findAllByDoctorServiceDetailAndStatus(DoctorServiceDetail doctorServiceDetail, Status status);

    DoctorTimeSlots findByIdAndStatus(UUID id, Status status);

}
