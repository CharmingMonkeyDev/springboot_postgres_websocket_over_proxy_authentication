package com.sunshineoxygen.inhome.service.sms;


import com.sunshineoxygen.inhome.model.DynamicBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SmsServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceFactory.class);

    public static ISmsService get(DynamicBean bean) {
        if (bean.getPropertyAsBoolean("simulation")) {
            return (phoneNumber, message) ->
                    logger.warn(String.format("***** SIMULATION MODE ***** Would send SMS to %s with text: %s", phoneNumber, message));
        } else {
            AwsSmsService awsSmsService =  new AwsSmsService(bean);
            return  awsSmsService;
        }
    }
}
