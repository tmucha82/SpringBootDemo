package com.tm.example.avro;

public interface UnknownFieldListener {

	void onUnknownField(String name, Object value, String path);
}