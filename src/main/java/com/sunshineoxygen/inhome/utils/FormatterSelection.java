package com.sunshineoxygen.inhome.utils;

public class FormatterSelection {
    public static String selectFormat(String type) {
        String format = null;
        if(type.equals(Formatter.PATTERN_TYPE_DATE)){
            format = "dd/MM/YYYY";
            /*if(format==null){
                format = Formatter.PATTERN_DATE;
            }*/
        }else if(type.equals(Formatter.PATTERN_TYPE_DATE_TIME)){
            format = "dd/MM/YYYY HH:ss";
            /*if(format==null){
                format = Formatter.PATTERN_DATE_TIME;
            }*/
        }
        return format;
    }

}