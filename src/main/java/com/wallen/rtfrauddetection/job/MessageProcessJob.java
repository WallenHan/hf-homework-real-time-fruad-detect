package com.wallen.rtfrauddetection.job;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallen.rtfrauddetection.data.Transaction;
import com.wallen.rtfrauddetection.service.FraudDetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@Slf4j
public class MessageProcessJob implements ApplicationRunner {

    private final FraudDetectionService fraudDetectionService;
    private final SqsClient sqsClient;
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;
    private final ExecutorService executorService;

    public MessageProcessJob(FraudDetectionService fraudDetectionService, SqsClient amazonSQSClient) {
        this.fraudDetectionService = fraudDetectionService;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new NamedThreadFactory("SqsConsumer"));
        this.sqsClient = amazonSQSClient;
    }


    @Override
    public void run(ApplicationArguments args)  {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .maxNumberOfMessages(10)
                            .build();
                    ReceiveMessageResponse messageResponse = sqsClient.receiveMessage(receiveMessageRequest);

                    messageResponse.messages().forEach(message -> {
                        log.info("Message received: {}", message.body());
                        ObjectMapper objectMapper = new ObjectMapper();
                        Transaction transaction;
                        try {
                            transaction = objectMapper.readValue(message.body(), Transaction.class);
                        } catch (JsonProcessingException e) {
                            log.error("illegal  transaction data: {}", message.body());
                            throw new RuntimeException(e);
                        }
                        if (transaction != null) {
                            fraudDetectionService.analyzeTransaction(transaction);
                            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                                    .queueUrl(queueUrl)
                                    .receiptHandle(message.receiptHandle())
                                    .build();
                           sqsClient.deleteMessage(deleteMessageRequest);
                        }
                    });
                }
            });
        }
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        if (event != null) {
            log.info("Application context closed. Shutting down the job executor...");
            executorService.shutdownNow();
            log.info("Executor service has been shut down.");
        }
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        public NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


}
