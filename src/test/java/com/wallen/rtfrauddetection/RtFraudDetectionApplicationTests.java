package com.wallen.rtfrauddetection;

import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;
import com.wallen.rtfrauddetection.config.AWSResConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.event.annotation.PrepareTestInstance;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RtFraudDetectionApplicationTests {

    @Autowired
    private ApplicationContext context;


    @Test
    void contextLoads() {
        assertNotNull(context, "context should be loaded");
    }

    @Test
    void testBeansPresence() {
        assertNotNull(context.getBean(AWSResConfig.class), "AWSConfig bean should be loaded");
        assertNotNull(context.getBean(SqsClient.class), "SqsClient bean should be loaded");
        assertNotNull(context.getBean(AwsBasicCredentials.class), "BasicCredential bean should be loaded");
    }

}
