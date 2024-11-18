package com.wallen.rtfrauddetection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AWSResConfig {
    @Value("${aws.res.apiKey}")
    private String accessKey;

    @Value("${aws.res.apiSecret}")
    private String secretKey;

    @Value("${aws.res.region}")
    private String region;


    /**
     * init  basicAWSCredential
     *
     * @return basicAWSCredentials AWSCredentials
     */
    @Bean
    public AwsBasicCredentials credentialForAWS() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }


    /**
     * init the sqs client
     *
     * @return client sqsClient
     */
    @Bean
    public SqsClient amazonSQSClient(AwsBasicCredentials credentialForAWS) {
        return SqsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentialForAWS))
                .region(Region.of(region))
                .build();
    }

}
