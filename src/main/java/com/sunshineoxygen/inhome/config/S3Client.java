package com.sunshineoxygen.inhome.config;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import com.sunshineoxygen.inhome.model.DynamicBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.io.File;
import java.net.URL;

@Slf4j
@Component
public class S3Client {

	@Autowired
	private S3Manager s3Manager;

	public String putObjectAndReturnUrl(String sourceObjectKey, File file, boolean isPublic) {
		try {
			PutObjectRequest request = new PutObjectRequest(s3Manager.getBucket(), sourceObjectKey, file);

			if (isPublic) {
				request.setCannedAcl(CannedAccessControlList.PublicRead);
			}

			s3Manager.getClient().putObject(request);
			return getObjectUrl(sourceObjectKey, isPublic);

		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return null;
		}
	}

	public  boolean putObject(String sourceObjectKey, File file, boolean isPublic) {
		try {
			PutObjectRequest request = new PutObjectRequest(s3Manager.getBucket(), sourceObjectKey, file);

			if (isPublic) {
				request.setCannedAcl(CannedAccessControlList.PublicRead);
			}

			s3Manager.getClient().putObject(request);
			return true;

		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return false;
		}
	}

	public  String getLocalPath(String url) {
		try {
			URL objectUrl = new URL(url);
			return objectUrl.getFile();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getObjectUrl(String key, boolean isPublic) {
		try {
			if (isPublic) {
				return s3Manager.getClient().getUrl(s3Manager.getBucket(), key).toExternalForm();
			} else {
				GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(s3Manager.getBucket(), key);
				return s3Manager.getClient().generatePresignedUrl(request).toExternalForm();
			}
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String moveOrPutObject(String sourceObjectKey, String targetObjectKey, File file, boolean isPublic) {
		boolean isSuccess = putObject(targetObjectKey, file, isPublic);
		if (isSuccess) {
			deleteObject(sourceObjectKey);
			return getObjectUrl(targetObjectKey, isPublic);
		} else {
			return null;
		}
	}

	public boolean moveObject(String sourceObjectKey, String targetObjectKey, boolean isPublic) {
		boolean isSuccess = putObject(targetObjectKey, new File(getLocalPath(getObjectUrl(sourceObjectKey, isPublic))), isPublic);
		if (isSuccess) {
			deleteObject(sourceObjectKey);
		}
		return isSuccess;
	}

	public void deleteObject(String objectKey) {
		try {
			s3Manager.getClient().deleteObject(new DeleteObjectRequest(s3Manager.getBucket(), objectKey));
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		}
	}

	public DynamicBean getObjectMetaData(String objectKey) {
		GetObjectMetadataRequest request = new GetObjectMetadataRequest(s3Manager.getBucket(), objectKey);
		AmazonS3 s3Client = s3Manager.getClient();
		ObjectMetadata data = s3Client.getObjectMetadata(request);
		DynamicBean bean = new DynamicBean(data.getRawMetadata());
		s3Client.shutdown();
		return bean;
	}


}
