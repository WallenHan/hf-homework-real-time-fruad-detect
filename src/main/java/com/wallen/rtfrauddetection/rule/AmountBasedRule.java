package com.wallen.rtfrauddetection.rule;

import com.wallen.rtfrauddetection.data.Transaction;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public class AmountBasedRule implements FraudRule {

    private final BigDecimal  threshold;

    AmountBasedRule(BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean isFraudulent(Transaction transaction) {
        int result  = transaction.getAmount().compareTo(threshold);
        return result > 0;
    }
}
