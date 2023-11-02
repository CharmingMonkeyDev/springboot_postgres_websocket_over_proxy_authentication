package com.sunshineoxygen.inhome.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author  EHizarci
 *
 */
public class GenericTypeValidator implements Serializable {


    /**
     *
     */
    private static final long	serialVersionUID	= 1L;

    public static Byte formatByte(String value) {
        Byte result = null;
        if(value==null || value.isEmpty()) return null;
        try {
            result = Byte.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static Short formatShort(String value) {
        Short result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            result = Short.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static Boolean formatBoolean(String value) {
        Boolean result = null;

        if (value != null && ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) ) ) {
            result = Boolean.TRUE;
        } else if (value != null && ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) ) ) {
            result = Boolean.FALSE;
        }

        return result;
    }

    public static Integer formatInt(String value) {
        Integer result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            result = Integer.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static Long formatLong(String value) {
        Long result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            result = Long.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static Float formatFloat(String value) {
        Float result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            result = Float.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static BigDecimal formatDecimal(String value, int scale) {
        BigDecimal result = null;
        if(value==null || value.isEmpty()) return null;
        try{
            result = BigDecimal.valueOf(Double.parseDouble(value));
            result = result.setScale(scale);
        }catch (Exception e){
        }
        return result;
    }

    public static Double formatDouble(String value) {
        Double result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            result = Double.valueOf(value);
        } catch (Exception e) {
        }

        return result;
    }

    public static Date formatDate(String value, Locale locale) {
        Date date = null;
        if(value==null || value.isEmpty()) return null;

        if (value != null) {
            try {
                DateFormat formatter = null;
                if (locale != null) {
                    formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                } else {
                    formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                }

                formatter.setLenient(false);

                date = formatter.parse(value);
            } catch (ParseException e) {
            }
        }

        return date;
    }

    public static Calendar formatCalendar(String value, Locale locale) {
        Date date = null;

        if (value != null) {
            try {
                DateFormat formatter = null;
                if (locale != null) {
                    formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,locale);
                } else {
                    formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                }

                formatter.setLenient(false);

                date = formatter.parse(value);
            } catch (ParseException e) {
            }
        }

        Calendar cal = null;
        if (date!=null){
            cal =Calendar.getInstance();
            cal.setTime(date);
        }
        return cal;
    }


    /**
     * <p>Checks if the field is a valid date.  The pattern is used with
     * <code>java.text.SimpleDateFormat</code>.  If strict is true, then the
     * length will be checked so '2/12/1999' will not pass validation with
     * the format 'MM/dd/yyyy' because the month isn't two digits.
     * The setLenient method is set to <code>false</code> for all.</p>
     *
     * @param 	value 		The value validation is being performed on.
     * @param 	datePattern	The pattern passed to <code>SimpleDateFormat</code>.
     * @param 	strict	        Whether or not to have an exact match of the datePattern.
     */
    public static Date formatDate(String value, String datePattern, boolean strict) {
        Date date = null;
        if(value==null || value.isEmpty()) return null;

        if (value != null && datePattern != null && datePattern.length() > 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                formatter.setLenient(false);

                date = formatter.parse(value);

                if (strict) {
                    if (datePattern.length() != value.length()) {
                        date = null;
                    }
                }
            } catch (ParseException e) {
            }
        }

        return date;
    }

    public static Calendar formatCalendar(String value, String dateTimePattern, boolean strict) {
        Date date = null;
        if(value==null || value.isEmpty()) return null;

        if (value != null && dateTimePattern != null && dateTimePattern.length() > 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern);
                formatter.setLenient(false);

                date = formatter.parse(value);

                if (strict) {
                    if (dateTimePattern.length() != value.length()) {
                        date = null;
                    }
                }
            } catch (ParseException e) {
            }
        }
        Calendar cal = null;
        if (date!=null){
            cal =Calendar.getInstance();
            cal.setTime(date);
        }
        return cal;
    }



    /**
     * <p>Checks if the field is a valid credit card number.</p>
     * <p>Translated to Java by Ted Husted (<a href="mailto:husted@apache.org">husted@apache.org</a>).<br>
     * &nbsp;&nbsp;&nbsp; Reference Sean M. Burke's script at http://www.ling.nwu.edu/~sburke/pub/luhn_lib.pl</p>
     *
     * @param 	value 		The value validation is being performed on.
     */
    public static Long formatCreditCard(String value) {
        Long result = null;
        if(value==null || value.isEmpty()) return null;

        try {
            if (GenericValidator.validateCreditCardLuhnCheck(value) && GenericValidator.validateCreditCardPrefixCheck(value)) {
                result = new Long(value);
            }
        } catch (Exception e) {
        }

        return result;
    }

}

