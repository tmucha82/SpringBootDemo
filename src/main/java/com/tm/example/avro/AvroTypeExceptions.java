package com.tm.example.avro;

import org.apache.avro.AvroTypeException;

import java.util.Deque;

class AvroTypeExceptions {
    static AvroTypeException enumException(Deque<String> fieldPath, String expectedSymbols) {
        return new AvroTypeException("Field " +
            PathsPrinter.print(fieldPath) +
            " is expected to be of enum type and be one of " +
            expectedSymbols);
    }

    static AvroTypeException unionException(String fieldName, String expectedTypes, Deque<String> offendingPath) {
        return new AvroTypeException("Could not evaluate union, field " +
            fieldName +
            " is expected to be one of these: " +
            expectedTypes +
            ". If this is a complex type, check if offending field: " +
            PathsPrinter.print(offendingPath) +
            " adheres to schema.");
    }

    static AvroTypeException typeException(Deque<String> fieldPath, String expectedType) {
        return new AvroTypeException("Field " +
            PathsPrinter.print(fieldPath) +
            " is expected to be type: " +
            expectedType);
    }
}