package com.sunshineoxygen.inhome.ui.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sunshineoxygen.inhome.config.S3Client;
import com.sunshineoxygen.inhome.config.S3Manager;
import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.service.sms.ISmsService;
import com.sunshineoxygen.inhome.service.sms.SmsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

	@Autowired
	S3Client s3Client;

	@Value("${aws.accessKey}")
	private  String accessKey;

	@Value("${aws.secretKey}")
	private  String secretKey;


	@RequestMapping(value = "/hello",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> user() {

		DynamicBean jsonObject = new DynamicBean();
		jsonObject.addProperty("messages", "HelloController");
		jsonObject.addProperty("accessKey",accessKey);
		jsonObject.addProperty("secretKey",secretKey);
		jsonObject.addProperty("simulation",false);
		ISmsService service = SmsServiceFactory.get(jsonObject);
		service.send("+393509670387","This is your One Time Password:231232");
		return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
	}

}
