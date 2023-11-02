package com.sunshineoxygen.inhome.utils;

import java.util.regex.Pattern;



/**
 * @author AKhatskevich
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TagUtils {

    private TagUtils() {}

    public static final String	INPUT			= "<input";
    public static final String	SELECT			= "<select";
    public static final String	OPTION			= "<option";
    public static final String	SPAN			= "<span";
    public static final String	A				= "<a";
    public static final String	LABEL			= "<label";
    public static final String	IMG				= "<img";
    public static final String	ENDTAG			= " >";
    public static final String	QUOTE			= "\"";
    public static final String	ENDSELECT		= "</select>";
    public static final String	ENDOPTION		= "</option>";
    public static final String	ENDSPAN			= "</span>";
    public static final String	ENDA			= "</a>";
    public static final String	ENDLABEL		= "</label>";
    public static final String	TEXTAREA		= "<textarea";
    public static final String	ENDTEXTAREA		= "</textarea>";
    public static final String	TABLE			= "<table";
    public static final String	ENDTABLE		= "</table>";
    public static final String	TR				= "<tr";
    public static final String	ENDTR			= "</tr>";
    public static final String	TD				= "<td";
    public static final String	ENDTD			= "</td>";

    public static final String	BR				= "<br>";
    public static final String	NBSP			= "&nbsp;";
    public static final String	TABLEATTRS		= " cellspacing=\"0\" cellpadding=\"0\" border=\"0\" ";

    public static final String	EMPTY			= "";
    public static final String	DOT				= ".";
    public static final String	UNDERSCORE		= "_";
    public static final String	PIPE			= "|";
    public static final String	ENDOFLINE		= "\n";
    public static final String	SPACE			= " ";
    public static final String	QUESTION		= "?";
    public static final String	EQUAL			= "=";
    public static final String	GREATER_THEN	= ">";
    public static final String	GT				= "&gt;";
    public static final String	LESS_THEN		= "<";
    public static final String	LT				= "&lt;";
    public static final String	AND				= "&";
    public static final String	AMP				= "&amp;";
    public static final String	APOSTROPHE		= "'";
    public static final String	APOSTROPHE_CODE	= "&#039;";
    public static final String	QUOT			= "&quot;";

    public static final String	TEXT			= "text";
    public static final String	PASSWORD		= "password";
    public static final String	CHECKBOX		= "checkbox";
    public static final String	RADIO			= "radio";
    public static final String	HIDDEN			= "hidden";
    public static final String	CHECKED			= " checked ";
    public static final String	SELECTED		= " selected ";
    public static final String	READONLY		= " readonly ";
    public static final String 	PLACEHOLDER		= "placeholder=\"";
    public static final String	DISABLED		= " disabled ";
    public static final String	BUTTON			= "button";

    public static final String	DOC_LOCATION	= "document.location='";

    public static final String	VALUE			= " value=\"";
    public static final String	TYPE			= " type=\"";
    public static final String	NAME			= " name=\"";

    public static final String	HREF			= " href=\"";
    public static final String	MAXLENGTH		= " maxlength=\"";
    public static final String	SIZE			= " size=\"";
    public static final String	CLASS			= " class=\"";
    public static final String	ONCLICK			= " onClick=\"";
    public static final String	ONCHANGE		= " onChange=\"";
    public static final String	ONFOCUS			= " onFocus=\"";
    public static final String	ONBLUR			= " onBlur=\"";

    public static final String	FOR				= " for=\"";
    public static final String	ID				= " id=\"";
    public static final String	COLS			= " cols=\"";
    public static final String	ROWS			= " rows=\"";
    public static final String	TARGET			= " target=\"";
    public static final String	STYLE			= " style=\"";
    public static final String	WIDTH			= " width=\"";
    public static final String	HEIGHT			= " height=\"";
    public static final String	BORDER			= " border=\"";
    public static final String	SRC				= " src=\"";
    public static final String 	TABINDEX		= " tabindex=\"";
    public static final String 	ALT				= " alt=\"";
    public static final String 	HSPACE			= " hspace=\"";
    public static final String 	VSPACE			= " vspace=\"";


    public static final String AND_CODE			= "%26";
    public static final String SPACE_CODE		= "%20";
    public static final String TITLE = " title=\"";


    public static Pattern _pattern_AND						= Pattern.compile("&");
    public static Pattern _pattern_GREATER_THEN				= Pattern.compile(">");
    public static Pattern _pattern_LESS_THEN				= Pattern.compile("<");
    public static Pattern _pattern_APOSTROPHE				= Pattern.compile("'");
    public static Pattern _pattern_QUOTE					= Pattern.compile("\"");
    public static Pattern _pattern_ENDOFLINE				= Pattern.compile("\n");


    public static String replaceNewLineToHTMLCharacters(final String value ) {
        if (value == null ) return null;
        String returnVal = value;
        if (returnVal.indexOf("\\n")>=0) returnVal = returnVal.replace("\\n", "<br/>");//_pattern_ENDOFLINE.matcher(returnVal).replaceAll(BR);
        return returnVal;
    }

    public static String replaceSpecialCharacters(final String value ) {
        if (value == null ) return null;

        String returnVal = value;

        if (returnVal.indexOf('&')>=0) returnVal = _pattern_AND.matcher(returnVal).replaceAll(AMP);
        if (returnVal.indexOf('>')>=0) returnVal = _pattern_GREATER_THEN.matcher(returnVal).replaceAll(GT);
        if (returnVal.indexOf('<')>=0) returnVal = _pattern_LESS_THEN.matcher(returnVal).replaceAll(LT);
        if (returnVal.indexOf('\'')>=0) returnVal = _pattern_APOSTROPHE.matcher(returnVal).replaceAll(APOSTROPHE_CODE);
        if (returnVal.indexOf('\"')>=0) returnVal = _pattern_QUOTE.matcher(returnVal).replaceAll(QUOT);
        if (returnVal.indexOf('\n')>=0) returnVal = _pattern_ENDOFLINE.matcher(returnVal).replaceAll(BR);


		/*
		StringBuffer returnValue = new StringBuffer(value );

		int index = 0;
		while ((index = returnValue.indexOf(AND, index ) ) != -1 ) {
			returnValue.replace(index, ++index, AMP );
		}

		index = 0;
		while ((index = returnValue.indexOf(GREATER_THEN, index ) ) != -1 ) {

			returnValue.replace(index, ++index, GT );
		}

		index = 0;
		while ((index = returnValue.indexOf(LESS_THEN, index ) ) != -1 ) {
			returnValue.replace(index, ++index, LT );
		}

		index = 0;
		while ((index = returnValue.indexOf(APOSTROPHE, index ) ) != -1 ) {
			returnValue.replace(index, ++index, APOSTROPHE_CODE );
		}

		index = 0;
		while ((index = returnValue.indexOf(QUOTE, index ) ) != -1 ) {
			returnValue.replace(index, ++index, QUOT );
		}

		index = 0;
		while ((index = returnValue.indexOf(ENDOFLINE, index ) ) != -1 ) {
			returnValue.replace(index, ++index, BR );
		}

		return returnValue.toString();
		*/
        return returnVal;
    }

    public static StringBuffer replaceSpecialCharactersInLink(final String link) {
        if (link == null ) return null;

        StringBuffer res = new StringBuffer(link );

        int index = 0;
        while ((index = res.indexOf(AND, index ) ) != -1 ) {
            res.replace(index, ++index, AND_CODE );
        }

        index = 0;
        while ((index = res.indexOf(SPACE, index ) ) != -1 ) {
            res.replace(index, ++index, SPACE_CODE );
        }

        return res;
    }

    public static String deleteScripts(final String value ){
        if (value == null ) return null;
		/*
		String returnVal =null;
		if(value.contains("<script>")){
			String[] tempValue=value.split("<script>");
			returnVal=tempValue[0];
			for(int i=1;i<tempValue.length;i++){
				returnVal+=tempValue[i].substring(tempValue[i].lastIndexOf("</script>")+9);
			}
		}*/
        return stripXSS(value);
    }

    private static Pattern[] scriptPatterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };


    private static String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("\0", "");

            // Remove all sections that match a pattern
            for (Pattern scriptPattern : scriptPatterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}

