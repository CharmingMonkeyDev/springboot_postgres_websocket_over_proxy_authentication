package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.model.Address;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends BaseRepository<Address, UUID> {
}
