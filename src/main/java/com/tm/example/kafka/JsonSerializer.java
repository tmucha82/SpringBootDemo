package com.tm.example.kafka;

import com.tm.example.avro.JsonAvroConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class JsonSerializer<T extends GenericRecord> implements Serializer<T> {

    private JsonAvroConverter converter = new JsonAvroConverter();


    @Override
    public void close() {
        // No-op
    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // No-op
    }

    @Override
    public byte[] serialize(String s, T t) {

        byte[] retVal = null;
        try {
            retVal = converter.convertToJson(t);
        } catch (Exception e) {
            log.error("Got Exception: ", e);
        }
        return retVal;
    }
}