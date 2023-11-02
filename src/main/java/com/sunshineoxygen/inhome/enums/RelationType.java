package com.sunshineoxygen.inhome.enums;

public enum RelationType {

    FATHER(1,"Father"), MOTHER(2,"Mother"), SON(3,"Son"), DAUGHTER(4,"Daughter"), GRANDFATHER(5,"Grandfather"), GRANDMOTHER(6,"Grandmother");

    private final Integer code;
    private final String name;

    RelationType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RelationType valueof(String name){
        RelationType[] relationTypes = RelationType.class.getEnumConstants();
        for(RelationType relationType : relationTypes){
            if(relationType.name.equals(name)){
                return relationType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
