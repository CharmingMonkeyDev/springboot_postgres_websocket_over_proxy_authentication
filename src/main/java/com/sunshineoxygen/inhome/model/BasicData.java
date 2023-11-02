package com.sunshineoxygen.inhome.model;

import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/** Base class for all data objects.
 * @author alk
 * @version 1.0
 */
public abstract class BasicData implements IData{
    /**
     * Creates new basicData
     */
    public static Pattern deaccentpattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");


    public BasicData() {
    }

    public static void main(String args[]) throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String substituteWrongChars(String source) {
        if(source==null) {
            return "";
        }

        return source.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;");
    }

    /**
     * DOCUMENT ME!
     *
     * @param price DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String formatPrice(Float price) {
        String priceStr = new java.text.DecimalFormat("#,###.00").format(Float.parseFloat(price.toString()));

        if(priceStr.indexOf(",00")>=0) {
            priceStr = priceStr.substring(0, priceStr.indexOf(",00"));
            priceStr += ",-";
        }

        return priceStr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param param DOCUMENT ME!
     * @param separator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Calendar convertStringToCalendar(String param,
                                            String separator) {
        try {
            String calendarParts[] = param.split((separator.equals(".") ? "\\." : separator));
            int    day = Integer.parseInt(calendarParts[0]);

            if((day<1) || (day>31)) {
                return null;
            }

            int month = Integer.parseInt(calendarParts[1]);

            if((month<1) || (month>12)) {
                return null;
            }

            String yearAsString = calendarParts[2];

            if(yearAsString.length()!=4) {
                return null;
            }

            int year = Integer.parseInt(yearAsString);

            if(year<1900) {
                return null;
            }

            SimpleDateFormat  sdformat = new SimpleDateFormat("d"+separator+"M"+separator+"yyyy");
            GregorianCalendar tempCalender = new GregorianCalendar(year, month-1, day);
            String            formatedDate = sdformat.format(tempCalender.getTime());

            if(!formatedDate.equals(day+separator+month+separator+year)) {
                return null;
            }

            return new GregorianCalendar(year, month-1, day);
        } catch(Exception e) {
            // e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param param DOCUMENT ME!
     * @param format DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String convertCalendarToString(Calendar param,
                                                 String   format) {
        if(param==null) {
            return null;
        }

        SimpleDateFormat sdformat = new SimpleDateFormat(format);

        return sdformat.format(param.getTime());
    }

	/*public Map addPrefixToMap(final Map withOutPrefix, final String prefix) {
    	Map withPrefix = new HashMap();
    	String key = null;
    	for (Iterator it = withOutPrefix.keySet().iterator(); it.hasNext(); key = (String) it.next() ) {
    		withPrefix.put(prefix + key, withOutPrefix.get(key));
    	}
    	return withPrefix;
    }*/


    public Map<String,Object> removePrefixsFromMap(final Map<String,Object> withPrefix) {
        if (withPrefix==null || withPrefix.isEmpty() ) {
            return withPrefix;
        }

        Map<String,Object> withOutPrefix = new HashMap<String,Object>((int) (withPrefix.size()*(1.33)), (float).75);
        String key = null;
        String newKey = null;
        for (Iterator<String> it = withPrefix.keySet().iterator(); it.hasNext();) {
            key = it.next();
            newKey = key;
            if (newKey.indexOf(".")>=0) {
                newKey = newKey.substring(newKey.indexOf(".")+1);
            }
            withOutPrefix.put(newKey, withPrefix.get(key));
        }
        return withOutPrefix;
    }




    public static Map<String,Object> removeAllPrefixs(final Map<String,Object> withPrefix) {
        if (withPrefix==null || withPrefix.isEmpty() ) {
            return withPrefix;
        }

        Map<String,Object> withOutPrefix = new HashMap<String,Object>((int) (withPrefix.size()*(1.33)), (float).75);
        String key = null;
        String newKey = null;
        for (Iterator<String> it = withPrefix.keySet().iterator(); it.hasNext();) {
            key = it.next();
            newKey = key;
            while (newKey.indexOf(".")>=0) {
                newKey = newKey.substring(newKey.indexOf(".")+1);
            }
            withOutPrefix.put(newKey, withPrefix.get(key));
        }
        return withOutPrefix;
    }

