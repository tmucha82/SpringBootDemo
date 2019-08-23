package com.tm.example.service;

import com.tm.example.db.DbMeasureResult;
import com.tm.example.model.Measure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class MeasureProcessing {

    @Autowired
    Function<TriggerMessage, Processor<? extends Measure>> processorSupplier;

    @Autowired
    Function<ResultSetExtractor<? extends Measure>, SimpleJdbcCall> simpleJdbcCallSupplier;

    @Autowired
    public Function<ResultSet, DbMeasureResult> processingMeasure;

    public void startProcessing(final TriggerMessage triggerMessage) {
        log.info("Start processing with trigger = {}", triggerMessage);
        final Processor<? extends Measure> processor = processorSupplier.apply(triggerMessage);
        final Map<String, Object> map = simpleJdbcCallSupplier
                .apply(processPnlExtractor(processor))
                .withProcedureName(processor.getProcedureName())
                .execute(new MapSqlParameterSource());
        log.info("Processor = {}", processor);
        log.info("Map = {}", map);
    }

    private ResultSetExtractor<? extends Measure> processPnlExtractor(Processor<? extends Measure> processor) {
        return rs -> {
            Measure measure = null;
            while(rs.next()) {
                measure = processor.getRowExtractor().apply(processingMeasure.apply(rs));

                //TODO - processing
            }
            return measure;
        };
    }

}
