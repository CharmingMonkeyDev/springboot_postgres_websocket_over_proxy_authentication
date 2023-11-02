package com.sunshineoxygen.inhome.exception;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/** Framework common exception.
 * @author   miraculix
 * @version  1.0
 */
@Slf4j
public class ApplicationException extends java.lang.Exception
{
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;

    private static String ERROR_TEXT  = "Olution.com Application Exception with ErrorCode :";

    /**
     * @uml.property  name="errorCodes"
     */
    protected Integer[]		errorCodes    	= null;
    protected String[][]	parametersArray = null;

    private Exception 	rootException  = null;

    /**
     * Creates new <code>ApplicationException</code> without detail message.
     */
    public ApplicationException () {

    }

    public ApplicationException(Exception rootException) {
        super((rootException instanceof SQLException)?"Error on saving data!":rootException.getMessage());
        if(rootException instanceof SQLException){
            log.error( rootException.getMessage());


        }
        this.rootException= rootException;
    }

    /**
     * Constructs an <code>ApplicationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ApplicationException (String msg) {
        super(msg);
    }

    public ApplicationException (String localeCode, String text, String...values) {
        //super(ConfigHolder.getLabelInLocale(localeCode, text, values));
        super(text);
    }


    public ApplicationException(Integer errorCode){
        super(ERROR_TEXT+errorCode.toString());

        errorCodes = new Integer[1];
        errorCodes[0] = errorCode;
        parametersArray = new String[1][1];
        parametersArray[0] = null;

    }

    public ApplicationException(Integer errorCodes[], String[][] parametersArray) {
        super(ERROR_TEXT+(errorCodes!=null?errorCodes.toString():"") );

        //this.parametersArray = parametersArray;
        /*Map map = new HashMap();
        for (int i=0;i<errorCodes.length;i++) {
            map.put(errorCodes[i].toString(),null);
        }*/

        if (errorCodes!=null) {
            List<Integer> errorList = new ArrayList<Integer>();
            List<String[]> paramList = new ArrayList<String[]>();

            for (int i=0;i<errorCodes.length;i++) {
                if (!errorList.contains(errorCodes[i])) {
                    errorList.add(errorCodes[i]);
                    if(parametersArray!=null) {
                        paramList.add(parametersArray[i]);
                    }else {
                        paramList.add(null);
                    }
                }else {
                    if (parametersArray!=null && parametersArray[i]!=null) {
                        if (!paramList.contains(parametersArray[i])) {
                            errorList.add(errorCodes[i]);
                            paramList.add(parametersArray[i]);
                        }
                    }
                }
            }

            this.errorCodes = new Integer[errorList.size()];
            this.parametersArray = new String[errorList.size()][];

            for (int i = 0;i<errorList.size();i++) {
                this.errorCodes[i] = errorList.get(i);
                this.parametersArray[i] = paramList.get(i);
            }

        }
    }

    public ApplicationException(Integer errorCode, String[] parameters ){
        super(ERROR_TEXT+errorCode.toString());

        errorCodes = new Integer[1];
        errorCodes[0] = errorCode;

        if (parameters!=null) {
            parametersArray = new String[1][parameters.length];
            parametersArray[0] = parameters;
        }
    }

    /**
     * @return
     * @uml.property  name="errorCodes"
     */
    public Integer[] getErrorCodes() {
        return errorCodes;
    }

    public String[][] getErrorCodesParameters() {
        return parametersArray;
    }

    public List<String>  getErrorsTexts(String localeCode ) {
        if (errorCodes == null ) {
            List<String> errors = new ArrayList<String>();
            errors.add(toString() );
            return errors;
        }

        List<String> errors = new ArrayList<String>();
        for (int i=0; i < errorCodes.length;i++ ) {
            String errorText = null; //DictionaryHolder.getErrorText(errorCodes[i], localeCode);
            if (errorText == null ) {
                errorText = "Unknown error: " + errorCodes[i].toString();
            }
//			} else if (parametersArray!=null && parametersArray[i] != null ) {
//				errorText = replaceParameters(errorText, parametersArray[i] );
//			}
            errors.add(errorText );
        }
        return errors;
    }

    public Exception getRootException() {
        return rootException;
    }

    public void setRootException(Exception rootException) {
        this.rootException = rootException;
    }

//    private String replaceParameters(String source, String[] parameters) {
//        if (parameters != null && parameters.length > 0 ) {
//            for (int index = 0; index < parameters.length; index++ ) {
//                source = source.replaceAll(";%" + (index + 1) + ";", parameters[index] );
//            }
//        }
//        return source;
//    }

}
