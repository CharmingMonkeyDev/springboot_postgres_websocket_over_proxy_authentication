package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.Currency;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface ICurrencyRepository extends BaseRepository<Currency, UUID> {

    List<Currency> getAllByStatus(Status status);
}
