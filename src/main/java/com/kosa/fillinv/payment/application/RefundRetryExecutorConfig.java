package com.kosa.fillinv.payment.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class RefundRetryExecutorConfig {

    @Bean
    public Executor refundRetryExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
