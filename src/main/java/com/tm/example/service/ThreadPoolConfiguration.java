package com.tm.example.service;

import com.zaxxer.hikari.util.UtilityElf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfiguration {

    private final static int CORE_POOL_SIZE = 1;
    private final static int MAX_POOL_SIZE = 2;
    private final static long KEEP_ALIVE_TIME = 1;

    @Bean
    public Executor pnlThreadPool() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
