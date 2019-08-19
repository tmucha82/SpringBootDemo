package com.tm.example.controller;

import com.tm.example.service.PnlProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import scala.concurrent.ExecutionContextExecutorService;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final KafkaTemplate<String, ?> kafkaTemplate;
    private final PnlProcessor pnlProcessor;

    public GreetingController(KafkaTemplate<String, ?> kafkaTemplate, PnlProcessor pnlProcessor) {
        this.kafkaTemplate = kafkaTemplate;
        this.pnlProcessor = pnlProcessor;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) throws InterruptedException {
        pnlProcessor.startProcessing();
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}