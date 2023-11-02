package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.Menu;
import com.sunshineoxygen.inhome.ui.dto.MenuDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMenuService extends IBaseService<Menu, MenuDTO, UUID> {

    ListResponse<MenuDTO> getAllActiveMenuByUserTypeId(DynamicBean bean);

}
