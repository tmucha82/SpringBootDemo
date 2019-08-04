package com.tm.example.kafka;


import com.tm.example.avro.User;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSerdeTest {

  private JsonSerializer<User> jsonSerializer = new JsonSerializer<>();
  private JsonDeserializer<User> jsonDeserializer = new JsonDeserializer<>(User.class);


  @Test
  public void shouldSerializeAndDeserializeAvroObject() {
    //given
    final User user = createSampleUser();

    //when
    final byte[] bytes = jsonSerializer.serialize(null, user);

    //then
    assertThat(bytes)
        .isNotNull()
        .isNotEmpty();
    assertThat(jsonDeserializer.deserialize(null, bytes))
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