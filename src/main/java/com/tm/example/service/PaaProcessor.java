package com.tm.example.service;

import com.tm.example.db.DbMeasureResult;
import com.tm.example.model.BatchNotification;
import com.tm.example.model.Paa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@Service
public class PaaProcessor implements Processor<Paa> {

    @Override
    public String getProcedureName() {
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
