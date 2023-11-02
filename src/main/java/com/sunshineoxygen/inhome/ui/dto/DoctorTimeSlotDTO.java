package com.sunshineoxygen.inhome.ui.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sunshineoxygen.inhome.model.DoctorServiceDetail;
import com.sunshineoxygen.inhome.utils.JsonDateDeserializer;
import com.sunshineoxygen.inhome.utils.JsonDateSerializer;
import com.sunshineoxygen.inhome.utils.JsonDateTimeDeserializer;
import com.sunshineoxygen.inhome.utils.JsonDateTimeSerializer;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class DoctorTimeSlotDTO {
    private UUID id;
    private DoctorServiceDetailDTO doctorServiceDetail;
    private Integer dayOfWeek;

    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalTime startTime;

    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private LocalTime   endTime;
}
