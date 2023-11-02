package com.sunshineoxygen.inhome.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateUtil {

    public static Date secondsAdd(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Date dayBefore(Date date, int numberOfDays) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        setTimeToBeginningOfDay(cal);
        cal.add(Calendar.DAY_OF_YEAR, -1*numberOfDays);
        return cal.getTime();
    }

    public static Date dayAdd(Date date, int numberOfDays) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        setTimeToBeginningOfDay(cal);
        cal.add(Calendar.DAY_OF_YEAR, numberOfDays);
        return cal.getTime();
    }


    public static Date weekBefore(Date date, int numberOfWeek) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -1*numberOfWeek);
        return cal.getTime();
    }

    public static Date monthBefore(Date date, int numberOfMonth ) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, -1*numberOfMonth);
        return cal.getTime();
    }

    public static Date monthAfter(Date date, int numberOfMonth ) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MONTH, numberOfMonth);
        return cal.getTime();
    }



    public static Date nextXDay(Date date, int xDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, xDay);
        //System.out.println(cal.getTime());
        if(!cal.getTime().after(date)){
            cal.add(Calendar.MONTH, 1);
        }

        return cal.getTime();
    }


    public static Date nextMonthXDay(Date date, int xDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, xDay);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    public static Integer dayDifference(Date dateFrom, Date dateTo) {
        return Days.daysBetween(new DateTime(dateFrom), new DateTime(dateTo)).getDays();
    }

    public static Date yearBefore(Date date, int numberOfYear) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.YEAR, -1*numberOfYear);
        return cal.getTime();
    }

    public static Date beginningOfYear() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date yearAfter(Date date, int numberOfYear) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.YEAR, numberOfYear);
        return cal.getTime();
    }


    public static Date getEndOfMonth(Integer month, Integer year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }


    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

}
