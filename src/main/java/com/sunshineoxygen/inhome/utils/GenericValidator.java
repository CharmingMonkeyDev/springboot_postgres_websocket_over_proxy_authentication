package com.sunshineoxygen.inhome.utils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






public class GenericValidator implements Serializable {

    public static void main(String args[]) throws MalformedURLException {
        String pat = "^([a-z0-9]{1,}|-?[a-z0-9]{1,}){4,20}$";
        String txt = "cor090-";
        System.out.println(txt.matches(pat));
        System.out.println(Pattern.matches(pat, txt));
        //ystem.out.println(pat.matches("-emrah"));

        System.out.println(new URL("http://-test.com.").getHost());
        System.out.println(isUrl("http://-test.com."));
        System.out.println(isValidTaxID(false, "S58781030", "ES"));
        System.out.println(isValidTaxID(false, "B86909496", "ES"));
        System.out.println(isValidTaxID(false, "B48537310","ES"));



    }
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;

    public static boolean isBlankOrNull(String value) {
        return ((value == null) || (value.trim().length() == 0));
    }

    public static boolean matchRegexp(String value, String regexp) {
        boolean match = false;

        if (regexp != null && regexp.length() > 0) {
            Pattern r = Pattern.compile(regexp);
            match = r.matcher(value).matches();
        }

        return match;
    }

    public static boolean isByte(String value) {
        return (GenericTypeValidator.formatByte(value) != null);
    }

    public static boolean isShort(String value) {
        return (GenericTypeValidator.formatShort(value) != null);
    }

    public static boolean isInt(String value) {
        return (GenericTypeValidator.formatInt(value) != null);
    }

    public static boolean isBoolean(String value) {
        return (GenericTypeValidator.formatBoolean(value) != null);
    }

    public static boolean isLong(String value) {
        return (GenericTypeValidator.formatLong(value) != null);
    }

    public static boolean isFloat(String value) {
        return (GenericTypeValidator.formatFloat(value) != null);
    }

    public static boolean isDouble(String value) {
        return (GenericTypeValidator.formatDouble(value) != null);
    }

    public static boolean isDate(String value, Locale locale) {
        boolean bValid = true;

        if (value != null) {
            try {
                DateFormat formatter = null;
                if (locale != null) {
                    formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                } else {
                    formatter =
                            DateFormat.getDateInstance(
                                    DateFormat.SHORT,
                                    Locale.getDefault());
                }

                formatter.setLenient(false);

                formatter.parse(value);
            } catch (ParseException e) {
                bValid = false;
            }
        } else {
            bValid = false;
        }

        return bValid;
    }

    public static boolean isDateTime(String value, Locale locale) {
        boolean bValid = true;

        if (value != null) {
            try {
                DateFormat formatter = null;
                if (locale != null) {
                    formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT, locale);
                } else {
                    formatter =
                            DateFormat.getDateInstance(
                                    DateFormat.SHORT,
                                    Locale.getDefault());
                }

                formatter.setLenient(false);

                formatter.parse(value);
            } catch (ParseException e) {
                bValid = false;
            }
        } else {
            bValid = false;
        }

