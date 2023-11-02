package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.exception.ApplicationException;
import com.sunshineoxygen.inhome.model.DoctorProfessionalProfile;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserProfile;
import com.sunshineoxygen.inhome.ui.dto.AddressDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorProfessionalProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorServiceDetailDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorTimeSlotDTO;
import com.sunshineoxygen.inhome.ui.dto.UpsertDoctorTimeSlotsDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface IUserProfileService extends IBaseService<UserProfile, UserProfileDTO, UUID> {

    UserProfile createUserProfile(User user);

    UserProfile getUserProfile(User user);

    AddressDTO setAddressByUser(DynamicBean bean, AddressDTO addressDTO);

    AddressDTO getAddress(DynamicBean bean);

    Optional<UserProfile> findByNatid(String natId);

    UserProfileDTO getUserProfile(DynamicBean bean) throws ApplicationException, IOException;

    UserProfileDTO updateUserProfile(DynamicBean bean, MultipartFile photo, UserProfileUpdateDTO userProfileUpdateDTO) throws IOException;

    DoctorProfessionalProfileDTO upsertDoctorProfessionalProfile(DynamicBean bean, MultipartFile photo, DoctorProfessionalProfileDTO professionalProfile) throws Exception;

    DoctorProfessionalProfileDTO getDoctorProfessionalProfile(DynamicBean bean) throws Exception;

    DoctorProfessionalProfile getDoctorProfessionalProfile(User user);

    ListResponse<DoctorServiceDetailDTO> getServices(DynamicBean bean) throws Exception;

    DoctorServiceDetailDTO upsertService(DynamicBean bean, DoctorServiceDetailDTO doctorServiceDetailDTO) throws Exception;

    DynamicBean deleteService(DynamicBean bean ,UUID id) throws Exception;

    ListResponse<DoctorTimeSlotDTO> getServiceTimeSlots(DynamicBean bean) throws Exception;

    ListResponse<DoctorTimeSlotDTO> upsertServiceTimeSlots(DynamicBean bean, UpsertDoctorTimeSlotsDTO doctorTimeSlots) throws Exception;

    DynamicBean deleteServiceTimeSlot(DynamicBean bean, UUID id) throws Exception;

    DoctorServiceDetailDTO getService(DynamicBean bean, UUID id) throws Exception;;
}
