package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.MenuUserType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IMenuUserTypeRepository extends BaseRepository<MenuUserType, UUID> {

    @Query(value = "SELECT m.menuId FROM MenuUserType m where m.status=?2 and m.userTypeId=?1 order by m.viewIndex asc")
    List<UUID> findAllMenuByUserTypeId(UUID userTypeId, Status status);

}
