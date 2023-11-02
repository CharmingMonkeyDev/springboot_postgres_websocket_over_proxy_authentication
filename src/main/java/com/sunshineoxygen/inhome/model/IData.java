package com.sunshineoxygen.inhome.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author EHizarci
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IData extends Serializable {
    public StringBuffer getAsXMLPart();

    public DynamicBean getAsDynamicBean();

    public Map<String,Object> getAsMap();

}