package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.Currency;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.ServiceCategory;
import com.sunshineoxygen.inhome.repository.ICurrencyRepository;
import com.sunshineoxygen.inhome.repository.IServiceCategoryRepository;
import com.sunshineoxygen.inhome.service.IMetaService;
import com.sunshineoxygen.inhome.ui.dto.ServiceCategoryDTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetaServiceImpl implements IMetaService {

    @Autowired
    IServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    ICurrencyRepository currencyRepository;

    @Autowired
    Mapper mapper;

    @Override
    public ListResponse<ServiceCategoryDTO> getAllActiveServiceCategories() {
        ListResponse<ServiceCategoryDTO> serviceCategoryListResponse = null;
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.getAllByStatus(Status.ACTIVE);
        if(!CollectionUtils.isEmpty(serviceCategories)){
            List<ServiceCategoryDTO> dtos = new ArrayList<>(serviceCategories.size());
            serviceCategories.stream().forEach(item -> {
                dtos.add(mapper.map(item, ServiceCategoryDTO.class));
            });

            serviceCategoryListResponse = new ListResponse<>();
            serviceCategoryListResponse.setCount(new Long(serviceCategories.size()));
            serviceCategoryListResponse.setItems(dtos);
        }
        return serviceCategoryListResponse;
    }

    @Override
    public ListResponse<Currency> getAllActiveCurrencies() {
        ListResponse<Currency> currencyListResponse = null;
        List<Currency> currencyList = currencyRepository.getAllByStatus(Status.ACTIVE);
        if(!CollectionUtils.isEmpty(currencyList)){
            currencyListResponse = new ListResponse<>();
            currencyListResponse.setCount(new Long(currencyList.size()));
            currencyListResponse.setItems(currencyList);
        }
        return currencyListResponse;
    }
}
