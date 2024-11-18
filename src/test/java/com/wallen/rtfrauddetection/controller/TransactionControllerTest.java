package com.wallen.rtfrauddetection.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import software.amazon.awssdk.services.sqs.SqsClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionControllerTest {


    private MockMvc mvc;
    @Autowired
    private SqsClient sqsClient;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new TransactionController(sqsClient)).build();
    }

    @Test
    void deliveryTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/v1/delivery/transaction")
                .content("{\"amount\": 3222222,\"sender\": \"mountA\",\"receiver\": \"dnxdhhs-m\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
