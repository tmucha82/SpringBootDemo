package com.tm.example.kafka.sender;


import com.tm.example.avro.User;
import com.tm.example.kafka.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
    "app.topic.example=dummyValue",
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
@DirtiesContext
public class UserSenderTest {

  @Value("${app.topic.example}")
  private String topic;

  @ClassRule
  public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true);

  @Autowired
  private UserSender sender;

  private KafkaMessageListenerContainer<String, User> container;
  private BlockingQueue<ConsumerRecord<String, User>> records = new LinkedBlockingQueue<>();

  @Before
  public void setUp() {
    log.info("Sending to topic = {} test", topic);
    embeddedKafka.getEmbeddedKafka().addTopics(topic);
    // create a Kafka MessageListenerContainer
    container = createListenerContainer(topic, new StringDeserializer(), new JsonDeserializer<>(User.class));

    // setup a Kafka message listener
    container.setupMessageListener((MessageListener<String, User>) record -> {
      log.info("test-listener received message='{}'", record.toString());
      records.add(record);
    });

    // start the container and underlying message listener
    container.start();

    // wait until the container has the required number of assigned partitions
    ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
  }

  @After
  public void tearDown() {
    container.stop();
  }

  @Test
  public void test() throws InterruptedException {
    //given
    final User user = createSampleUser();

    //when
    sender.send(user);

    //then
    final ConsumerRecord<String, User> received = records.poll(10, TimeUnit.SECONDS);
    assertThat(received)
        .isNotNull()
        .has(key(null));
    assertThat(received.value()).isEqualTo(user);
  }

  private <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(final String topic, final Deserializer<K> keySerializer, final Deserializer<V> valueSerializer) {
    // set up the Kafka consumer properties
    final Map<String, Object> consumerProperties =
        KafkaTestUtils.consumerProps("sender", "false", embeddedKafka.getEmbeddedKafka());

    // create a Kafka consumer factory
    final DefaultKafkaConsumerFactory<K, V> consumerFactory =
        new DefaultKafkaConsumerFactory<>(consumerProperties, keySerializer, valueSerializer);

    return new KafkaMessageListenerContainer<>(consumerFactory, new ContainerProperties(topic));
  }

  private User createSampleUser() {
    return User.newBuilder()
        .setName("Tomasz")
        .setFavoriteNumber(13)
        .setFavoriteColor("orange")
        .build();
  }
}
