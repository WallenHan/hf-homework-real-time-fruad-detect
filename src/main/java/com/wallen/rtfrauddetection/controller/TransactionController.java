package com.wallen.rtfrauddetection.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.wallen.rtfrauddetection.data.ApiResponse;
import com.wallen.rtfrauddetection.data.Transaction;
import com.wallen.rtfrauddetection.util.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/delivery")
public class TransactionController {


     private final AmazonSQS sqs;
     @Value("${aws.sqs.queueUrl}")
     private String queueUrl;

     public TransactionController(AmazonSQS sqs) {
          this.sqs = sqs;
     }

     @PostMapping("/transaction")
     public ResponseEntity<ApiResponse<?>> deliveryTransaction(@RequestBody Transaction transaction) {

          SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
                  .withMessageBody(JSONUtil.toJsonString(transaction))
                  .withMessageGroupId("unchecked-transaction")
                  .withMessageDeduplicationId(UUID.randomUUID().toString());
          SendMessageResult result = sqs.sendMessage(sendMessageRequest);
          return ResponseEntity.ok(ApiResponse.success(result.getSdkResponseMetadata()));
     }

}
