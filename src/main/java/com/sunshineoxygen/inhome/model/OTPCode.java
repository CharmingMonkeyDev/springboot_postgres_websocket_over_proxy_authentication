package com.sunshineoxygen.inhome.model;

import com.sunshineoxygen.inhome.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "otp_code", schema = "core")
@Getter
@Setter
@Data
public class OTPCode extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    private String code;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "expired_date")
    private Date expiredDate;


}
