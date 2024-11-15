package com.wallen.rtfrauddetection.rule;

import com.wallen.rtfrauddetection.data.Transaction;
import lombok.Builder;

import java.util.List;

@Builder
public class SuspiciousAccountRule implements FraudRule {

    private final List<String> suspiciousAccounts;
    public SuspiciousAccountRule(List<String> suspiciousAccounts) {
        this.suspiciousAccounts = suspiciousAccounts;
    }

    @Override
    public boolean isFraudulent(Transaction transaction) {
        boolean isSuspiciousSender = suspiciousAccounts.contains(transaction.getSender());
        boolean isSuspiciousReceiver = suspiciousAccounts.contains(transaction.getReceiver());
        return isSuspiciousSender || isSuspiciousReceiver;
    }
}
