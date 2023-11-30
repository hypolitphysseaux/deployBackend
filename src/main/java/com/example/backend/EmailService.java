package com.example.backend;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

@Service
public class EmailService {
        //Attribute
        private final JavaMailSender javaMailSender;
        //Constructor
        public EmailService(JavaMailSender javaMailSender) {
            this.javaMailSender = javaMailSender;
        }

        public void sendRegistrationEmail(String to, String username) throws IOException {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            try {
                helper.setTo(to);
                helper.setSubject("Ďakujeme za registráciu do našej knižnice!");
                helper.setFrom("tsikt.library@seznam.cz");
                ClassPathResource resource = new ClassPathResource("email_library.html");
                byte[] fileContent = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String htmlContent = new String(fileContent);

                // You can also replace dynamic content in the HTML if needed
                htmlContent = htmlContent.replace("[Username]", username);

                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                // Handle exception appropriately
                e.printStackTrace();
                System.out.println(e);
            }
        }
}
