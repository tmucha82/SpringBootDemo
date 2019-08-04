package com.tm.example.spring.kafka;

import com.google.common.collect.ImmutableList;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka
@SpringBootConfiguration
public class SimpleKafkaTest {

  private static final String TOPIC = "domain-events";

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Consumer<String, String> consumer;

  @Before
  public void setUp() {
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
    consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
    consumer.subscribe(ImmutableList.of(TOPIC));
    consumer.poll(0L);
  }

  @After
  public void tearDown() {
    consumer.close();
  }

  @Test
  public void test() {
    // Arrange
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();

    // Act
    producer.send(new ProducerRecord<>(TOPIC, "my-aggregate-id", "{\"event\":\"Test Event\"}"));
    producer.flush();

    // Assert
    ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
    assertThat(singleRecord).isNotNull();
    assertThat(singleRecord.key()).isEqualTo("my-aggregate-id");
    assertThat(singleRecord.value()).isEqualTo("{\"event\":\"Test Event\"}");
    
  }
}
