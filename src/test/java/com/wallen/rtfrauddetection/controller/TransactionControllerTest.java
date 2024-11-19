package com.wallen.rtfrauddetection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallen.rtfrauddetection.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class TransactionControllerTest {


    private MockMvc mvc;

    @Autowired
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void deliveryTransaction() throws Exception {
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(3222222))
                .sender("mountA")
                .receiver("mountB")
                .build();
        mvc.perform(MockMvcRequestBuilders.post("/v1/delivery/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transaction)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
