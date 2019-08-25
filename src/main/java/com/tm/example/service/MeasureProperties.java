package com.tm.example.service;


import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@ToString
@Setter
public class MeasureProperties {
    private String dbProcedure;
    private Map<String, PublisherProperties> publishers;
}
