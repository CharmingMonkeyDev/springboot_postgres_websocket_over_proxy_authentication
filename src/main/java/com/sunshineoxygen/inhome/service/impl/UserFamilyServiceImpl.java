package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.exception.TokenNotFoundException;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.model.UserFamily;
import com.sunshineoxygen.inhome.repository.IUserFamilyRepository;
import com.sunshineoxygen.inhome.service.IUserFamilyService;
import com.sunshineoxygen.inhome.service.IUserService;
import com.sunshineoxygen.inhome.ui.dto.UserFamilyDTO;
import com.sunshineoxygen.inhome.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserFamilyServiceImpl extends BaseServiceImpl<UserFamily, UserFamilyDTO, UUID> implements IUserFamilyService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserFamilyRepository userFamilyRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ListResponse<UserFamilyDTO> getUserFamilies(DynamicBean bean) throws Exception{
        User user = userService.getUserFromJwt(bean);
        ListResponse<UserFamilyDTO> userFamilyDTOListResponse = null;
        if(user != null){
            List<UserFamily> userFamilies = userFamilyRepository.findAllByUserAndStatus(user, Status.ACTIVE);
            if(!CollectionUtils.isEmpty(userFamilies)){
               List<UserFamilyDTO> userFamilyDTOS = new ArrayList<>(userFamilies.size());
               userFamilies.forEach(item -> {
                   try{
                       UserFamilyDTO dto = mapper.map(item, UserFamilyDTO.class);
                       if(StringUtils.isNotEmpty(item.getPhoto())){
                           byte[] image = Files.readAllBytes(Paths.get(item.getPhoto()));
                           dto.setPp(image);
                       }
                       userFamilyDTOS.add(dto);
                   }catch (Exception ex){
                       ex.printStackTrace();
                   }
               });
                userFamilyDTOListResponse = new ListResponse<>();
                userFamilyDTOListResponse.setCount(new Long(userFamilyDTOS.size()));
                userFamilyDTOListResponse.setItems(userFamilyDTOS);
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return userFamilyDTOListResponse;
    }

    @Override
    @Transactional
    public UserFamilyDTO upsertFamilyRecord(DynamicBean bean, MultipartFile photo, UserFamilyDTO userFamilyDTO)  throws Exception{
        User user = userService.getUserFromJwt(bean);
        UserFamilyDTO result = null;
        if(user != null) {
            UserFamily userFamily = mapper.map(userFamilyDTO, UserFamily.class);
            userFamily.setUser(user);
            String url ="";
            if(photo != null){
                String fileType = FileUtil.getSuffix(photo.getOriginalFilename());
                byte [] byteArr=photo.getBytes();
                File temp = File.createTempFile("temp","photo-doctor".concat(user.getId().toString()).concat(".").concat(fileType));
                try{
                    FileOutputStream osf = new FileOutputStream(temp);
                    osf.write(byteArr);
                    osf.flush();
                    osf.close();
                    url = temp.getPath();
                }catch(Exception e) {e.printStackTrace();}
                userFamily.setPhoto(url);
            }else{
                if(userFamilyDTO.getId() != null){
                    UserFamily oldUserFamily = userFamilyRepository.findByUserAndStatusAndId(user,Status.ACTIVE,userFamilyDTO.getId());
                    if(oldUserFamily != null && oldUserFamily.getPhoto() != null){
                        userFamily.setPhoto(oldUserFamily.getPhoto());
                    }

                }
            }
            userFamilyRepository.save(userFamily);
            result = mapper.map(userFamily, UserFamilyDTO.class);
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }

        return result;
    }

    @Override
    @Transactional
    public DynamicBean deleteFamilyMember(DynamicBean bean, UUID id) throws Exception{
        User user = userService.getUserFromJwt(bean);
        if(user != null) {
            UserFamily userFamily = userFamilyRepository.findByUserAndStatusAndId(user, Status.ACTIVE, id);
            if(userFamily != null){
                userFamily.setStatus(Status.PASSIVE);
                userFamilyRepository.save(userFamily);
                bean.addProperty("message","Family Member deleted successfully");
            }else{
                bean.addProperty("error","User has not a family member!");
            }
        }else{
            throw new TokenNotFoundException(403,"User not found!");
        }
        return bean;
    }

    @Override
    public UserFamilyDTO getFamilyMember(DynamicBean bean, UUID fromString) throws Exception {
        User user = userService.getUserFromJwt(bean);
        UserFamilyDTO result = null;
        if(user != null) {
          UserFamily userFamily = userFamilyRepository.findByUserAndStatusAndId(user,Status.ACTIVE,fromString);
          if(userFamily != null){
              result = mapper.map(userFamily,UserFamilyDTO.class);
              if(StringUtils.isNotEmpty(userFamily.getPhoto())){
                  byte[] image = Files.readAllBytes(Paths.get(userFamily.getPhoto()));
                  result.setPp(image);
              }
          }
        }
        return result;
    }


}
