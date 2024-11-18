package com.wallen.rtfrauddetection.service;

import com.wallen.rtfrauddetection.rule.AmountBasedRule;
import com.wallen.rtfrauddetection.rule.FraudRule;
import com.wallen.rtfrauddetection.rule.SuspiciousAccountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@TestPropertySource(properties = {"rule.thresholdAmount=1000", "rule.suspiciousAccounts=xbc,xxxsscv"})
class FraudDetectionServiceTests {
    @Mock
    private List<FraudRule> fraudRules;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private FraudDetectionService fraudDetectionService;


    @BeforeEach
    void setUp() {
        fraudDetectionService = new FraudDetectionService(fraudRules, notificationService);
        fraudDetectionService.init();
    }

    @Test
    void testInit() {
        verify(fraudRules, times(1)).add(any(AmountBasedRule.class));
        verify(fraudRules, times(1)).add(any(SuspiciousAccountRule.class));
    }

}