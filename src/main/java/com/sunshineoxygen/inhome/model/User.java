package com.sunshineoxygen.inhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 * A user.
 */
@Entity
@Table(name = "apl_user", schema = "core")
@Getter
@Setter
public class User  extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    @Email
    @Size(min = 5, max = 254)
    @Column(name = "email",length = 254, unique = true)
    private String email;

    @Size(min = 2, max = 3)
    @Column(name = "prefix_phone_number",length = 254)
    private String prefixPhoneNumber;

    @Size(min = 5, max = 10)
    @Column(name = "phone_number",length = 254)
    private String phoneNumber;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @JsonIgnore
    @Column(name="ext_id")
    private Integer extId;

    @JsonIgnore
    @Column(name = "is_allowed_sms")
    private Boolean isAllowedSms;

}
