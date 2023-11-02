package com.sunshineoxygen.inhome.utils;


import com.sunshineoxygen.inhome.config.S3Client;
import com.sunshineoxygen.inhome.exception.ApplicationException;
import com.sunshineoxygen.inhome.model.CropInfo;
import com.sunshineoxygen.inhome.model.DynamicBean;
import org.imgscalr.Scalr;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UploadUtils {

	public static long _UPLOAD_MAX_FILE_SIZE = 100 * 1024 * 1024;

	public static String _BASE_FOLDER = "/test";
	public static String _SAFE_CONTENT_FOLDER = "/safe";

	public static String _UPLOAD_TEMP_FOLDER = "/uploads/[[SHOPNAME]]/t/";
	public static String _UPLOAD_DONE_FOLDER = "/uploads/[[SHOPNAME]]/1/";
	public static String _SYSTEM_TEMP_FOLDER = FileUtil.getSystemTempFolderPath();
	public static String _DYNAMIC_TEMPLATE_LABEL_FOLDER_ABSOLUTE_PATH ="";
	public static String _DYNAMIC_TEMPLATE_LABEL_FILENAME_PATTERN = "label-[[LOCALE_CODE]].json";

	public static List<String> ALLOWED_FILE_EXTENSIONS = new ArrayList<String>();
    static{
    	ALLOWED_FILE_EXTENSIONS.addAll(FileUtil._IMAGE_FILE_EXTENSIONS);
    	ALLOWED_FILE_EXTENSIONS.addAll(FileUtil._PDF_FILE_EXTENSIONS);
    }




    public static String getSystemTempFolder() {
    	return _SYSTEM_TEMP_FOLDER;
    }





	public static String getAbsolutePath(String relativePath) {
		String baseFolder = _BASE_FOLDER;
		return baseFolder.concat(relativePath);
	}

	public static String moveToUpload(S3Client s3Client,  String fromPath, String targetFolder) throws ApplicationException{
    	String uploadFolderName = null;
    	uploadFolderName = "Temp";


    	String targetFolderName = getFolderName(_UPLOAD_DONE_FOLDER, uploadFolderName);
    	String baseFolder = _BASE_FOLDER;

    	File sourceFile = new SafeFile(fromPath);
    	String relativeTargetPath = targetFolderName+targetFolder+"/"+sourceFile.getName();
    	File targetFile = new SafeFile(baseFolder.concat(relativeTargetPath));
		File parent = targetFile.getParentFile();
		if(!parent.exists() && !parent.mkdirs()){
		    throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		if(targetFile.exists()){
			targetFile.delete();
		}
		try{
			Path filePathUnderUploadFolder = moveFile(fromPath, targetFile.getPath());
			return s3Client.putObjectAndReturnUrl(relativeTargetPath, targetFile, true);
		}catch(Exception e){
			e.printStackTrace();
			return relativeTargetPath;
		}

    }

	public static Path moveFile(String sourceFilePath, String targetFilePath) throws IOException {
		Path sourcePath      = Paths.get(sourceFilePath);
		Path destinationPath = Paths.get(targetFilePath);

		try {
		    return Files.move(sourcePath, destinationPath,StandardCopyOption.REPLACE_EXISTING);
		} catch(FileAlreadyExistsException e) {
		    //destination file already exists
		} catch (IOException e) {
		    //something else went wrong
		    e.printStackTrace();
		}
		return null;


		//Path fileToMovePath = Files.createFile(Paths.get(sourceFilePath));
	    //Path targetPath = Paths.get(targetFilePath);

	    //return Files.move(fileToMovePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
	}




    public static String moveToUpload(S3Client s3Client,  String fromPath, String targetFolder, Boolean isTemporary)throws ApplicationException {
    	if(isTemporary){
    		targetFolder="temp/"+targetFolder;
    	}
    	return moveToUpload( s3Client,fromPath, targetFolder);
	}

    public static DynamicBean moveToUploadAndResourceBean( S3Client s3Client,String fromPath, String targetFolder, String objectType, boolean checkDeveloper)throws ApplicationException {
    	if(fromPath==null || !new File(fromPath).exists()) return null;
    	String newFilePath = moveToUpload(s3Client,fromPath, targetFolder, checkDeveloper );

    	DynamicBean bean = new DynamicBean();
    	bean.addProperty("fullsizeurl", newFilePath);
    	bean.addProperty("thumbnailsizeurl", newFilePath);
    	bean.addProperty("filesize", new File(newFilePath).length());
    	bean.addProperty("object", objectType);
    	return bean;

    }


    public static DynamicBean commitUpload(S3Client s3Client,String fileName, String targetFolder, String newFileName, boolean thumbnailIt, CropInfo cropInfo, Integer maxWidth) {

    	String uploadFolderName = "TEST";


    	String targetFolderName = getFolderName(_UPLOAD_DONE_FOLDER, uploadFolderName);
    	String tempFolderName = getFolderName(_UPLOAD_TEMP_FOLDER, uploadFolderName);
    	String baseFolder = _BASE_FOLDER;

    	DynamicBean returnBean = new DynamicBean();
    	try{
	    	File file = new SafeFile(baseFolder+tempFolderName+fileName);
	    	String fileExtension = FileUtil.getSuffix(fileName);

	    	if (file.exists()) {
	    		String relativeTargetPath = targetFolderName+targetFolder+"/"+newFileName+"."+fileExtension;
	    		String relativeTargetThumbnailPath = targetFolderName+targetFolder+"/"+newFileName+"_thumb."+fileExtension;
	    		File targetFile = new SafeFile(baseFolder.concat(relativeTargetPath));
	    		File parent = targetFile.getParentFile();
	    		if(!parent.exists() && !parent.mkdirs()){
	    		    throw new IllegalStateException("Couldn't create dir: " + parent);
	    		}

	    		if(targetFile.exists()){
	    			targetFile.delete();
	    		}

				long fileSize = file.length();
				returnBean.addProperty("filesize", fileSize);

	    		Path filePathUnderUploadFolder = moveFile(file.getPath(), targetFile.getPath());
	    		returnBean.addProperty("fullsizeurl", relativeTargetPath);
	    		if(maxWidth!=null && FileUtil.isResizableImageExtension(fileExtension)) {
    			    File newImageFile = new SafeFile(baseFolder.concat(relativeTargetPath));
    				BufferedImage im = ImageUtil.readImage(newImageFile);
                    if (im != null && im.getWidth()>maxWidth) {
                        ImageUtil.resizeImage(newImageFile, maxWidth);
                        returnBean.addProperty("fullsizeurl", relativeTargetPath);
                    }
	    		}
	    		if (cropInfo!=null){
	    			if (FileUtil.isResizableImageExtension(fileExtension)) {
	    				File newImageFile = new SafeFile(baseFolder.concat(relativeTargetPath));
	    				BufferedImage im = ImageUtil.readImage(newImageFile);
	                    BufferedImage cropImg = null;
	                    if(cropInfo.getX()==null || cropInfo.getY()==null){
	                    	 cropImg = Scalr.crop(im, cropInfo.getWidth(), cropInfo.getHeight());
	                    }else{
	                    	 cropImg = Scalr.crop(im, cropInfo.getX(), cropInfo.getY(), cropInfo.getWidth(), cropInfo.getHeight());
	                    }

	                    ImageUtil.saveImage(cropImg, newImageFile);
	                }
	    		}
	    		if (thumbnailIt) {
	    			if (FileUtil.isResizableImageExtension(fileExtension)) {
	    				File newImageFile = new SafeFile(baseFolder.concat(relativeTargetPath));
	    				File thumbnailImageFile = new SafeFile(baseFolder.concat(relativeTargetThumbnailPath));
	    				BufferedImage im = ImageUtil.readImage(newImageFile);
	                    if (im != null) {
	                        BufferedImage thumb = Scalr.resize(im, Scalr.Method.ULTRA_QUALITY, 175);
	                        ImageUtil.saveImage(thumb, thumbnailImageFile);
	                        returnBean.addProperty("thumbnailsizeurl", relativeTargetThumbnailPath);

	                    }
	    			} else if (FileUtil.isImageExtension(fileExtension)) {
	    				returnBean.addProperty("thumbnailsizeurl", relativeTargetPath);
	    			}
	    		}

	    		//after the operation is done, we can move image
	    		//1. try to moveobject
	    		try {
	    			String cdnUrl = s3Client.moveOrPutObject(tempFolderName.concat(fileName), relativeTargetPath, targetFile, true);
	    			returnBean.addProperty("fullsizeurl",  cdnUrl);
	    		}catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    		try {
		    		File thumbnailImageFile = new SafeFile(baseFolder.concat(relativeTargetThumbnailPath));
		    		if(thumbnailImageFile.exists()) {
		    			String thumbCdnUrl = s3Client.moveOrPutObject(relativeTargetThumbnailPath, relativeTargetThumbnailPath, thumbnailImageFile, true);
		    			if(thumbCdnUrl!=null) {
		    				 returnBean.addProperty("thumbnailsizeurl", thumbCdnUrl);
		    			}
		    		}
	    		}catch(Exception e) {
	    			e.printStackTrace();
	    		}
	    		//.putObject(tempFolderName.concat(fileName), new SafeFile(baseFolder.concat(relativeTargetPath)), true);

	    	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	return returnBean;

    }


    public static String getFolderName(String folder, String shopName){
    	return folder.replace("[[SHOPNAME]]", shopName);
    }

    public static void deleteUploadedFile( S3Client s3Client,String fileName) {
    	//if filename starts with http then it means the resource is on cdn, we need to find local path first
    	if(fileName.indexOf("http")==0) {
    		fileName = s3Client.getLocalPath(fileName);
    		if(fileName==null) {
    			return;
    		}
    	}
    	String shopName = "TEST";
    	String folderName = getFolderName(_UPLOAD_DONE_FOLDER, shopName);
    	if (fileName.indexOf(folderName)==0) {
    		File file = null;
    		try{
    			file = new SafeFile(_BASE_FOLDER+fileName);
    			if(file.exists()) {
    				file.delete();
    			}
    			s3Client.deleteObject(fileName);
    		}catch(Exception e){
    			e.printStackTrace();
    		}

    	}
    }



    public static String getUploadTempAbsolutePath(){
    	String path = _BASE_FOLDER.concat(getFolderName(_UPLOAD_TEMP_FOLDER, "TEST").replace("/", File.separator));
    	FileUtil.createFolderIfNotExist(path);
    	return path;
    }



    public static String getUploadTempFileRelativeUrl( String fileName) {
    	return getFolderName(_UPLOAD_TEMP_FOLDER, "TEST").concat(fileName);
    }





	public static String getLabelJSONFilePath(String localeCode) {
		String labelFileName = _DYNAMIC_TEMPLATE_LABEL_FILENAME_PATTERN.replace("[[LOCALE_CODE]]", localeCode);
		String labelFileAbsulutePath = _DYNAMIC_TEMPLATE_LABEL_FOLDER_ABSOLUTE_PATH.concat(labelFileName);
		return labelFileAbsulutePath;
	}




	public static String getSafeContentFolderAbsolutePath() {
		String targetFolderName = getFolderName(_UPLOAD_DONE_FOLDER, "TEST");
		File parent = new File(_SAFE_CONTENT_FOLDER);
		if(!parent.exists() && !parent.mkdirs()){
			throw new IllegalStateException("Couldn't create dir: " + parent);
		}
		return _SAFE_CONTENT_FOLDER + targetFolderName+ "content";
	}

}
