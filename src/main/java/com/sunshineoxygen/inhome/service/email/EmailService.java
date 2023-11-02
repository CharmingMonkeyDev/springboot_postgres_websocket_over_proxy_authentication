package com.sunshineoxygen.inhome.service.email;

import com.sunshineoxygen.inhome.model.DynamicBean;
import com.sunshineoxygen.inhome.model.User;
import com.sunshineoxygen.inhome.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class EmailService implements IEmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public EmailService(
            JavaMailSender javaMailSender,
            MessageSource messageSource,
            SpringTemplateEngine templateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    @Getter
    @Setter
    private String mailfrom;

    @Value("${app.base-url}")
    @Getter
    @Setter
    private String baseUrl;

    @Override
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(this.getMailfrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Override
    @Async
    public void sendEmailFromTemplate(DynamicBean contextInfo, String templateName, String titleKey) {
        User user = null;
        Locale locale =null;
        Context context = null;
        if(contextInfo != null){
            if(contextInfo.getPropertyAsObject("user") != null){
                user = (User) contextInfo.getPropertyAsObject("user");
                locale= Locale.forLanguageTag(user.getLangKey());
                context = new Context(locale);
            }else{
                //check locale info
                if(StringUtils.isNotEmpty(contextInfo.get("locale"))){
                    locale= Locale.forLanguageTag(contextInfo.getPropertyAsString("locale"));
                    context = new Context(locale);
                }
            }
        }

        if(context != null) {
            context.setVariables(contextInfo.getProperties());
            context.setVariable(Constants.BASE_URL, this.getBaseUrl());
            String content = templateEngine.process(templateName, context);
            String subject = messageSource.getMessage(titleKey, null, locale);
            sendEmail(user.getEmail(), subject, content, false, true);
        }else{
            log.warn("Email could not be sent for this template '{}'", templateName);
        }
    }
}
