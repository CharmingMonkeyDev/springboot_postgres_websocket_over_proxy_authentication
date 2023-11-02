package com.sunshineoxygen.inhome.service;

import com.sunshineoxygen.inhome.model.Currency;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.ServiceCategory;
import com.sunshineoxygen.inhome.ui.dto.ServiceCategoryDTO;

import java.util.List;

public interface IMetaService {

    ListResponse<ServiceCategoryDTO> getAllActiveServiceCategories();

    ListResponse<Currency> getAllActiveCurrencies();

}
