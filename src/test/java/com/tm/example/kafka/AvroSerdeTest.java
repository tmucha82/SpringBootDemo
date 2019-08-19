package com.tm.example.kafka;

import com.tm.example.avro.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AvroSerdeTest {

  private AvroSerializer<User> avroSerializer = new AvroSerializer<>();
  private AvroDeserializer<User> avroDeserializer = new AvroDeserializer<>(User.class);

  @Test
  public void shouldSerializeAndDeserializeAvroObject() {
    //given
    final User user = createSampleUser();

    //when
    final byte[] bytes = avroSerializer.serialize(null, user);

    //then
    assertThat(bytes)
        .isNotNull()
        .isNotEmpty();
    assertThat(avroDeserializer.deserialize(null, bytes))
        .isEqualTo(user);

  }

  private User createSampleUser() {
    return User.newBuilder()
        .setName("Tomasz")
        .setFavoriteNumber(13)
        .setFavoriteColor("orange")
        .build();
  }
}