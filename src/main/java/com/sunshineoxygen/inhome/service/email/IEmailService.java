package com.sunshineoxygen.inhome.service.email;


import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.User;

public interface IEmailService {

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    public void sendEmailFromTemplate(DynamicBean context, String templateName, String titleKey);
}
