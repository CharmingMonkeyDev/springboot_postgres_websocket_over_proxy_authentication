package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IMenuRepository extends BaseRepository<Menu, UUID>{

    List<Menu> findAllByIdInAndStatus(List<UUID> ids, Status status);
}
