package com.tm.example.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class AvroJsonSerializer<T extends GenericRecord> implements Serializer<T> {

    private final AvroMapper mapper = new AvroMapper();

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
        try {
            return mapper.writerFor(t.getClass())
                    .with(new AvroSchema(t.getSchema()))
                    .writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            log.error("Got Exception: ", e);
            return null;
        }
    }
}