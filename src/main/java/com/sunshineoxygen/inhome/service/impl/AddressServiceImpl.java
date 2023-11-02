package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.model.Address;
import com.sunshineoxygen.inhome.service.IAddressService;
import com.sunshineoxygen.inhome.ui.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, AddressDTO, UUID> implements IAddressService {
}
