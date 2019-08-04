package com.tm.example.avro;

import java.util.Deque;

class PathsPrinter {

    static String print(Deque<String> path) {
        return String.join(".", path);
    }

    static String print(Deque<String> path, String additionalSegment) {
    	if (path.isEmpty()) {
    		return additionalSegment;
    	}
        return print(path) + "." + additionalSegment;
    }

}