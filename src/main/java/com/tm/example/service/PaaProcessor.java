package com.tm.example.service;

import com.tm.example.db.DbMeasureResult;
import com.tm.example.model.BatchNotification;
import com.tm.example.model.Measure;
import com.tm.example.model.Paa;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Setter
@Slf4j
@Service
@ConfigurationProperties(prefix = "app")
public class PaaProcessor implements Processor<Paa> {

    private Map<String, MeasureProperties> measures;

    @Override
    public String getProcedureName() {
        System.out.println("measures = " + measures);
        return "FETCH_PNL";
    }

    @Override
    public Function<DbMeasureResult, Paa> getRowExtractor() {
        return dbMeasureResult -> Paa.builder()
                .build();
    }

    @Override
    public CompletableFuture<Paa> sendData(Paa data) {
        return null;
    }

    @Override
    public CompletableFuture<Paa> sendNotification(BatchNotification notification) {
        return null;
    }
}
