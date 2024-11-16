package com.wallen.rtfrauddetection.controller;

import com.wallen.rtfrauddetection.data.ApiResponse;
import com.wallen.rtfrauddetection.data.Transaction;
import com.wallen.rtfrauddetection.util.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.UUID;

@RestController
@RequestMapping("/v1/delivery")
public class TransactionController {


     private final SqsClient sqs;
     @Value("${aws.sqs.queueUrl}")
     private String queueUrl;

     public TransactionController(SqsClient sqs) {
          this.sqs = sqs;
     }

     @PostMapping("/transaction")
     public ResponseEntity<ApiResponse<?>> deliveryTransaction(@RequestBody Transaction transaction) {

          SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                  .queueUrl(queueUrl)
                  .messageBody(JSONUtil.toJsonString(transaction))
                  .messageGroupId("unchecked-transaction")
                  .messageDeduplicationId(UUID.randomUUID().toString())
                  .build();
          SendMessageResponse response = sqs.sendMessage(sendMessageRequest);
          return ResponseEntity.ok(ApiResponse.success(response.messageId()));
     }

}
