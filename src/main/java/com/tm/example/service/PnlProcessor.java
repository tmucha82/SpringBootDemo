package com.tm.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class PnlProcessor {

    private final Executor pnlThreadPool;

    public PnlProcessor(Executor pnlThreadPool) {
        this.pnlThreadPool = pnlThreadPool;
    }

    public void startProcessing() {
        CompletableFuture.runAsync(() -> {
            log.info("Start processing task ...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("Finished processing task ...");
        }, pnlThreadPool);
    }
}
