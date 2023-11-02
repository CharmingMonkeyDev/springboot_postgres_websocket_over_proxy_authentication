package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.DoctorServiceDetail;
import com.sunshineoxygen.inhome.model.DoctorTimeSlots;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.UserProfile;
import com.sunshineoxygen.inhome.service.IAddressService;
import com.sunshineoxygen.inhome.service.IBaseService;
import com.sunshineoxygen.inhome.service.IUserProfileService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.ui.dto.AddressDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorProfessionalProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorServiceDetailDTO;
import com.sunshineoxygen.inhome.ui.dto.DoctorTimeSlotDTO;
import com.sunshineoxygen.inhome.ui.dto.TokenInfoDTO;
import com.sunshineoxygen.inhome.ui.dto.UpsertDoctorTimeSlotsDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileDTO;
import com.sunshineoxygen.inhome.ui.dto.UserProfileUpdateDTO;
import com.sunshineoxygen.inhome.utils.SecurityUtils;
import io.micrometer.core.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController extends BaseController<UUID, UserProfile> {

    final static Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IUserService userService;

    @Override
    IBaseService getBaseService() {
        return userProfileService;
    }

    @PostMapping("setAddress")
    public ResponseEntity setAddressByUser(Principal principal, @RequestBody AddressDTO addressDTO) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        AddressDTO address = null;
        try {
            address = userProfileService.setAddressByUser(bean,addressDTO);
        }catch (Exception ex){
            logger.error("[UserProfileController][setAddressByUser][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    }

    @PostMapping("getAddress")
    public ResponseEntity getAddressByUser(Principal principal) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        AddressDTO address = null;
        try {
            address = userProfileService.getAddress(bean);
        }catch (Exception ex){
            logger.error("[UserProfileController][getAddressByUser][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<AddressDTO>(address, HttpStatus.OK);
    }

    @PostMapping("getUserProfile")
    public ResponseEntity getUserProfile(Principal principal) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        UserProfileDTO userProfileDTO = null;
        try {
            userProfileDTO = userProfileService.getUserProfile(bean);
        }catch (Exception ex){
            logger.error("[UserProfileController][getUserProfile][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<UserProfileDTO>(userProfileDTO, HttpStatus.OK);
    }

    @PostMapping("setUserProfile")
    public ResponseEntity setUserProfile(Principal principal, @RequestPart @Nullable MultipartFile photo, @RequestPart UserProfileUpdateDTO userProfileUpdateDTO) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        UserProfileDTO userProfileDTO = null;
        try {
            userProfileDTO = userProfileService.updateUserProfile(bean,photo,userProfileUpdateDTO);
        }catch (Exception ex){
            logger.error("[UserProfileController][setUserProfile][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<UserProfileDTO>(userProfileDTO, HttpStatus.OK);
    }

    @PostMapping("setProfessionalProfile")
    public ResponseEntity setProfessionalProfile(Principal principal, @RequestPart @Nullable MultipartFile photo, @RequestPart DoctorProfessionalProfileDTO professionalProfile) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DoctorProfessionalProfileDTO doctorProfessionalProfileDTO = null;
        try {
            doctorProfessionalProfileDTO = userProfileService.upsertDoctorProfessionalProfile(bean,photo,professionalProfile);
        }catch (Exception ex){
            logger.error("[UserProfileController][setProfessionalProfile][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<DoctorProfessionalProfileDTO>(doctorProfessionalProfileDTO, HttpStatus.OK);
    }

    @PostMapping("getProfessionalProfile")
    public ResponseEntity getProfessionalProfile(Principal principal) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DoctorProfessionalProfileDTO doctorProfessionalProfileDTO = null;
        try {
            doctorProfessionalProfileDTO = userProfileService.getDoctorProfessionalProfile(bean);
        }catch (Exception ex){
            logger.error("[UserProfileController][getProfessionalProfile][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<DoctorProfessionalProfileDTO>(doctorProfessionalProfileDTO, HttpStatus.OK);
    }

    @PostMapping("/getService")
    public ResponseEntity getService(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DoctorServiceDetailDTO result = null;
        try {
            result = userProfileService.getService(bean,UUID.fromString(id));
        }catch (Exception ex){
            logger.error("[UserProfileController][getService][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<DoctorServiceDetailDTO>(result, HttpStatus.OK);

    }

    @PostMapping("getServices")
    public ResponseEntity getServices(Principal principal) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        ListResponse<DoctorServiceDetailDTO> services = null;
        try {
            services = userProfileService.getServices(bean);
        }catch (Exception ex){
            logger.error("[UserProfileController][getServices][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(services);
    }

    @PostMapping("setService")
    public ResponseEntity setService(Principal principal, @RequestBody DoctorServiceDetailDTO doctorServiceDetailDTO) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DoctorServiceDetailDTO result = null;
        try {
            result = userProfileService.upsertService(bean,doctorServiceDetailDTO);
        }catch (Exception ex){
            logger.error("[UserProfileController][setService][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<DoctorServiceDetailDTO>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteService")
    public ResponseEntity deleteService(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DynamicBean result = userProfileService.deleteService(bean,UUID.fromString(id));
        if(result.get("error") != null){
            logger.error("[UserProfileController][deleteService][EXCEPTION][ERROR] : {}", result.get("error"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else{
            return ResponseEntity.ok(result);
        }

    }

    @PostMapping("getServiceTimeSlots")
    public ResponseEntity getServiceTimeSlots(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        bean.addProperty("serviceId",id);
        ListResponse<DoctorTimeSlotDTO> services = null;
        try {
            services = userProfileService.getServiceTimeSlots(bean);
        }catch (Exception ex){
            logger.error("[UserProfileController][getServiceTimeSlots][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(services);
    }

    @PostMapping("upsertServiceTimeSlots")
    public ResponseEntity upsertServiceTimeSlots(Principal principal, @RequestBody UpsertDoctorTimeSlotsDTO doctorTimeSlots) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        ListResponse<DoctorTimeSlotDTO> result = null;
        try {
            result = userProfileService.upsertServiceTimeSlots(bean,doctorTimeSlots);
        }catch (Exception ex){
            logger.error("[UserProfileController][upsertServiceTimeSlots][EXCEPTION][ERROR] : {}", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteServiceTimeSlot")
    public ResponseEntity deleteServiceTimeSlot(Principal principal,@RequestParam(value = "id", required = false) String id) throws Exception {
        DynamicBean bean = SecurityUtils.getDecodedJwt(principal);
        DynamicBean result = userProfileService.deleteServiceTimeSlot(bean,UUID.fromString(id));
        if(result.get("error") != null){
            logger.error("[UserProfileController][deleteServiceTimeSlot][EXCEPTION][ERROR] : {}", result.get("error"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }else{
            return ResponseEntity.ok(result);
        }

    }
}
