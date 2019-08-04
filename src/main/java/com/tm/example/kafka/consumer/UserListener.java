package com.tm.example.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserListener<T> {

//  @KafkaListener(topics = "${app.topic.example}")
  public void receive(@Payload T data, @Headers MessageHeaders headers) {
    log.info("received data='{}'", data);

    headers.keySet().forEach(key -> log.info("{}: {}", key, headers.get(key)));
  }

}