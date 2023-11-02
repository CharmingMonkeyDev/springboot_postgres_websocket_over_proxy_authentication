package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.ServiceCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IServiceCategoryRepository extends BaseRepository<ServiceCategory, UUID> {

    List<ServiceCategory> getAllByStatus(Status status);

}
