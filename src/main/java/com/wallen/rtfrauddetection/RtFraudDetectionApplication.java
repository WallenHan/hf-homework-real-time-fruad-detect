package com.wallen.rtfrauddetection;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableEncryptableProperties
public class RtFraudDetectionApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .environment(new StandardEncryptableEnvironment())
                .sources(RtFraudDetectionApplication.class)
                .run(args);
    }
}
