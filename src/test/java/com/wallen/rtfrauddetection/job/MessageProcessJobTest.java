package com.wallen.rtfrauddetection.job;

import com.wallen.rtfrauddetection.service.FraudDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class MessageProcessJobTest {

    @Mock
    private FraudDetectionService fraudDetectionService;
    @Mock
    private SqsClient sqsClient;
    @Mock
    private ApplicationArguments args;
    @Mock
    private ExecutorService executorService;
    @InjectMocks
    private MessageProcessJob messageProcessJob;

    @BeforeEach
    void setUp() {
        messageProcessJob = new MessageProcessJob(fraudDetectionService, sqsClient);
    }


    @Test
    void onApplicationEvent(CapturedOutput output) {

        ApplicationContext context = mock(ApplicationContext.class);
        ContextClosedEvent event = new ContextClosedEvent(context);
        messageProcessJob.onApplicationEvent(event);
        // 验证日志输出
        assertTrue(output.getOut().contains("Application context closed. Shutting down the job executor"));
        assertTrue(output.getOut().contains("Executor service has been shut down."));
    }
}