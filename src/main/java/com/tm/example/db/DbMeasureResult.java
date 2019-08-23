package com.tm.example.db;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Value
@ToString
public class DbMeasureResult {
  private final Integer id;
  private final String name;
  private final String title;
  private final Integer rowCounter;
}
