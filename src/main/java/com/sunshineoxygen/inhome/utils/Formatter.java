package com.sunshineoxygen.inhome.utils;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Formatter {


    public static final String PATTERN_TYPE_DATE		= "date";
    public static final String PATTERN_TYPE_DATE_TIME	= "dateTime";
    public static final String PATTERN_TYPE_NUMBER	= "number";
    public static final String PATTERN_TYPE_MONEY	= "money";
    public static final String PATTERN_TYPE_MONEY0	= "money0";
    public static final String PATTERN_TYPE_MONEY2	= "money2";
    public static final String PATTERN_TYPE_MONEY3	= "money3";

   /* public static final String PATTERN_DATE = ConfigHolder.getApplicationConfig("formatter.pattern.date");
    public static final String PATTERN_DATE_TIME = ConfigHolder.getApplicationConfig("formatter.pattern.dateTime");
    public static final String PATTERN_NUMBER = ConfigHolder.getApplicationConfig("formatter.pattern.number");
    public static final String PATTERN_MONEY = ConfigHolder.getApplicationConfig("formatter.pattern.money");
    public static final String PATTERN_MONEY0 = ConfigHolder.getApplicationConfig("formatter.pattern.money0");
    public static final String PATTERN_MONEY2 = ConfigHolder.getApplicationConfig("formatter.pattern.money2");
    public static final String PATTERN_MONEY3= ConfigHolder.getApplicationConfig("formatter.pattern.money3");*/
    public static DecimalFormatSymbols customSymbols =   new DecimalFormatSymbols();
    static {
            customSymbols.setDecimalSeparator(',');
            customSymbols.setGroupingSeparator('.');
    }

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/YYYY HH:ss");



    public static void init() {

    }


    public static String format(Object object, String type) throws Exception{
        if (object==null) {
            return null;
        }

        if (type==null) {
            throw new Exception("Type parameter can't be null in Formatter.format method!");
        }

        if (!(type.equalsIgnoreCase(PATTERN_TYPE_DATE)
                || type.equalsIgnoreCase(PATTERN_TYPE_DATE_TIME))){
            throw new Exception("Formatter pattern '"+type+"' is not defined in config file!");
        }


        if (PATTERN_TYPE_DATE.equalsIgnoreCase(type)) {
            if (object instanceof Calendar ) {
                return new SimpleDateFormat(FormatterSelection.selectFormat(type)).format(((Calendar)object).getTime());
            } else if (object instanceof Date ) {
                return new SimpleDateFormat(FormatterSelection.selectFormat(type)).format((Date)object);
            } else {
                throw new Exception("The object can't be formatted as Date!");
            }
        }else if (PATTERN_TYPE_DATE_TIME.equalsIgnoreCase(type)) {
            if (object instanceof Calendar ) {
                return new SimpleDateFormat(FormatterSelection.selectFormat(type)).format(((Calendar)object).getTime());
            } else if (object instanceof Date ) {
                return new SimpleDateFormat(FormatterSelection.selectFormat(type)).format((Date)object);
            } else {
                throw new Exception("The object can't be formatted as Datetime!");
            }
        }else {
            return null;
        }
    }


}
