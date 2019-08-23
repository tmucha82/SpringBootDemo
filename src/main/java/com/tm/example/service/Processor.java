package com.tm.example.service;

import com.tm.example.db.DbMeasureResult;
import com.tm.example.model.BatchNotification;
import com.tm.example.model.Measure;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Processor<T extends Measure> {

    String getProcedureName();
    Function<DbMeasureResult, T> getRowExtractor();
    CompletableFuture<T> sendData(T data);
    CompletableFuture<T> sendNotification(BatchNotification notification);
}
