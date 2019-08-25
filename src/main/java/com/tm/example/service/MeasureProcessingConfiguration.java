package com.tm.example.service;

import com.tm.example.model.Measure;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Configuration
public class MeasureProcessingConfiguration {

    @Autowired
    private List<Processor<? extends Measure>> processors;

    @Bean
    public Function<TriggerMessage, Processor<? extends Measure>> processorSupplier() {
        return triggerMessage -> {
            Class<? extends Measure> measure = MeasureValue.map(triggerMessage.getMeasureValue());
            return processors.stream()
                    .filter(processor -> processor.getClass()
                            .getGenericInterfaces()[0].getTypeName().contains(measure.getCanonicalName()))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        };
    }

}
