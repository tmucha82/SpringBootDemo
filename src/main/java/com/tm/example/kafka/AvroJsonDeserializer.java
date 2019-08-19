package com.tm.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.tm.example.avro.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class AvroJsonDeserializer<T extends GenericRecord> implements Deserializer<T> {

    private Class <T> type;
    private Schema schema;

    private final AvroMapper mapper = new AvroMapper();

    public AvroJsonDeserializer(Class<T> type) {
        this.type = type;
        this.schema = ReflectData.get().getSchema(type);
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
        try {
            return mapper.readerFor(type)
                    .with(new AvroSchema(schema))
                    .readValue(bytes);
        } catch (IOException e) {
            log.error("Got Exception: ", e);
            return null;
        }
    }
}