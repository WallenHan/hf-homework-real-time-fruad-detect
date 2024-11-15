package com.wallen.rtfrauddetection.job;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@Slf4j
public class MessageProcessJob implements ApplicationRunner {

    private final FraudDetectionService fraudDetectionService;
    private final AmazonSQS amazonSQS;
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;
    private final ExecutorService executorService;

    public MessageProcessJob(FraudDetectionService fraudDetectionService, AmazonSQS amazonSQS) {
        this.fraudDetectionService = fraudDetectionService;
        this.amazonSQS = amazonSQS;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                new NamedThreadFactory("SqsConsumer"));
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                            .withMaxNumberOfMessages(10)
                            .withWaitTimeSeconds(20);

                    ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(receiveMessageRequest);

                    receiveMessageResult.getMessages().forEach(message -> {
                        log.info("Message received: {}", message.getBody());
                        ObjectMapper objectMapper = new ObjectMapper();
                        Transaction transaction = null;
                        try {
                            transaction = objectMapper.readValue(message.getBody(), Transaction.class);
                        } catch (JsonProcessingException e) {
                            log.error("illegal  transaction data: {}", message.getBody());
                            throw new RuntimeException(e);
                        }
                        if (transaction != null) {
                            fraudDetectionService.analyzeTransaction(transaction);
                            amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
                        }
                    });
                }
            });
        }
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event) {
        executorService.shutdownNow();
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
