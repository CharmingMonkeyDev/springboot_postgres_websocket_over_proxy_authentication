package com.sunshineoxygen.inhome.ui.controller;

import com.sunshineoxygen.inhome.model.BaseEntity;
import com.sunshineoxygen.inhome.service.IBaseService;
import com.sunshineoxygen.inhome.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.ConstraintViolationException;

public abstract class BaseController<ID, Entity extends BaseEntity>  {
    final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    abstract IBaseService getBaseService();

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole(\"" + Constants.ROLE_ADMIN + "\")")
    public ResponseEntity delete(@PathVariable ID id) {

        try {

            getBaseService().passive(id);

        } catch (Exception e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("true");
            }
            if (t instanceof NullPointerException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("true");
            }

        }

        return ResponseEntity.ok().body("true");
    }


}
