package com.wallen.rtfrauddetection.rule;

import com.wallen.rtfrauddetection.data.Transaction;

public interface FraudRule {
    boolean isFraudulent(Transaction transaction);
}
