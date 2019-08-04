package com.tm.example.spring.kafka;

import com.google.common.collect.ImmutableList;
import com.tm.example.avro.User;
import com.tm.example.kafka.JsonDeserializer;
import com.tm.example.kafka.JsonSerializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka
public class SimpleJsonKafkaTest {

  private static final String TOPIC = "domain-events";

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private Consumer<String, User> consumer;

  @Before
  public void setUp() {
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
    consumer = new DefaultKafkaConsumerFactory<>(configs,
        new StringDeserializer(),
        new JsonDeserializer<>(User.class)).createConsumer();
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
    Producer<String, User> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<User>()).createProducer();

    // Act
    final User sampleUser = createSampleUser();
    producer.send(new ProducerRecord<>(TOPIC, "my-aggregate-id", sampleUser));
    producer.flush();

    // Assert
    ConsumerRecord<String, User> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
    assertThat(singleRecord).isNotNull();
    assertThat(singleRecord.key()).isEqualTo("my-aggregate-id");
    assertThat(singleRecord.value()).isEqualTo(sampleUser);
  }

  private User createSampleUser() {
    return User.newBuilder()
        .setName("Tomasz")
        .setFavoriteNumber(13)
        .setFavoriteColor("orange")
        .build();
  }
}
