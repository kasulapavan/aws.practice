package com.example.aws.demo.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Message;
import com.example.aws.demo.configuration.JwtTokenUtils;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
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

import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

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



    private String YOUR_ACCESS_KEY = "";
    private String YOUR_SECRET_KEY = "";


    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon SES.
    final String FROM = "kasulapavan9@gmail.com";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    final String TO = "kasulapavan9@gmail.com";

    // The configuration set to use for this email. If you do not want to use a
    // configuration set, comment the following variable and the
    // .withConfigurationSetName(CONFIGSET); argument below.
    final String CONFIGSET = "ConfigSet";

    // The subject line for the email.
    final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    // The HTML body for the email.
    final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    // The email body for recipients with non-HTML email clients.
    final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";


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
                String token = jwtTokenUtils.getToken(appUser);
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


    private void sendLoginEmail(String recipientEmail, String token) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(YOUR_ACCESS_KEY, YOUR_SECRET_KEY);

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials) {
                            })
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.AP_SOUTH_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new com.amazonaws.services.simpleemail.model.Destination().withToAddresses(TO))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);
            // Comment or remove the next line if you are not using a
            // configuration set
//						.withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }


    }
}