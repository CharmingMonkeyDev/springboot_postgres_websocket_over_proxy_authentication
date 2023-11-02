package com.sunshineoxygen.inhome.ui.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpsertDoctorTimeSlotsDTO {

    public UUID serviceId;

    public List<DoctorTimeSlotDTO> timeSlots;

}


