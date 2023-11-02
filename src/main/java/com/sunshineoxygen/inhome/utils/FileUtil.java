package com.sunshineoxygen.inhome.utils;

import com.sunshineoxygen.inhome.model.BasicData;
import com.sunshineoxygen.inhome.model.DynamicBean;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.URI;
import java.text.Normalizer;
import java.util.*;

public class FileUtil {
	public static List<String> _IMAGE_FILE_EXTENSIONS = Arrays.asList(new String[] { "bmp", "gif", "jpg", "png", "jpeg", "ico", "svg" });
	public static List<String> _RESIZABLE_IMAGE_FILE_EXTENSIONS = Arrays.asList(new String[] { "bmp", "gif", "jpg", "png", "jpeg", "ico"});
	public static List<String> _PDF_FILE_EXTENSIONS = Arrays.asList(new String[] { "pdf" });
	public static List<String> _ARCHIVE_FILE_EXTENSIONS = Arrays.asList(new String[] { "zip" });
	public static List<String> _SPREADSHEET_FILE_EXTENSIONS = Arrays.asList(new String[] { "csv","xls","xlsx" });

	public static boolean isImageExtension(String extension) {
		return _IMAGE_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
	}
	
	public static boolean isResizableImageExtension(String extension) {
		return _RESIZABLE_IMAGE_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
	}
	
	public static boolean isPdfExtension(String extension) {
		return _PDF_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
	}

	public static boolean isArchiveExtension(String extension) {
		return _ARCHIVE_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
	}
	
	public static boolean isSpreadsheetExtension(String extension) {
		return _SPREADSHEET_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.ENGLISH));
	}
	
	public static DynamicBean getFileTypeInformationAsDynamicBean(File file) {
		
		if (file.exists()) {
			return FileTypeUtil.getFileTypeByFile(file);
		}
		return null;
	}

	public static String getMimeType(File file) {
		return getFileTypeInformationAsDynamicBean(file).get("mimetype");
	}
	
	public static String readFileFromSamePackage(Class _class, String fileName){
		InputStream is = _class.getResourceAsStream(fileName);
		return new Scanner(is).useDelimiter("\\A").next();
	}
	
	public static String fixFileName(String str) { 
		if(str==null || str.isEmpty()) return str;
		
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);     
		return BasicData.deaccentpattern.matcher(nfdNormalizedString).replaceAll("").replaceAll("[^a-zA-Z0-9]","-").replaceAll("(-){2,}","-");
	}
	
	
	public static String generateFilePathOnTemp(String fileName, String fileExtension) {
		if(fileName==null || fileName.isEmpty()) {
			fileName = RandomString.nextString(20);
		}
		String _fileName = FileUtil.fixFileName(fileName);
		if(fileExtension!=null && !fileExtension.isEmpty()){
			_fileName = _fileName.concat(".").concat(fileExtension);
		}
		return getSystemTempFolderPath().concat(_fileName);
	}

	public static String readFileContent(String fileUrl) {
		return readFileContent(fileUrl, false);
	}
	
	public static String readFileContent(String fileUrl, boolean returnNullOnFail) {
		byte[] bytes = null;
		try{
			File file = new File(fileUrl);
			URI uri = file.toURI();
			bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(uri));
		}catch(IOException e) {
			return returnNullOnFail ? null : "ERROR loading file "+fileUrl;
		}
		
		return bytes==null && returnNullOnFail ? null : new String(bytes);
	}
	
	
	public static String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		suffix = suffix.toLowerCase();
		return suffix;
	}

	public static String getFileName(String fileName) {
		int pos = fileName.lastIndexOf('.');
		if (pos > 0 && pos < fileName.length() - 1) {
			return fileName.substring(0, pos);
		}
		return fileName;
	}
	
	public static void createFolderIfNotExist(String path) {
    	File folder = new File(path);
    	if(folder.isDirectory()==false)
    		folder.mkdirs();
    }
	
	public static void removeDeniedTemplateFilesByPath(String path) {
		File root = new File(path);
		
		List<String> fileList = generateFileList(root);
		
		for (String _file : fileList) {
			File file = new File(_file);
			
			DynamicBean bean = FileTypeUtil.getFileTypeByFile(file);
			
			if(bean.getPropertyAsBoolean("templateallow")!=true) {
				file.delete();
			}
		}
	}
	
	public static List<String> generateFileList(File node) {
		List<String> fileList = new ArrayList<String>();
		
		// add file only
		if (node.isFile()) {
			fileList.add(node.getPath());
		}
		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				fileList.addAll(generateFileList(new File(node, filename)));
			}
		}
		return fileList;
	}
	
	public static String clearPath(String path) {
		return path.replace("../", "");
	}
	
	public static void writeToFile(File file, String content) {
		if(file==null)
			return;
		fileWriter(file, content);
	}
	public static void writeToFile(String filepath, String content) {
		if(filepath==null || filepath.isEmpty())
			return;
		fileWriter(new File(filepath), content);
	}
	
	private static void fileWriter(File file, String content) {
		if(file==null)
			return;
		
		if(content==null)
			content = "";
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			if(!file.exists())
				file.createNewFile();
			
			bw.write(content);
			
			bw.close();
			fw.close();
 		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String encodeFileToBase64Binary(String fileName)
			throws IOException {
 
		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);
 
		return encodedString;
	}
 
	private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);
 
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
 
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
		}
 
	    is.close();
	    return bytes;
	}
	
	public static String getFilePathUnderSystemTempFolder(String fileName) {
		return new File(getSystemTempFolderPath(), fileName).getPath();
	}
	
	public static String getSystemTempFolderPath() {
		String dir = System.getProperty("java.io.tmpdir");
		return appendFileSeparator(dir);
	}
	
	public static String appendFileSeparator(String path) {
		return path + (path.substring(path.length() - 1).equals(File.separator)?"":File.separator);
	}
	
	public static File generateFileOnTemp(String fileName) {
		return new File(System.getProperty("java.io.tmpdir"), fileName);
	}
	
}
