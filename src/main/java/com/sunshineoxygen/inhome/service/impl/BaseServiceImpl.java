package com.sunshineoxygen.inhome.service.impl;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.repository.BaseRepository;
import com.sunshineoxygen.inhome.model.BaseEntity;
import com.sunshineoxygen.inhome.service.IBaseService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true, timeout = 60)
public class BaseServiceImpl<T, D, ID extends Serializable> implements IBaseService<T, D, ID> {

    @Autowired
    protected BaseRepository<T, ID> repository;

    @Autowired
    private Mapper mapper;

    protected Class<T> entityClass;

    protected Class<D> dtoClass;

    public BaseServiceImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.dtoClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    @Transactional
    public D save(T t) throws DataAccessException {
        repository.save(t);
        return mapper.map(t,dtoClass) ;
    }

    @Override
    @Transactional
    public List<T> saveAll(List<T> ts) throws DataAccessException {
        List<T> result = new ArrayList<T>();

        if (ts == null) {
            return result;
        }

        for (T entity : ts) {
            try {
                result.add(repository.save(entity));
            } catch (Exception e) {
                continue;	//ignore exception for batch
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(ID id) throws DataAccessException {
        this.passive(id);
    }

    @Override
    @Transactional
    public void passive(ID id) throws DataAccessException {
        T entity = repository.findById(id).orElseThrow(null);

        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setStatus(Status.PASSIVE);
            this.save(entity);
        }
    }

    @Override
    public List<D> findAll() throws DataAccessException {
        List<D> dtoList = new ArrayList<D>();

        for (T t : repository.findAll()) {
            dtoList.add(mapper.map(t, dtoClass)) ;
        }
        return dtoList;
    }

    @Override
    public D findByID(ID id) throws DataAccessException {
        T t = repository.findById(id).orElseThrow(null);
        return mapper.map( t,dtoClass) ;
    }

}
