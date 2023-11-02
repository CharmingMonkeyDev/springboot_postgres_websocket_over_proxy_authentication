package com.sunshineoxygen.inhome.model;

import com.sunshineoxygen.inhome.utils.*;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * @uml.property  name="properties"
     */
    private Map<String,Object> properties = null;

    /** Creates a new instance of DynamicBean */
    public DynamicBean(Map<String,Object> properties) {
        this.properties = properties;
    }

    public DynamicBean() {
        properties = new HashMap<String,Object>();
    }

    public Boolean isEmpty() {
        if (properties==null || properties.isEmpty()) {
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public Boolean isPropertyEmpty(String key) {
        return get(key)==null;
    }


    public Boolean isAllNull() {
        boolean returnVal = true;
        if (properties==null || properties.isEmpty()) {
            return Boolean.TRUE;
        }else {
            Iterator<Object> it = properties.values().iterator();
            while (it.hasNext() ) {
                Object key = it.next();
                if (key!=null) returnVal = false;
            }
        }

        return returnVal;
    }

    public DynamicBean(Object key, Object value) {
        properties = new HashMap<String,Object>();
        addProperty(key, value);

    }


    public void addProperty(Object key, Object value) {
        if (properties != null ) {
            properties.put(key.toString(), value);
        }
    }

    public void putProperties(Map<String,Object> properties ) {
        if (properties != null ) {

            Iterator<String> it = properties.keySet().iterator();
            while (it.hasNext() ) {
                Object key = it.next();
                this.properties.put(key.toString(), properties.get(key) );
            }
            //this.properties.putAll(properties );
        }
    }


    public void putProperties(DynamicBean dynamicBean ) {
        if (dynamicBean != null ) {
            Map<String,Object> prop = dynamicBean.getProperties();
            Iterator<String> it = prop.keySet().iterator();
            while (it.hasNext() ) {
                Object key = it.next();
                this.properties.put(key.toString(), prop.get(key) );
            }
            //this.properties.putAll(dynamicBean.getProperties() );
        }
    }

    /**
     * @return
     * @uml.property  name="properties"
     */
    public Map<String,Object> getProperties(){
        return properties;
    }


    public String get(String propertyName){
        return getPropertyAsString(propertyName);
    }

    public Object getProperty(String propertyName){
        return properties.get(propertyName);
    }

    public String getPropertyAsStringIgnorePrefix(String propertyName) {
        if (properties==null || properties.isEmpty() || propertyName==null) {
            return null;
        }else if (getPropertyAsString(propertyName)!=null ) {
            return getPropertyAsString(propertyName);
        }

        for (Iterator<Map.Entry<String,Object>> it = properties.entrySet().iterator();it.hasNext();) {
            Map.Entry<String,Object> entry = it.next();
            String key = entry.getKey();
            if (key.lastIndexOf(propertyName)>0) {
                if (key.lastIndexOf(propertyName)+propertyName.length() == key.length()) {
                    return getPropertyAsString(key);
                }
            }
        }

        return null;

    }

    public Object getPropertyAsObject(String propertyName){
        Object object = getProperty(propertyName);
        if (object == null) {
            return null;
        }
        if (object.getClass().getComponentType()!=null) {
            Object objectArray[] = (Object[]) object;
            if (objectArray.length==1) {
                return objectArray[0];
            }else {
                return null;
            }
        }else {
            return object;
        }
    }

    public String getPropertyAsString(String propertyName){
        Object object = getProperty(propertyName);
        if (object == null) {
            return null;
        }
        if (object.getClass().getComponentType()!=null) {
            Object objectArray[] = (Object[]) object;
            if (objectArray.length==1) {
                return objectArray[0].toString();
            }else {
                return null;
            }
        }else {
            return object.toString();
        }
    }

    public String getPropertyAsMD5String(String propertyName){
        String propVal = getPropertyAsString(propertyName);
        if(propVal==null || propVal.isEmpty()) return null;
        else {
            try{
                return Security.digout(propVal);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }


    public Boolean getPropertyAsBoolean(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            return null;
        }else if (value instanceof java.lang.Boolean){
            return (Boolean) value;
        }else if (value instanceof java.lang.String ) {
            return Boolean.valueOf("YES".equalsIgnoreCase(value.toString()) || "1".equals(value.toString()));
        }else {
            String strValue = getPropertyAsString(propertyName);
            if (strValue!=null) {
                return Boolean.valueOf("YES".equalsIgnoreCase(strValue.toString()) || "1".equals(value.toString()) );
            }else{
                return null;
            }
        }
    }

    public Boolean getPropertyAsBoolean(String propertyName, boolean isNullFalse) {
        Boolean returnVal = getPropertyAsBoolean(propertyName);
        if(returnVal==null) return Boolean.FALSE;
        else return returnVal;
    }



    public Byte getPropertyAsByte(String propertyName){
        return GenericTypeValidator.formatByte(getPropertyAsString(propertyName));
    }

    public Short getPropertyAsShort(String propertyName){
        return GenericTypeValidator.formatShort(getPropertyAsString(propertyName));
    }

    public Integer getPropertyAsInteger(String propertyName){
        return GenericTypeValidator.formatInt(getPropertyAsString(propertyName));
    }

    public Long getPropertyAsLong(String propertyName){
        return GenericTypeValidator.formatLong(getPropertyAsString(propertyName));
    }

    public Float getPropertyAsFloat(String propertyName) {
        return GenericTypeValidator.formatFloat(getPropertyAsString(propertyName));
    }

    public Double getPropertyAsDouble(String propertyName) {
        return GenericTypeValidator.formatDouble(getPropertyAsString(propertyName));
    }

    public Calendar getPropertyAsCalendar(String propertyName, Locale locale) {
        return GenericTypeValidator.formatCalendar(getPropertyAsString(propertyName), locale);
    }

    public Calendar getPropertyAsCalendar(String propertyName, String datePattern, boolean strict) {
        return GenericTypeValidator.formatCalendar(getPropertyAsString(propertyName), datePattern, strict);
    }


    public Date getPropertyAsDate(String propertyName, Locale locale) {
        return GenericTypeValidator.formatDate(getPropertyAsString(propertyName), locale);
    }

    public Date getPropertyAsDate(String propertyName, String datePattern, boolean strict) {
        return GenericTypeValidator.formatDate(getPropertyAsString(propertyName), datePattern, strict);
    }

    public Date getPropertyAsDate(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            return null;
        }
        if (value instanceof Date ) {
            return (Date) value;
        }else if (value instanceof Calendar ) {
            return ((Calendar) value).getTime();
        }else if (value instanceof GregorianCalendar ) {
            return ((GregorianCalendar) value).getTime();

        }
        else {
            return null;
        }
    }
    public Calendar getPropertyAsCalendar(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            return null;
        }
        if (value instanceof java.util.Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date)value);
            return cal;
        }else if (value instanceof java.util.Calendar){
            return (Calendar)value;
        }else if (value instanceof java.sql.Timestamp){
            java.sql.Timestamp timeStamp = (java.sql.Timestamp) value;
            Date dat = new Date(timeStamp.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(dat);
            return cal;

        }else {
            return null;
        }
    }

    public Object[] getPropertyAsArray(String propertyName){
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }

        if (value.getClass().getComponentType()!=null){
            return (Object[]) value;
        }else if (value.getClass().getName().indexOf("java.")==0) {
            return new Object[]{value};
        }else{
            return null;
        }
    }

    public String[] getPropertyAsStringArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.String;".equalsIgnoreCase(value.getClass().getName())) {
            return (String[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            String returnArray[] = new String[object.length];
            for(int i=0;i<returnArray.length;i++){
                returnArray[i] = object[i].toString();
            }
            return returnArray;
        }else if (value.getClass().getName().indexOf("java.")==0) {
            return new String[]{value.toString()};
        }else {
            return null;
        }

    }

    public String getPropertyAsCommaSeparatorString(String propertyName) {
        String[] asArray = getPropertyAsStringArray(propertyName);
        if(asArray==null) return null;
        String returnVal = "";
        for(String str:asArray){
            returnVal+=","+str;
        }
        if(returnVal.length()>1) returnVal= returnVal.substring(1);
        return returnVal;
    }

    public Integer[] getPropertyAsIntegerArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Integer;".equalsIgnoreCase(value.getClass().getName())) {
            return (Integer[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Integer returnArray[] = new Integer[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = Integer.valueOf(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Integer returnArray[] = new Integer[1];
            try {
                returnArray[0] = Integer.valueOf(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }

    public Short[] getPropertyAsShortArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Short;".equalsIgnoreCase(value.getClass().getName())) {
            return (Short[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Short returnArray[] = new Short[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = new Short(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Short returnArray[] = new Short[1];
            try {
                returnArray[0] = new Short(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }
    public Byte[] getPropertyAsByteArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Byte;".equalsIgnoreCase(value.getClass().getName())) {
            return (Byte[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Byte returnArray[] = new Byte[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = new Byte(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Byte returnArray[] = new Byte[1];
            try {
                returnArray[0] = new Byte(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }

    public Float[] getPropertyAsFloatArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Float;".equalsIgnoreCase(value.getClass().getName())) {
            return (Float[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Float returnArray[] = new Float[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = new Float(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Float returnArray[] = new Float[1];
            try {
                returnArray[0] = new Float(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }

    public Double[] getPropertyAsDoubleArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Double;".equalsIgnoreCase(value.getClass().getName())) {
            return (Double[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Double returnArray[] = new Double[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = new Double(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Double returnArray[] = new Double[1];
            try {
                returnArray[0] = new Double(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }

    public Long[] getPropertyAsLongArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.lang.Long;".equalsIgnoreCase(value.getClass().getName())) {
            return (Long[]) value;
        }else if (value.getClass().getComponentType()!=null){
            Object object[] = (Object[]) value;
            Long returnArray[] = new Long[object.length];
            for(int i=0;i<returnArray.length;i++){
                try {
                    returnArray[i] = new Long(object[i].toString());
                } catch (Exception e) {
                    return null;
                }
            }
            return returnArray;
        }else if(value.getClass().getName().indexOf("java.")==0) {
            Long returnArray[] = new Long[1];
            try {
                returnArray[0] = new Long(value.toString());
            } catch (Exception e) {
                return null;
            }
            return returnArray;

        }else{
            return null;
        }

    }


    public Date[] getPropertyAsDateArray(String propertyName) {
        Object value = getProperty(propertyName);
        if (value==null) {
            try{
                value = properties.get(propertyName);
            }catch(ClassCastException cce) {
                return null;
            }
        }
        if (value==null) {
            return null;
        }
        if ("[Ljava.util.Date;".equalsIgnoreCase(value.getClass().getName())) {
            return (Date[]) value;
        }else if ("java.util.Date".equalsIgnoreCase(value.getClass().getName())) {
            return new Date[]{(Date)value};
        }else {
            return null;
        }

    }

    public Date[] getPropertyAsDateArray(String propertyName, Locale locale) {
        String values[] = getPropertyAsStringArray(propertyName);
        Date returnValue[] = null;
        if (values!=null) {
            returnValue = new Date[values.length];
            for (int i=0;i<values.length;i++) {
                returnValue[i] = GenericTypeValidator.formatDate(values[i], locale);
            }
        }
        return returnValue;
    }

    public Date[] getPropertyAsDateArray(String propertyName, String datePattern, boolean strict) {
        String values[] = getPropertyAsStringArray(propertyName);
        Date returnValue[] = null;
        if (values!=null) {
            returnValue = new Date[values.length];
            for (int i=0;i<values.length;i++) {
                returnValue[i] = GenericTypeValidator.formatDate(values[i], datePattern, strict);
            }
        }
        return returnValue;
    }


    public java.util.List<String> getPropertyAsList(String propertyName) {
        String values[] = getPropertyAsStringArray(propertyName);
        java.util.List<String> returnList = new java.util.ArrayList<String>();
        if (values!=null && values.length>0) {
            for (int i=0;i<values.length;i++) {
                returnList.add(values[i]);
            }
        }

        return returnList;
    }
    public void dropProperty(String propertyName ) {
        properties.remove(propertyName);
    }

    public String getPropertiesAsQueryString() throws Exception{
        StringBuffer buffer = new StringBuffer();
        if (properties!=null && properties.size()>0) {
            for(Iterator<Map.Entry<String,Object>> it = properties.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String,Object> entry = it.next();
                String paramName = entry.getKey();

                String vals [] = getPropertyAsStringArray(paramName);
                if (vals!=null) {
                    for(int i=0;i<vals.length;i++) {
                        buffer.append(paramName+"="+java.net.URLEncoder.encode(vals[i],"UTF-8")+"&");

                    }
                }
            }
        }

        return buffer.toString();

    }

    public String getPropertyAsQueryString(String propertyName) throws Exception{
        String property = getPropertyAsString(propertyName);
        return property==null?null:URLEncoder.encode(property,"UTF-8");

    }

    public String getPropertyAsStringIgnoreNull(String propertyName) {
        String property = getPropertyAsString(propertyName);
        return property==null?"":property;

    }

    public String getPropertyAsHtmlSafeString(String propertyName) {
        return HtmlUtil.safeString(getPropertyAsStringIgnoreNull(propertyName));
    }

    public List<String> getKeys() {
        List<String> returnList = new ArrayList<String>();
        if (properties!=null && properties.size()>0) {
            for(Iterator<Map.Entry<String,Object>> it = properties.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String,Object> entry = it.next();
                returnList.add(entry.getKey());
            }
        }
        return returnList;
    }

    public String getAsXMLTagsEx() {
        if (properties==null || properties.isEmpty()) return "";
        StringBuffer buffer=new StringBuffer();
        for(Iterator<Map.Entry<String,Object>> it=properties.entrySet().iterator();it.hasNext();){
            Map.Entry<String,Object> entry = it.next();
            buffer.append("<"+entry.getKey().toString()+">");
            if(entry!=null && entry.getValue()!=null && entry.getValue().toString().indexOf('&')<0) buffer.append(entry.getValue().toString());
            else if(entry!=null && entry.getValue()!=null && entry.getValue().toString().indexOf('&')>0) {
                buffer.append("<![CDATA[").append(entry.getValue().toString()).append("]]>");
            }
            buffer.append("</"+entry.getKey().toString()+">");

        }
        return buffer.toString();
    }

    public String getAsXMLTags() {
        if (properties==null || properties.isEmpty()) return "";
        StringBuffer buffer=new StringBuffer();
        for(Iterator<Map.Entry<String,Object>> it=properties.entrySet().iterator();it.hasNext();){
            Map.Entry<String,Object> entry = it.next();
            buffer.append("<"+entry.getKey().toString().toUpperCase()+">");
            if(entry!=null && entry.getValue()!=null && entry.getValue().toString().indexOf('&')<0) buffer.append(entry.getValue().toString());
            else if(entry!=null && entry.getValue()!=null && entry.getValue().toString().indexOf('&')>0) {
                buffer.append("<![CDATA[").append(entry.getValue().toString()).append("]]>");
            }
            buffer.append("</"+entry.getKey().toString().toUpperCase()+">");

        }
        return buffer.toString();
    }

    public String getPropertyAsDeAccentString(String propertyName) {
        return BasicData.deAccent(getPropertyAsString(propertyName));
    }

    public static List<Integer> getDynamicBeanListAsIntegerList(List<DynamicBean> dynamicBeanList, String propertyName) {
        List<Integer> returnList = new ArrayList<Integer>();
        if (dynamicBeanList==null || dynamicBeanList.isEmpty())
            return returnList;
        else {
            for(DynamicBean bean:dynamicBeanList){
                returnList.add(bean.getPropertyAsInteger(propertyName));
            }
        }
        return returnList;


    }

    public static List<String> getDynamicBeanListAsStringList(List<DynamicBean> dynamicBeanList, String propertyName) {
        List<String> returnList = new ArrayList<String>();
        if (dynamicBeanList==null || dynamicBeanList.isEmpty())
            return returnList;
        else {
            for(DynamicBean bean:dynamicBeanList){
                returnList.add(bean.getPropertyAsString(propertyName));
            }
        }
        return returnList;


    }

    public static List<String> getDynamicBeanListAsDistinctStringList(List<DynamicBean> dynamicBeanList, String propertyName) {
        List<String> returnList = new ArrayList<String>();
        if (dynamicBeanList==null || dynamicBeanList.isEmpty())
            return returnList;
        else {
            for(DynamicBean bean:dynamicBeanList){
                if(bean.getPropertyAsString(propertyName)!=null && !returnList.contains(bean.getPropertyAsString(propertyName))){
                    returnList.add(bean.getPropertyAsString(propertyName));
                }
            }
        }
        return returnList;


    }



    public static String getDynamicBeanAsJSSeries(List<DynamicBean> dynamicBeanList, String propertyName, String type, DecimalFormat formatter) {
        StringBuilder builder = new StringBuilder();

        if (dynamicBeanList==null || dynamicBeanList.isEmpty()) {
            return "";
        }else {
            for(DynamicBean bean:dynamicBeanList){
                if ("string".equalsIgnoreCase(type)) {
                    builder.append("'"+bean.getPropertyAsString(propertyName)+"',");
                }else if (formatter!=null) {
                    builder.append(""+formatter.format(bean.getPropertyAsFloat(propertyName))+",");
                }

            }
            builder.deleteCharAt(builder.length()-1);
        }


        return builder.toString();
    }

    public static String getDynamicBeanAsJSSeriesByMasterList(List<String> masterList, String masterFieldName, List<DynamicBean> dynamicBeanList, String propertyName, String type, DecimalFormat formatter) {
        StringBuilder builder = new StringBuilder();

        if (dynamicBeanList==null || dynamicBeanList.isEmpty()) {
            return "";
        }else {
            for(String str:masterList){
                boolean isAppended = false;
                String timeStr = changeStrToJSDate(str);
                builder.append("[").append(timeStr).append(",");
                for(DynamicBean bean:dynamicBeanList){
                    if(str.equals(bean.get(masterFieldName))){
                        isAppended = true;
                        if ("string".equalsIgnoreCase(type)) {
                            builder.append("'"+bean.getPropertyAsString(propertyName)+"'");
                        }else if (formatter!=null) {
                            builder.append(""+formatter.format(bean.getPropertyAsFloat(propertyName))+"");
                        }
                    }
                }
                if (!isAppended){
                    if ("string".equalsIgnoreCase(type)) {
                        builder.append("''");
                    }else if (formatter!=null) {
                        builder.append("0");
                    }
                }

                builder.append("],");
            }
            builder.deleteCharAt(builder.length()-1);
        }


        return builder.toString();
    }



    public static String getStringListAsJSSeries(List<String> stringList) {
        StringBuilder builder = new StringBuilder();

        if (stringList==null || stringList.isEmpty()) {
            return "";
        }else {
            for(String str:stringList){
                builder.append("'"+str+"',");
            }
            builder.deleteCharAt(builder.length()-1);
        }


        return builder.toString();
    }

    public static String getStringListAsJSDateSeries(List<String> stringList) {
        StringBuilder builder = new StringBuilder();

        if (stringList==null || stringList.isEmpty()) {
            return "";
        }else {
            for(String str:stringList){
                String jsStr = changeStrToJSDate(str);
                builder.append((jsStr==null?"":jsStr)).append(",");
            }
            if(builder.length()>0) builder.deleteCharAt(builder.length()-1);
        }


        return builder.toString();
    }

    public static String changeStrToJSDate(String str) {
        StringBuilder builder = new StringBuilder();
        if(str.indexOf(".")>0){
            String parts[] = str.split("\\.");
            if(parts==null || parts.length==0) return null;
            if(parts.length>=3){
                builder.append("Date.UTC("+parts[0]+","+(Integer.valueOf(parts[1])-1)+","+parts[2]+")");
            }else if(parts.length>=2){
                builder.append("Date.UTC("+parts[0]+","+(Integer.valueOf(parts[1])-1)+",1)");
            }else if(parts.length>=1){
                builder.append("Date.UTC("+parts[0]+",0,1)");
            }
        }else{
            builder.append("'"+str+"'");
        }
        return builder.toString();
    }

    public static Integer getColumnTotalAsInteger(List<DynamicBean> dynamicBeanList, String propertyName) {
        Integer returnVal = 0;

        if(dynamicBeanList!=null && !dynamicBeanList.isEmpty()){
            for(DynamicBean bean:dynamicBeanList){
                Integer val = bean.getPropertyAsInteger(propertyName);
                returnVal += (val==null?0:val.intValue());
            }
        }

        return returnVal;

    }

    public static Float getColumnTotalAsFloat(List<DynamicBean> dynamicBeanList, String propertyName) {
        Float returnVal = 0f;

        if(dynamicBeanList!=null && !dynamicBeanList.isEmpty()){
            for(DynamicBean bean:dynamicBeanList){
                Float val = bean.getPropertyAsFloat(propertyName);
                returnVal += (val==null?0f:val.floatValue());
            }
        }

        return returnVal;

    }

    public static String getDynamicBeanAsJSSeriesByFilter(List<DynamicBean> dynamicBeanList,String filterPropertyName, String filterPropertyValue, String propertyName, String type, DecimalFormat formatter) {
        StringBuilder builder = new StringBuilder();

        if (dynamicBeanList==null || dynamicBeanList.isEmpty()) {
            return "";
        }else {
            for(DynamicBean bean:dynamicBeanList){
                if (filterPropertyValue.equals(bean.getPropertyAsString(filterPropertyName))) {
                    if ("string".equalsIgnoreCase(type)) {
                        builder.append("'"+bean.getPropertyAsString(propertyName)+"',");
                    }else if (formatter!=null) {
                        builder.append(""+formatter.format(bean.getPropertyAsFloat(propertyName))+",");
                    }
                }
            }
            builder.deleteCharAt(builder.length()-1);
        }


        return builder.toString();
    }

    public static String getPropertyByOtherProperty(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue, String targetProperty) {
        String returnVal = null;
        if (list==null || list.isEmpty()) return returnVal;
        for(DynamicBean bean:list) {
            if (bean.getPropertyAsString(searchedPropertyName)!=null && searchedPropertyValue.equals(bean.getPropertyAsString(searchedPropertyName).trim())) {
                returnVal = bean.getPropertyAsString(targetProperty);
            }
        }

        return returnVal;

    }

    public static DynamicBean getBeanByPropertyValueFromList(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue) {
        if (list==null || list.isEmpty()) return null;
        for(DynamicBean bean:list) {
            if (bean.getPropertyAsString(searchedPropertyName)!=null && searchedPropertyValue.equals(bean.getPropertyAsString(searchedPropertyName).trim())) {
                return bean;
                //returnVal =bean.getPropertyAsString(targetProperty);
            }
        }
        return null;
        //return returnVal;

    }

    public static Integer getBeanIndexByPropertyValueFromList(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue) {
        if (list==null || list.isEmpty()) return null;
        int index = -1;
        for(DynamicBean bean:list) {
            index++;
            if (bean.getPropertyAsString(searchedPropertyName)!=null && searchedPropertyValue.equals(bean.getPropertyAsString(searchedPropertyName).trim())) {
                return index;
                //returnVal =bean.getPropertyAsString(targetProperty);
            }
        }
        return null;
        //return returnVal;

    }

    public static Integer countLinesByValue(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue) {
        if (list==null || list.isEmpty()) return 0;
        int count = 0;
        for(DynamicBean bean:list) {
            if (bean.getPropertyAsString(searchedPropertyName)!=null && searchedPropertyValue.equals(bean.getPropertyAsString(searchedPropertyName).trim())) {
                count++;
            }
        }
        return count;
    }

    public static List<DynamicBean> getSubListByPropertyValueFromList(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue) {
        if (list==null || list.isEmpty()) return null;
        List<DynamicBean> returnList = new ArrayList<DynamicBean>();
        for(DynamicBean bean:list) {
            String valueOnBean = bean.getPropertyAsString(searchedPropertyName);
            if (valueOnBean!=null && searchedPropertyValue.equals(valueOnBean.trim())) {
                returnList.add(bean);
                //returnVal =bean.getPropertyAsString(targetProperty);
            }
        }
        return returnList;
        //return returnVal;

    }

    public static List<DynamicBean> removeListByPropertyValueFromList(List<DynamicBean> list, String searchedPropertyName, String searchedPropertyValue) {
        if (list==null || list.isEmpty()) return null;
        ArrayList<DynamicBean> returnList = new ArrayList<DynamicBean>();
        for(DynamicBean bean:list) {
            if ((searchedPropertyValue==null && bean.getPropertyAsString(searchedPropertyName)!=null)
                    || (searchedPropertyValue!=null && !searchedPropertyValue.equals(bean.getPropertyAsString(searchedPropertyName).trim()))) {
                returnList.add(bean);
                //returnVal =bean.getPropertyAsString(targetProperty);
            }
        }
        return returnList;
        //return returnVal;

    }

    public static List<DynamicBean> removeListByPropertyValuesFromList(List<DynamicBean> list, String searchedPropertyName, List<String> searchedPropertyValues) {
        if (list==null || list.isEmpty()) return null;
        ArrayList<DynamicBean> returnList = new ArrayList<DynamicBean>();
        for(DynamicBean bean:list) {
            if ((searchedPropertyValues==null && bean.getPropertyAsString(searchedPropertyName)!=null)
                    || (searchedPropertyValues==null || searchedPropertyValues.isEmpty())
                    || (searchedPropertyValues!=null && !searchedPropertyValues.isEmpty() && !searchedPropertyValues.contains(bean.getPropertyAsStringIgnoreNull(searchedPropertyName).trim()))) {
                returnList.add(bean);
                //returnVal =bean.getPropertyAsString(targetProperty);
            }
        }
        return returnList;
        //return returnVal;

    }





    public static String replaceTokensInStr(DynamicBean bean, String str) {
        //String str = "a $(a) $(b)";
        Pattern pat = Pattern.compile("\\$\\([^\\)]{1,}\\)");
        Matcher m = pat.matcher(str);
        String returnStr = str;
        while(m.find()) {
            String token = m.group(0);
            String propertyName = token.substring(token.indexOf('(')+1, token.indexOf(')'));
            returnStr = returnStr.replace(token, bean.getPropertyAsString(propertyName));

        }
        return returnStr;

    }

    public static DynamicBean addPrefix(DynamicBean bean, String prefix) {
        Map<String,Object> map = new HashMap<String,Object>();
        for(Iterator<Map.Entry<String,Object>> it=bean.getProperties().entrySet().iterator();it.hasNext();){
            Map.Entry<String,Object> entry = it.next();
            String key = entry.getKey();
            map.put(prefix.concat(".").concat(key), entry.getValue());
        }

        return new DynamicBean(map);
    }

    public static DynamicBean removePrefix(DynamicBean bean, String prefix) {
        Map<String,Object> map = new HashMap<String,Object>();
        for(Iterator<Map.Entry<String,Object>> it=bean.getProperties().entrySet().iterator();it.hasNext();){
            Map.Entry<String,Object> entry = it.next();
            String key = entry.getKey();
            if (key.indexOf(prefix.concat("."))==0) {
                key = key.replace(prefix.concat("."), "");
            }
            map.put(key, entry.getValue());
        }

        return new DynamicBean(map);
    }


    public static List<DynamicBean> addPrefix(List<DynamicBean> beanList, String prefix) {
        List<DynamicBean> returnList = new ArrayList<DynamicBean>();
        for(DynamicBean bean:beanList){
            returnList.add(addPrefix(bean, prefix));
        }
        return returnList;

    }

    public static StringBuffer getDynamicBeanListAsCSV(List<DynamicBean> beanList, List<String> columns, List<String> columnTypes, char delimeter) throws Exception {
        StringBuffer buffer = new StringBuffer();
        for(String column:columns) {
            if(column.indexOf(delimeter)>=0){
                column = column.replace(delimeter, ' ');
            }
            buffer.append(column).append(delimeter);
        }

        buffer.deleteCharAt(buffer.length()-1);
        buffer.append("\r\n");

        for(DynamicBean bean:beanList){
            for(int i=0;i<columns.size();i++){
                String columnName = columns.get(i);
                String columnType = columnTypes.get(i);
                String columnValue = bean.get(columnName);
                if(columnValue!=null && !columnValue.isEmpty() && columnValue.indexOf(delimeter)>=0){
                    columnValue = columnValue.replace(delimeter, ' ');
                }
                if(bean.get(columnName)==null){
                    //buffern.append()
                }else{
                    if("float".equals(columnType)){
                        buffer.append(Formatter.format(bean.getPropertyAsFloat(columnName), Formatter.PATTERN_TYPE_MONEY ));
                    }else{
                        buffer.append(columnValue);
                    }
                }
                buffer.append(delimeter);
            }
            buffer.deleteCharAt(buffer.length()-1);
            buffer.append("\r\n");

        }

        return buffer;

    }

    public static StringBuffer getDynamicBeanListAsXML(List<DynamicBean> beanList, List<String> columns, List<String> columnTypes, String rootTag, String childTag) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<").append(rootTag).append(">").append("\r\n");

        for(DynamicBean bean:beanList){
            buffer.append("\t").append("<").append(childTag).append(">").append("\r\n");

            for(int i=0;i<columns.size();i++){
                String columnName = columns.get(i);
                String columnType = columnTypes.get(i);
                buffer.append("\t\t").append("<").append(columnName).append(">");

                if(bean.get(columnName)==null){
                    //buffern.append()
                }else{
                    if("float".equals(columnType)){
                        buffer.append(Formatter.format(bean.getPropertyAsFloat(columnName), Formatter.PATTERN_TYPE_MONEY ));
                    }else{
                        buffer.append(TagUtils.replaceSpecialCharacters(bean.get(columnName)));
                    }
                }
                buffer.append("</").append(columnName).append(">").append("\r\n");

            }
            buffer.append("\t").append("</").append(childTag).append(">").append("\r\n");
        }

        buffer.append("</").append(rootTag).append(">");

        return buffer;

    }

    public static void changePropertyName(DynamicBean bean, String oldName, String newName) throws Exception {
        if(bean==null || bean.isEmpty()) return;
        Object obj = bean.getProperty(oldName);
        if(obj!=null){
            bean.addProperty(newName, obj);
            bean.getProperties().remove(oldName);
        }
    }

    public static List<String> comparePropertyValuesByPropertyNames(DynamicBean bean1, DynamicBean bean2, List<String> propertyNames) throws Exception {
        if(bean1==null || bean1.isEmpty() || bean2==null || bean2.isEmpty() || propertyNames==null || propertyNames.size()<1) return null;

        List<String> differentProperties = new ArrayList<String>();
        for(String propertyName:propertyNames) {
            if(propertyName!=null && !bean1.getPropertyAsStringIgnoreNull(propertyName).equals(bean2.getPropertyAsStringIgnoreNull(propertyName))) {
                differentProperties.add((String)propertyName);
            }
        }
        return differentProperties;
    }

    public static String concatValues(List<DynamicBean> list, String propertyName, String delimeter){
        return concatValues(list, propertyName, delimeter, 0);
    }

    public static String concatValues(List<DynamicBean> list, String propertyName, String delimeter, int beginIndex ) {
        if(list==null || list.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for(int i=beginIndex;i<list.size();i++){
            DynamicBean bean = list.get(i);
            sb.append(bean.getPropertyAsStringIgnoreNull(propertyName));
            if(i<(list.size()-1)) sb.append(delimeter);
        }

        return sb.toString();

    }

    public void dropAllNull(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.putAll(properties);
        for(Iterator<Map.Entry<String,Object>> it=map.entrySet().iterator();it.hasNext();){
            Map.Entry<String,Object> entry = it.next();
            if(entry.getValue()==null) {
                properties.remove(entry.getKey());
            }
        }
    }

    public static List<DynamicBean> sortDynamicBeanListByProperty(List<DynamicBean> list, String propertyName){
        if(propertyName==null|| propertyName.isEmpty()){
            return list;
        }
        if(list.size()>1){
            for(int j=0;j<list.size()-1;j++){
                for(int i=0;i<list.size()-1;i++){
                    if(list.get(i).get(propertyName).compareTo(list.get(i+1).get(propertyName))>0){
                        DynamicBean tempBean=list.get(i);
                        list.set(i, list.get(i+1));
                        list.set(i+1,tempBean);
                    }
                }
            }
        }
        return list;
    }

    public static void lowerCasePropertyName(DynamicBean bean) throws Exception{
        for(String key:bean.getKeys()){
            String newKey=key.toLowerCase();
            if(key.equals(newKey)) continue;
            changePropertyName(bean, key, newKey);
        }
    }

    @Override
    public String toString() {
        if (properties==null || properties.isEmpty()) return null;
        StringBuffer sb = new StringBuffer();
        properties.keySet().stream().sorted().forEach(key ->{
            String val = this.getPropertyAsStringIgnoreNull(key);
            sb.append(key).append(":").append(val).append(",\r\n");
        });
        return sb.toString();
    }

    public String toStringAsHtmlTable(List<String> columns) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table><tbody>");
        columns.forEach(line -> {
            sb.append("<tr><td style='margin:10px'><b>").append(line).append("</b></td><td style='margin:10px'>").append(this.getPropertyAsStringIgnoreNull(line)).append("</td></tr>");
        });
        sb.append("</tbody></table>");

        return sb.toString();

    }

    public static boolean hasLineWith(List<DynamicBean> list, String fieldName, String fieldValue) {
        boolean returnVal = false;
        if(list==null || list.isEmpty()) return returnVal;
        DynamicBean line = DynamicBean.getBeanByPropertyValueFromList(list, fieldName, fieldValue);
        if(line!=null) returnVal = true;
        return returnVal;
    }

    public static List<DynamicBean> cloneList(List<DynamicBean> list) {
        if(list==null || list.isEmpty()) return list;
        return new ArrayList<DynamicBean>(list);
    }


    public DynamicBean clone() {
        DynamicBean cloneBean = new DynamicBean();
        cloneBean.putProperties(this.getProperties());
        return cloneBean;
    }


}