        return bValid;
    }


    public static boolean isDate(String value, String datePattern, boolean strict) {

        boolean bValid = true;

        if (value != null && datePattern != null && datePattern.length() > 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                formatter.setLenient(false);

                formatter.parse(value);

                if (strict) {
                    if (datePattern.length() != value.length()) {
                        bValid = false;
                    }
                }

            } catch (ParseException e) {
                bValid = false;
            }
        } else {
            bValid = false;
        }

        return bValid;
    }

    public static boolean isDateTime(String value, String dateTimePattern, boolean strict) {

        boolean bValid = true;

        if (value != null && dateTimePattern != null && dateTimePattern.length() > 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern);
                formatter.setLenient(false);

                formatter.parse(value);

                if (strict) {
                    if (dateTimePattern.length() != value.length()) {
                        bValid = false;
                    }
                }

            } catch (ParseException e) {
                bValid = false;
            }
        } else {
            bValid = false;
        }

        return bValid;
    }



    public static boolean isInRange(int value, int min, int max) {
        return ((value >= min) && (value <= max));
    }

    public static boolean isInRange(float value, float min, float max) {
        return ((value >= min) && (value <= max));
    }

    public static boolean isInRange(short value, short min, short max) {
        return ((value >= min) && (value <= max));
    }

    public static boolean isInRange(double value, double min, double max) {
        return ((value >= min) && (value <= max));
    }

    public static boolean isCreditCard(String value) {
        return (
                validateCreditCardLuhnCheck(value)
                        && validateCreditCardPrefixCheck(value));
    }


    protected static boolean validateCreditCardLuhnCheck(String cardNumber) {
        // number must be validated as 0..9 numeric first!!
        int digits = cardNumber.length();
        int oddoeven = digits & 1;
        long sum = 0;
        for (int count = 0; count < digits; count++) {
            int digit = 0;
            try {
                digit = Integer.parseInt(String.valueOf(cardNumber.charAt(count)));
            } catch (NumberFormatException e) {
                return false;
            }
            if (((count & 1) ^ oddoeven) == 0) { // not
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        if (sum == 0) {
            return false;
        }

        if (sum % 10 == 0) {
            return true;
        }

        return false;
    }

    protected static boolean validateCreditCardPrefixCheck(String cardNumber) {
        final String AX_PREFIX = "34,37,";
        final String VS_PREFIX = "4";
        final String MC_PREFIX = "51,52,53,54,55,";
        final String DS_PREFIX = "6011";

        int length = cardNumber.length();
        if (length < 13) {
            return false;
        }

        boolean valid = false;
        int cardType = 0;

        String prefix2 = cardNumber.substring(0, 2) + ",";

        if (AX_PREFIX.indexOf(prefix2) != -1) {
            cardType = 3;
        }
        if (cardNumber.substring(0, 1).equals(VS_PREFIX)) {
            cardType = 4;
        }
        if (MC_PREFIX.indexOf(prefix2) != -1) {
            cardType = 5;
        }
        if (cardNumber.substring(0, 4).equals(DS_PREFIX)) {
            cardType = 6;
        }

        if ((cardType == 3) && (length == 15)) {
            valid = true;
        }
        if ((cardType == 4) && ((length == 13) || (length == 16))) {
            valid = true;
        }
        if ((cardType == 5) && (length == 16)) {
            valid = true;
        }
        if ((cardType == 6) && (length == 16)) {
            valid = true;
        }

        return valid;
    }

    public static boolean isUrl(String url) {
        try{
            new URL(url);
            return true;
        }catch(MalformedURLException mex){
            return false;
        }

    }



    public static boolean isEmail(String email) {
        boolean bValid = true;

        try {
            Pattern quotedPattern = Pattern.compile("^\"[\\x00-\\x7F&&[^\\x22\\x0D]]{1,}\"$");
            Pattern atomPattern = Pattern.compile("^[\\x21-\\x7E&&[^\\x28\\x29\\x3C\\x3D\\x40\\x2C\\x3B\\x3A\\x2F\\x5C\\x22\\x2E\\x5B\\x5D]]{1,}$");
            Pattern domainLiteralPattern = Pattern.compile("^\"[\\x00-\\x7F&&[^\\x5B\\x5D\\x0D]]{1,}\"$");
            if (email==null || email.indexOf("@")<1 || email.indexOf("@")==email.length() ) {
                return false;
            }else{
                String localPart = email.substring(0, email.indexOf("@"));
                String domain = email.substring(email.indexOf("@")+1);
                String localParts[] = localPart.split("\\.");
                String domainParts[] = domain.split("\\.");
                if (localPart.lastIndexOf(".")==localPart.length()-1){
                    return false;
                }else if (domain.lastIndexOf(".")==domain.length()-1) {
                    return false;
                }else if (domainParts.length<2) {
                    return false;
                }

                for (int i=0;i<localParts.length;i++) {

                    if (localParts[i].indexOf("\"")==0) { // it is quoted string;
                        Matcher quotedMatcher = quotedPattern.matcher(localParts[i]);
                        bValid = bValid && quotedMatcher.matches();
                    }else {
                        Matcher wordMatcher = atomPattern.matcher(localParts[i]);
                        bValid = bValid && wordMatcher.matches();
                    }
                }

                for (int i=0; i<domainParts.length; i++) {
                    Matcher domainRefMatcher = atomPattern.matcher(domainParts[i]);
                    Matcher domainLitMatcher = domainLiteralPattern.matcher(domainParts[i]);
                    bValid = bValid && (domainRefMatcher.matches()||domainLitMatcher.matches());
                }
            }
        } catch (Exception e) {
            bValid = false;
        }

        return bValid;
    }

    public static boolean maxLength(String value, int max) {
        return (value.length() <= max);
    }

    public static boolean minLength(String value, int min) {
        return (value.length() >= min);
    }

    public static boolean checkHappyDigitsNumber(String mainHappyDigitsNumber) {
        if (mainHappyDigitsNumber==null || mainHappyDigitsNumber.trim().equals("") || mainHappyDigitsNumber.trim().length()!=16) {
            return false;
        }

        String happyDigitsNumber = mainHappyDigitsNumber.substring(0,15);
        String checkDigit = mainHappyDigitsNumber.substring(15);
        try{
            Double.parseDouble(happyDigitsNumber);
            Integer.parseInt(checkDigit);
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        int sum = 0;
        happyDigitsNumber+=" ";
        for (int i=14; i>=0; i--) {
            int tmp = Integer.parseInt(happyDigitsNumber.substring(i,i+1));
            if (i%2==0) {
                tmp = tmp*2;
                if (tmp>=10) {
                    tmp = tmp-9;
                }
            }
            sum += tmp;
        }
        int checkDgt = 0;
        if (sum%10==0) checkDgt = 0;
        else checkDgt = 10 - (sum%10);

        if (checkDgt==Integer.parseInt(checkDigit)) {
            return true;
        }

        return false;
    }

    public static boolean isValidTaxID(String taxID, String countryCode) {
        boolean isValid = isValidTaxID(true, taxID, countryCode);
        if(!isValid) {
            isValid = isValidTaxID(false, taxID, countryCode);
        }
        return isValid;
    }

    public static boolean isValidTaxID(boolean isPerson, String taxID, String countryCode) {
        taxID = taxID.toUpperCase();
        taxID = taxID.replace(" ", "");
        if (countryCode != null) {
            if ("ES".equals(countryCode)) {
                if (isPerson) {
                    boolean isValidNIF = isValidNIF(taxID);
                    if(!isValidNIF) {
                        if (taxID.toUpperCase().indexOf('X') == 0 || taxID.toUpperCase().indexOf('Y') == 0
                                || taxID.toUpperCase().indexOf('Z') == 0) {
                            if (taxID.indexOf("X") == 0)
                                taxID = "0" + taxID.substring(1);
                            else if (taxID.indexOf("Y") == 0)
                                taxID = "1" + taxID.substring(1);
                            else if (taxID.indexOf("Z") == 0)
                                taxID = "2" + taxID.substring(1);

                            isValidNIF = isValidNIF(taxID);
                        }
                        if (!isValidNIF) {
                            if (taxID.indexOf("X")==0) {
                                return isValidCIF(taxID);
                            }else{
                                return false;
                            }
                        }
                    }else {
                        return true;
                    }
                } else {
                    return isValidCIF(taxID);
                }
            }
            /*
             * else if ("PT".equals(countryCode)) { if (isPerson) { Pattern pat
             * = Pattern.compile(
             * "[A-Z]{6}\\d{2}[A-Z]{1}\\d{2}[A-Z]{1}\\d{3}[A-Z]{1}"); return
             * pat.matcher(taxID.toUpperCase()).matches(); }else { Pattern pat =
             * Pattern.compile("\\d{11}"); return pat.matcher(taxID).matches();
             *
             * } }
             */
        }
        return true;

    }


    private static boolean isValidCIF(String cif) {
        if (cif==null) {
            return false;
        }

        if (cif.length()<9) {
            //return false;
        }

        cif = cif.replaceAll("-","");

        //String strLetter = cif.substring(0,1);
        //String strNumber = cif.substring(1, 8);
        // S/tring strDigit = cif.substring(8);


        String letter  = cif.substring(0,1);
        String   number  = cif.substring(1, 8);
        String   control = cif.substring(8);

        int even_sum = 0;
        int odd_sum = 0;
        int n;

        for ( int i = 0; i < number.length(); i++) {
            n = Integer.valueOf(number.charAt(i)+"");

            // Odd positions (Even index equals to odd position. i=0 equals first position)
            if ( i % 2 == 0 ) {
                n *= 2;
                odd_sum += n < 10 ? n : n - 9;
            }else {
                even_sum += n;
            }

        }

        int sumTotal = even_sum +odd_sum;
        int sum_digit = 0;
        if((sumTotal%10)>0) sum_digit = 10-(sumTotal%10);

        //int control_digit = (10 - Integer.parseInt(sumTotalStr.substring(sumTotalStr.length()-1) ));
        // control_digit = control_digit%10;
        Integer control_digit = null;
        try{
            control_digit = Integer.parseInt(control);
        }catch(Exception e){

        }

        // in case of digit
        if(control_digit!=null){
            return control_digit.intValue()==sum_digit;
        }else if ("X".equals(letter)) {
            return isValidNIF(number+control);
        }else{
            String control_letter = "JABCDEFGHI".substring( sum_digit,sum_digit+1 );
            return control.equals(control_letter);
        }


//        if(control_digit<10) control_letter = "JABCDEFGHI".substring( control_digit,control_digit+1 );
//
//        // Control must be a digit
//        if ( letter.matches("[ABEH]") ) {
//          return control.equals(control_digit+"");
//
//        // Control must be a letter
//        } else if ( letter.matches("[KPQS]") ) {
//          return control.equals(control_letter);
//        }else if ("X".equals(letter)) {
//        	return isValidNIF(number+control);
//
//        // Can be either
//        } else {
//          return (control == control_digit+"" || control.equals(control_letter));
//
//        }
//        String possibleInitials = ",A,B,C,D,E,F,G,H,K,L,M,P,Q,S,X,R,";
//
//        if (!Character.isLetter(strLetter.charAt(0))) {
//            return false;
//        }else {
//            if (possibleInitials.indexOf(","+strLetter+",")<0) {
//                return false;
//            }
//        }
//
//        for (int i=0;i<7;i++) {
//            if (!Character.isDigit(strNumber.charAt(i)) ) {
//                return false;
//            }
//        }
//
//        int sum = 0;
//        for (int i=0;i<7;i++) {
//            int auxNum = 0;
//            if (i%2!=0) {
//                sum += Integer.parseInt(strNumber.substring(i,i+1));
//            }else {
//                auxNum = Integer.parseInt(strNumber.substring(i,i+1)) * 2;
//                sum += auxNum < 10 ? auxNum : auxNum- 9; //(int) (auxNum/10) + (auxNum%10);
//            }
//
//        }
//
//        sum = (10-sum%10)%10;
//
//
//        String strDigitAux = null;
//        if (",K,P,Q,S,".indexOf(","+strLetter+",")>=0) {
//            sum += 64;
//            strDigitAux = Character.toString((char) sum);
//
//        }else if ("X".equals(strLetter)) {
//            return isValidNIF(strNumber+strDigit);
//        }else {
//            strDigitAux =""+sum;
//            return strDigit.equals(strDigitAux);
//        }
//
//        return strDigit.equals(strDigitAux);




    }

    private static boolean isValidNIF (String nif)
    {
        // check N.I.F is not null
        if (nif == null) {
            return false;
        }

        // check N.I.F at least 8 characters long
        if (nif.length () < 8) {
            return false;
        }

        // normalize N.I.F, remove any dashes
        StringBuffer b = new StringBuffer (nif);

        for (int index = 0; (index = b.indexOf ("-", index)) != -1;) {
            b.deleteCharAt (index++);
        }

        nif = b.toString ();

        // check last character is letter
        if (! Character.isLetter (nif.charAt (nif.length () - 1))) {
            return false;
        }

        // check all but last character are digits
        for (int index = 0; index < nif.length () - 1; index++) {
            if (! Character.isDigit (nif.charAt (index))) {
                return false;
            }
        }

        // check N.I.F. code
        int number = 0;
        char[] code = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        try {
            number = Integer.parseInt (nif.substring (0, nif.length () - 1));
        }
        catch (NumberFormatException nfex) {
            return false;
        }

        if (nif.charAt (nif.length () - 1) != code[number % 23]) {
            return false;
        }

        return true;
    }

    public static boolean isPostalCode(String postalCode, String countryCode) {
        if (postalCode==null || postalCode.isEmpty() || countryCode==null || countryCode.isEmpty()) {
            return false;
        }else{
            try{
                return PostalCodeValidator.validate(postalCode, countryCode);
            }catch(Exception e) {
                return false;
            }
        }
    }


}
