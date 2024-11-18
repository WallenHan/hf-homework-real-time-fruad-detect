package com.wallen.rtfrauddetection.rule;

import com.wallen.rtfrauddetection.data.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountBasedRuleTest {


    /***
     * 以金额为准的规则，大于该阈值的都算涉嫌诈骗
     */
    @Test
    void isFraudulent() {
        AmountBasedRule rule = AmountBasedRule.builder()
                .threshold(BigDecimal.valueOf(199999))
                .build();
        Transaction transactionA = Transaction.builder()
                .amount(BigDecimal.valueOf(200000))
                .receiver("xxxxx")
                .sender("abcss")
                .build();
        assertTrue(rule.isFraudulent(transactionA));
        Transaction transactionB = Transaction.builder()
                .amount(BigDecimal.valueOf(199998))
                .receiver("xxxxx")
                .sender("abcss")
                .build();
        assertFalse(rule.isFraudulent(transactionB));
    }
}