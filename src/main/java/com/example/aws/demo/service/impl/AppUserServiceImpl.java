package com.example.aws.demo.service.impl;

import com.example.aws.demo.configuration.JwtTokenUtils;
import com.example.aws.demo.dto.AppUserDto;
import com.example.aws.demo.entity.AppUser;
import com.example.aws.demo.repository.AppUserRepo;
import com.example.aws.demo.service.AppUserService;
import com.example.aws.demo.utils.AppUserUtils;
import com.example.aws.demo.utils.ApplicationProperties;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Properties;
import java.util.Random;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private Environment environment;

    private SesClient sesClient;

    private String YOUR_ACCESS_KEY = "AKIA4JLRBRT76IUV3YWA";
    private String YOUR_SECRET_KEY = "rBRK0oFH6PXVZhOdNyqD2Z3lBo77tThhkh5JSZuN";

    public AppUserServiceImpl(SesClient sesClient) {
        this.sesClient = sesClient;
    }
//    @Autowired
//    private AppUserUtils appUserUtils;


    @Override
    public String signUp(AppUserDto appUserDto) {

        appUserRepo.save(AppUserUtils.dtoToEntity(appUserDto));
        return "successfully saved";
    }

    @Override
    public String loginIn(AppUserDto appUserDto) throws JOSEException {
        AppUser appUser = appUserRepo.findByEmail(appUserDto.getEmail());
        if (appUser != null) {
            if (appUser.getPassword().equals(appUserDto.getPassword())) {
                String token  = jwtTokenUtils.getToken(appUser);
                // Send email notification
                sendLoginEmail(appUser.getEmail(), token);
                System.out.println("hellolllllllllllllllllllllllll");

                return token;
            }

        }

        return null;
    }


    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(applicationProperties.getMailHost());
        mailSender.setPort(Integer.parseInt(applicationProperties.getMailPort()));

        mailSender.setUsername(applicationProperties.getMailUsername());
        mailSender.setPassword(applicationProperties.getMailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }
    public void YourServiceClass() {
        // Initialize SES client
        this.sesClient = SesClient.builder()
                .region(Region.AF_SOUTH_1)  // Change to your desired region
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(YOUR_ACCESS_KEY, YOUR_SECRET_KEY)))
                .build();
    }

    private void sendLoginEmail(String recipientEmail, String token) {
        // Create a request to send an email
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .source("kasulapavan9@gmail.com") // Sender's email address (must be verified in SES)
                .destination(Destination.builder().toAddresses("pavan.kasula@thrymr.net").build()) // Recipient's email address
                .message(Message.builder()
                        .subject(Content.builder().data("Login Successful").build())
                        .body(Body.builder().text(Content.builder().data("You have successfully logged in. Your token is: " + token).build()).build())
                        .build())
                .build();

        // Send the email
        try {
            sesClient.sendEmail(emailRequest);
            System.out.println("Login email sent successfully!");
        } catch (SesException e) {
            e.printStackTrace();
            System.err.println("Error sending login email: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Remember to close the SES client when your application shuts down
    public void closeSesClient() {
        sesClient.close();
    }
}