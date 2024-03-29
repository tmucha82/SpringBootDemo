package com.tm.example.service;

import com.tm.example.db.DbMeasureResult;
import com.tm.example.model.BatchNotification;
import com.tm.example.model.Pnl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

@Slf4j
@Service
public class PnlProcessor implements Processor<Pnl> {

    @Override
    public String getProcedureName() {
        return "FETCH_PNL";
    }

    @Override
    public Function<DbMeasureResult, Pnl> getRowExtractor() {
        return dbMeasureResult -> Pnl.builder()
                .build();
    }

    @Override
    public CompletableFuture<Pnl> sendData(Pnl data) {
        return null;
    }

    @Override
    public CompletableFuture<Pnl> sendNotification(BatchNotification notification) {
        return null;
    }
}
