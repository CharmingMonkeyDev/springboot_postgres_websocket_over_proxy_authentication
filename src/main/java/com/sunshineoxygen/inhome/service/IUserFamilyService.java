package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.UserFamily;
import com.sunshineoxygen.inhome.ui.dto.UserFamilyDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IUserFamilyService extends IBaseService<UserFamily, UserFamilyDTO, UUID> {

    ListResponse<UserFamilyDTO> getUserFamilies(DynamicBean bean) throws Exception;

    UserFamilyDTO upsertFamilyRecord(DynamicBean bean, MultipartFile photo, UserFamilyDTO userFamilyDTO) throws Exception;

    DynamicBean deleteFamilyMember(DynamicBean bean, UUID id) throws Exception;

    UserFamilyDTO getFamilyMember(DynamicBean bean, UUID fromString) throws Exception;
}
