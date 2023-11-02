package com.sunshineoxygen.inhome.repository;

import com.sunshineoxygen.inhome.enums.Status;
import com.sunshineoxygen.inhome.model.OTPCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOTPCodeRepository extends BaseRepository<OTPCode, UUID> {

    @Query(value = "SELECT o FROM OTPCode o where o.status=?1 and (o.email=?2 or o.phoneNumber=?3 ) and o.userId=?4")
    Optional<OTPCode> findByStatusAndEmailOrPhoneNumberAndUserId(Status status, String email, String phoneNumber,UUID userId);

}
