package com.sunshineoxygen.inhome.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "apl_user_type", schema = "core")
@Getter
@Setter
public class UserType extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

    @Size(min = 1, max = 254)
    @Column(name = "name",length = 254, unique = true)
    private String name;

    @Size(min = 1, max = 20)
    @Column(name = "short_code",length = 20, unique = true)
    private String shortCode;

}
