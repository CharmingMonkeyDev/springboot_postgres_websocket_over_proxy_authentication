package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.exception.BadUsageException;
import com.sunshineoxygen.inhome.exception.WrongOtpException;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.Menu;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserType;
import com.sunshineoxygen.inhome.repository.IMenuRepository;
import com.sunshineoxygen.inhome.repository.IMenuUserTypeRepository;
import com.sunshineoxygen.inhome.service.IMenuService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.service.IUserTypeService;
import com.sunshineoxygen.inhome.ui.dto.MenuDTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, MenuDTO, UUID> implements IMenuService {

    @Autowired
    IMenuRepository menuRepository;

    @Autowired
    IMenuUserTypeRepository menuUserTypeRepository;

    @Autowired
    IUserService userService;

    @Autowired
    IUserTypeService userTypeService;

    @Autowired
    private Mapper mapper;

    @Override
    public ListResponse<MenuDTO> getAllActiveMenuByUserTypeId(DynamicBean bean) {
        UserType userType = userTypeService.findUserTypeByShortCodeAndStatus(bean.get("usertype"),Status.ACTIVE).orElse(null);
        if(userType == null)
            throw new IllegalArgumentException("invalid token!");
        User user = userService.findOneByEmailIgnoreCaseAndUserType(bean.get("email"),userType).orElse(null);
        if(user == null)
            throw new IllegalArgumentException("invalid user");

        List<MenuDTO> menuDTOS = null;
       List<UUID> menuIds = menuUserTypeRepository.findAllMenuByUserTypeId(user.getUserType().getId(), Status.ACTIVE);
       if(!CollectionUtils.isEmpty(menuIds)){
          List<Menu> menus = menuRepository.findAllByIdInAndStatus(menuIds,Status.ACTIVE);
          menuDTOS = CollectionUtils.isEmpty(menus) ? null : new ArrayList<>();
          Collections.sort(menus, new Comparator<Menu>() {
               @Override
               public int compare(Menu o1, Menu o2) {
                   int index1 = menuIds.indexOf(o1.getId());
                   int index2 = menuIds.indexOf(o2.getId());
                   return Integer.compare(index1, index2);
               }
           });
          for(Menu menu : menus){
              MenuDTO menuDTO = mapper.map(menu,MenuDTO.class);
              menuDTO.setUrl("/page".concat(menu.getUrl()));
              menuDTOS.add(menuDTO);
          }
       }

        ListResponse<MenuDTO> response = null;
        if(!CollectionUtils.isEmpty(menuDTOS)){
            response = new ListResponse<>();
            response.setCount(new Long(menuDTOS.size()));
            response.setItems(menuDTOS);
        }
        return response;
    }
}
