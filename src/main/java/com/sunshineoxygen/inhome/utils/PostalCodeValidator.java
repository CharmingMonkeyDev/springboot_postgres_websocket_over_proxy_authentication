package com.sunshineoxygen.inhome.utils;

public class PostalCodeValidator {
    private static String url = "http://ws.geonames.org/postalCodeSearch?postalcode={{PC}}&country={{CC}}&maxRows=3";
    /**
     * @param args
     * @throws Exception
     */

    public static boolean validate(String postalCode, String countryCode) throws Exception {
        String urlIn = url.replace("{{PC}}", postalCode).replace("{{CC}}", countryCode);
        String content = HttpDownloader.getContent(urlIn, null, null);
        String totalResultsCount = null;
        try{
            totalResultsCount = content.substring(0,content.indexOf("</totalResultsCount>"));
            totalResultsCount = totalResultsCount.substring(totalResultsCount.lastIndexOf('>')+1);
        }catch(Exception e) {
            e.printStackTrace();
        }

        if (totalResultsCount==null || totalResultsCount.isEmpty() || "0".equals(totalResultsCount)){
            return false;
        }else{
            return true;
        }
    }
}

