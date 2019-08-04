package com.tm.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class JsonDeserializer<T extends GenericRecord> implements Deserializer<T> {

    private Class <T> type;

    public JsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void close() {
        // No-op
    }

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // No-op
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        T obj = null;
        try {
            obj = mapper.readValue(bytes, type);
        } catch (Exception e) {

            log.error(e.getMessage());
        }
        return obj;
    }
}