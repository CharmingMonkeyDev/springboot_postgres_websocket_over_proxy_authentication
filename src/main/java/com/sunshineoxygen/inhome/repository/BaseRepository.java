package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.exception.BadUsageException;
import com.sunshineoxygen.inhome.model.ListResponse;
import com.sunshineoxygen.inhome.model.SortField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    public ListResponse<T> findByCriteria(MultiValueMap<String, String> queryParameters, Integer page, Integer size, SortField sortField, Class<T> entityClass) throws BadUsageException;

}