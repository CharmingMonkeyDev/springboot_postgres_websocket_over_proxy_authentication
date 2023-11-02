package com.sunshineoxygen.inhome.utils;

import com.sunshineoxygen.inhome.model.DynamicBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HtmlUtil {

    public static String safeString(String string) {

		/*https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet
		 & --> &amp;
		 < --> &lt;
		 > --> &gt;
		 " --> &quot;
		 ' --> &#x27;     &apos; not recommended because its not in the HTML spec (See: section 24.4.1) &apos; is in the XML and XHTML specs.
		 / --> &#x2F;*/

        if(string==null || string.isEmpty()) return string;
        else{
            return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
        }
    }

    public static List<String> generateDataAttributes(DynamicBean bean, String...keys) {
        List<String> list = new ArrayList<String>();
        List<String> keyFilterList = null;
        if (keys.length > 0) {
            keyFilterList = Arrays.asList(keys);
        }
        for (String key: bean.getKeys()) {
            if (bean.get(key) != null && !bean.get(key).isEmpty() && (keyFilterList == null || keyFilterList.contains(key))) {
                list.add("data-" + key + "=\"" + bean.getPropertyAsHtmlSafeString(key) +"\"");
            }
        }
        return list;
    }
}
