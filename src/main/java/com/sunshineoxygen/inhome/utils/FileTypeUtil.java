package com.sunshineoxygen.inhome.utils;


import com.sunshineoxygen.inhome.model.DynamicBean;

import java.io.File;
import java.util.*;

public class FileTypeUtil {
	static Map<String, DynamicBean> FILE_TYPES = new HashMap<String,DynamicBean>();
	
	public static List<String> getFileTypes() {
		return new ArrayList<String>(FILE_TYPES.keySet());
	}
	
	public static DynamicBean getFileType(String name) {
		return FILE_TYPES.get(name);
	}

	public static DynamicBean getFileTypeByExtension(String extension) {
		for(Iterator<Map.Entry<String,DynamicBean>> it=FILE_TYPES.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, DynamicBean> entry = it.next();
			if(extension.equals(entry.getValue().get("extension"))){
				return entry.getValue();
			}
		}
		
		return null;
		
	}

	public static DynamicBean getFileTypeByFile(File file) {
		String suffix = FileUtil.getSuffix(file.getName());
		DynamicBean bean = getFileTypeByExtension(suffix);
		
		if(bean!=null)
			return bean;
		
		return getUnknownFileType(file);
		
	}
	
	public static DynamicBean getUnknownFileType(File file) {
		String suffix = FileUtil.getSuffix(file.getName());
		javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
		DynamicBean bean = new DynamicBean();
		bean.addProperty("mimetype", mtMap.getContentType(file));
		bean.addProperty("extension", suffix);
		bean.addProperty("filetypename", suffix);
		bean.addProperty("editable", false);
		bean.addProperty("templateallow", false);
		return bean;
	}
	
	static{
		DynamicBean bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/png");
		bean.addProperty("extension", "png");
		bean.addProperty("filetypename", "png");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("png", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/jpg"); 
		bean.addProperty("extension", "jpg");
		bean.addProperty("filetypename", "jpg");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);

		FILE_TYPES.put("jpg", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/jpeg");
		bean.addProperty("extension", "jpeg");
		bean.addProperty("filetypename", "jpeg");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("jpeg", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/gif");
		bean.addProperty("extension", "gif");
		bean.addProperty("filetypename", "gif");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("gif", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/bmp");
		bean.addProperty("extension", "bmp");
		bean.addProperty("filetypename", "bmp");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("bmp", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/json");
		bean.addProperty("extension", "json");
		bean.addProperty("filetypename", "json");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "text");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("json", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/js");
		bean.addProperty("extension", "js");
		bean.addProperty("filetypename", "javascript");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "text");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("js", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "text/html");
		bean.addProperty("extension", "html");
		bean.addProperty("filetypename", "html");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "text");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("html", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "text/css");
		bean.addProperty("extension", "css");
		bean.addProperty("filetypename", "css");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "text");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("css", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "text/plain");
		bean.addProperty("extension", "txt");
		bean.addProperty("filetypename", "txt");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "text");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("txt", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/x-shockwave-flash");
		bean.addProperty("extension", "swf");
		bean.addProperty("filetypename", "swf");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "swf");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("swf", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/font-sfnt");
		bean.addProperty("extension", "ttf");
		bean.addProperty("filetypename", "ttf");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "font");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("ttf", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/svg+xml");
		bean.addProperty("extension", "svg");
		bean.addProperty("filetypename", "svg");
		bean.addProperty("editable", true);
		bean.addProperty("filetypegroup", "font");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("svg", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/x-font-woff");
		bean.addProperty("extension", "woff");
		bean.addProperty("filetypename", "woff");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "font");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("woff", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/font-sfnt");
		bean.addProperty("extension", "otf");
		bean.addProperty("filetypename", "otf");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "font");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("otf", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/pdf");
		bean.addProperty("extension", "pdf");
		bean.addProperty("filetypename", "pdf");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "document");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("pdf", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/excel");
		bean.addProperty("extension", "xls");
		bean.addProperty("filetypename", "xls");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "document");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("xls", bean);
		
		bean.addProperty("extension", "xlsx");		
		FILE_TYPES.put("xlsx", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/msword");
		bean.addProperty("extension", "doc");
		bean.addProperty("filetypename", "doc");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "document");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("doc", bean);
		
		bean.addProperty("extension", "docx");
		FILE_TYPES.put("docx", bean);
		
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "application/x-compressed");
		bean.addProperty("extension", "zip");
		bean.addProperty("filetypename", "zip");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "compressed");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("zip", bean);
		
		bean = new DynamicBean();
		
		bean.addProperty("mimetype", "image/x-icon");
		bean.addProperty("extension", "icon");
		bean.addProperty("filetypename", "icon");
		bean.addProperty("editable", false);
		bean.addProperty("filetypegroup", "image");
		bean.addProperty("templateallow", true);
		
		FILE_TYPES.put("ico", bean);
	}
}
