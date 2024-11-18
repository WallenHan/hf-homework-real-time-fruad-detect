package com.wallen.rtfrauddetection.rule;

import com.wallen.rtfrauddetection.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuspiciousAccountRuleTest {



    @Test
    void isFraudulent() {
        List<String> balckList = List.of("aaa", "bbb", "ccc", "ddd", "eee", "fff");
        SuspiciousAccountRule suspiciousAccountRule = SuspiciousAccountRule.builder()
                .suspiciousAccounts(balckList)
                .build();

        Transaction transactionA = Transaction.builder()
                .sender("aa")
                .receiver("bbb")
                .amount(BigDecimal.valueOf(200))
                .build();
        assertTrue(suspiciousAccountRule.isFraudulent(transactionA));
        Transaction transactionB = Transaction.builder()
                .sender("aaa")
                .receiver("bb")
                .amount(BigDecimal.valueOf(200))
                .build();
        assertFalse(suspiciousAccountRule.isFraudulent(transactionB));
        Transaction transactionC = Transaction.builder()
                .sender("aaa")
                .receiver("bbb")
                .amount(BigDecimal.valueOf(200))
                .build();
        assertFalse(suspiciousAccountRule.isFraudulent(transactionC));
        Transaction transactionD = Transaction.builder()
                .sender("aa")
                .receiver("bb")
                .amount(BigDecimal.valueOf(200))
                .build();
        assertFalse(suspiciousAccountRule.isFraudulent(transactionD));

    }
}