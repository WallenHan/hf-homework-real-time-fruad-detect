package com.wallen.rtfrauddetection.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private BigDecimal amount;
    private String sender;
    private String receiver;

    @Override
    public String toString(){
        return "Transaction { amount=" + amount +", sender=\"" + sender + "\", receiver=\"" + receiver + "\" }";
    }
}
