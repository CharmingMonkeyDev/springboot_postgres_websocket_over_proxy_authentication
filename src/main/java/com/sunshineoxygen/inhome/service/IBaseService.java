package com.sunshineoxygen.inhome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IBaseService<T, D, ID> {
    final static Logger logger = LoggerFactory.getLogger(IBaseService.class);

    public D save(T t) throws DataAccessException;
    List<T> saveAll(List<T> tList) throws DataAccessException;
    void delete(ID id) throws DataAccessException;
    public void passive(ID id) throws DataAccessException;
    List<D> findAll() throws DataAccessException;
    D findByID(ID id) throws DataAccessException;
}