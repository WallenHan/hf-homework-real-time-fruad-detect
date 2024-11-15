package com.wallen.rtfrauddetection;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class RtFraudDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtFraudDetectionApplication.class, args);
    }
}
