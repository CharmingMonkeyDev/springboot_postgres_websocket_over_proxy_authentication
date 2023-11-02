package com.sunshineoxygen.inhome.service.sms;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sunshineoxygen.inhome.model.DynamicBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;


import java.util.HashMap;
import java.util.Map;

public class AwsSmsService implements ISmsService {

    private final SnsClient sns;

    private final String accessKey;
    private final String secretKey;

    public AwsSmsService(DynamicBean config) {
        //senderId = config.get("senderId");

        accessKey = config.get("accessKey");
        secretKey = config.get("secretKey");

        sns = SnsClient.builder()
                .credentialsProvider(new AwsCredentialsProvider() {
                    @Override
                    public AwsCredentials resolveCredentials() {
                        return new AwsCredentials() {
                            @Override
                            public String accessKeyId() {
                                return accessKey;
                            }

                            @Override
                            public String secretAccessKey() {
                                return secretKey;
                            }
                        };
                    }
                })
                .region(Region.EU_CENTRAL_1)
                .build();
    }

    @Override
    public void send(String phoneNumber, String message) {

        AWSCredentials awsCredentials =new BasicAWSCredentials(accessKey,secretKey);
        AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);


        //Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("AWS.SNS.SMS.SenderID",
                MessageAttributeValue.builder().stringValue("360inhome").dataType("String").build());
        messageAttributes.put("AWS.SNS.SMS.SMSType",
                MessageAttributeValue.builder().stringValue("Transactional").dataType("String").build());

        /*PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(messageAttributes));*/
        //System.out.println(result);
        sns.publish(builder -> builder
                .message(message)
                .phoneNumber(phoneNumber)
                .messageAttributes(messageAttributes));
    }

}
