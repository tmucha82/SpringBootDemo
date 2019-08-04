package com.tm.example.kafka.sender;

import com.tm.example.avro.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSender {

  private final KafkaTemplate<String, User> kafkaTemplate;

  @Value("${app.topic.example}")
  private String topic;

  @Autowired
  public UserSender(KafkaTemplate<String, User> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  void send(User data) {
    log.info("sending data='{}' to topic='{}'", data, topic);

    final Message<User> message = MessageBuilder
        .withPayload(data)
        .setHeader(KafkaHeaders.TOPIC, topic)
        .build();

    kafkaTemplate.send(message);
  }
}