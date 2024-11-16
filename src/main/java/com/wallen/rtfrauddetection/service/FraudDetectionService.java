package com.wallen.rtfrauddetection.service;


import com.wallen.rtfrauddetection.data.Transaction;
import com.wallen.rtfrauddetection.rule.AmountBasedRule;
import com.wallen.rtfrauddetection.rule.FraudRule;
import com.wallen.rtfrauddetection.rule.SuspiciousAccountRule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 *  detect service
 *  1. init all rules
 *  2. detect
 */
@Service
@Slf4j
public class FraudDetectionService {

    @Value("${rule.thresholdAmount}")
    private long thresholdAmount;
    @Value("${rule.suspiciousAccounts}")
    private List<String> suspiciousAccounts;
    private final List<FraudRule> fraudRules;
    private final NotificationService notificationService;

    public FraudDetectionService(List<FraudRule> fraudRules, @Qualifier("compositeNotificationService") NotificationService notificationService) {
        this.fraudRules = fraudRules;
        this.notificationService = notificationService;
    }

    /**
     * init the rules when startup
     */
    @PostConstruct
    void init() {
        fraudRules.add(AmountBasedRule.builder().threshold(BigDecimal.valueOf(thresholdAmount)).build());
        fraudRules.add(SuspiciousAccountRule.builder().suspiciousAccounts(suspiciousAccounts).build());
        log.info("init rules success");
    }

    public void analyzeTransaction(Transaction transaction) {
        for (FraudRule rule : fraudRules) {
            if (rule.isFraudulent(transaction)) {
                notificationService.notify("Fraudulent transaction detected: " + transaction);
                logFraudulentTransaction(transaction);
                break;
            }
        }
    }

    /**
     * log the fraud transaction to cloud log
     * @param transaction  the trans will be logged
     */
    private void logFraudulentTransaction(Transaction transaction) {
        // TODO  use cloud log system
        log.info("Fraudulent transaction: {}",  transaction);
    }
}
