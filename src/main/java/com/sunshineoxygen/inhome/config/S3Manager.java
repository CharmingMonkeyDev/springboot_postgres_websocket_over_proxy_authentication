package com.sunshineoxygen.inhome.config;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class S3Manager {

	@Value("${aws.accessKey}")
	private  String accessKey;

	@Value("${aws.secretKey}")
	private  String secretKey;

	@Value("${aws.bucket}")
	private   String bucket;

	public  AmazonS3 getClient() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.EU_WEST_3)
				.build();

		return s3Client;
	}
	public  String getBucket() {
		return bucket;
	}

}