    public Map<String,Object> replacePrefixsInMap(final Map<String,Object> withPrefix, final String prefix) {
        if (withPrefix==null || withPrefix.isEmpty() ) {
            return withPrefix;
        }

        Map<String,Object> replacedMap = new HashMap<String,Object>();
        String key;
        Map.Entry<String,Object> entry;
        //String newKey = null;

        for (Iterator<Map.Entry<String,Object>> it= withPrefix.entrySet().iterator();it.hasNext();) {
            entry = it.next();
            key = entry.getKey().toString();
            replacedMap.put(key.indexOf(".")>=0?prefix.concat(key.substring(key.indexOf(".")+1)):key, withPrefix.get(key));
        }

    	/*for (Iterator it = withPrefix.keySet().iterator(); it.hasNext();) {
    		key = (String) it.next();
    		newKey = key;
    		if (newKey.indexOf(".")>=0) {
    			newKey = prefix + newKey.substring(newKey.indexOf(".")+1);
    		}
    		replacedMap.put(newKey, withPrefix.get(key));
    	}*/
        return replacedMap;
    }


    protected Map<String,Object> removeRootPrefixFromMap(final Map<String,Object> withPrefix, String rootPrefix ) {
        if (withPrefix==null || withPrefix.isEmpty() ) {
            return withPrefix;
        }

        Map<String,Object> replacedMap = new HashMap<String,Object>((int) (withPrefix.size()*(1.33)), (float).75);

        Iterator<String> it = withPrefix.keySet().iterator();
        while (it.hasNext() ) {
            String key = it.next();
            String newKey = key;
            if (key.indexOf(rootPrefix) == 0 ) {
                newKey = key.substring(rootPrefix.length() );
            }
            replacedMap.put(newKey, withPrefix.get(key) );
        }
        return replacedMap;
    }

    protected Map<String,Object> getWithRootPrefixFromMap(final Map<String,Object> withPrefix, String rootPrefix ) {
        if (withPrefix==null || withPrefix.isEmpty() ) {
            return withPrefix;
        }

        Map<String,Object> replacedMap = new HashMap<String,Object>((int) (withPrefix.size()*(1.33)), (float).75);

        Iterator<String> it = withPrefix.keySet().iterator();
        while (it.hasNext() ) {
            String key = it.next();
            if (key.indexOf(rootPrefix) == 0 ) {
                replacedMap.put(key, withPrefix.get(key) );
            }
        }
        return replacedMap;
    }

    public static float roundFloat(float val) {
        return ((float) Math.round(val*100)/100);
    }

    public static String roundFloatAndGetString(float val) {
        return String.valueOf(((int) Math.round(val*100)));
    }

    public static float addPercantage(float val, float percantageToAdd, boolean roundToo) {
        float returnVal = val;
        returnVal += val * (percantageToAdd/100);
        return roundToo?roundFloat(returnVal):returnVal;
    }

    public static String deAccent(String str) {
        if(str==null || str.isEmpty()) return str;

        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        return deaccentpattern.matcher(nfdNormalizedString).replaceAll("").replaceAll("[^a-zA-Z0-9]","-").replaceAll("(-){2,}","-").toLowerCase();
    }

    public static String getDomainNameByUrl(String str) {
        if (str==null || str.isEmpty() ) return str;
        str = str.toLowerCase();
        if (str.indexOf("http://")==0) str = str.substring("http://".length());
        else if (str.indexOf("https://")==0) str = str.substring("https://".length());

        if (str.indexOf("www.")==0) str = str.substring("www.".length());
        if (str.indexOf("/")>0) str = str.substring(0, str.indexOf("/"));

        return str;

    }

    public static String getDomainUrlByUrl(String str) {
        if (str==null || str.isEmpty() ) return str;
        str = str.toLowerCase();
        if (str.indexOf("http://")==0) str = str.substring("http://".length());
        else if (str.indexOf("https://")==0) str = str.substring("https://".length());

        //if (str.indexOf("www.")==0) str = str.substring("www.".length());
        if (str.indexOf("/")>0) str = str.substring(0, str.indexOf("/"));

        return str;

    }

    public static <T> T coalesce(T ...items) {
        for(T i : items) if(i != null) return i;
        return null;
    }


}

