package com.sunshineoxygen.inhome.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sunshineoxygen.inhome.enums.RelationType;
import com.sunshineoxygen.inhome.utils.JsonDateDeserializer;
import com.sunshineoxygen.inhome.utils.JsonDateSerializer;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
public class UserFamilyDTO {

    private UUID id;

    private String firstName;

    private String lastName;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp birthday;

    private byte[] pp;

    private String relationTypeStr;

    private RelationType relationType;

    private int isUser;

    private int age;

    public int getAge() {
        Date now = new Date();
        long timeBetween = now.getTime() - this.birthday.getTime();
        double yearsBetween = timeBetween / 3.15576e+10;
        int age = (int) Math.floor(yearsBetween);
        return age;
    }

    public String getRelationTypeStr() {
        if(StringUtils.isNotEmpty(this.relationTypeStr))
            return relationTypeStr;
        else{
            if(this.relationType != null){
                return this.relationType.getName();
            }
        }
        return null;
    }

    public RelationType getRelationType() {
       if(this.relationType == null && StringUtils.isNotEmpty(relationTypeStr)){
           return RelationType.valueof(this.relationTypeStr);
       }
       return null;
    }
}
