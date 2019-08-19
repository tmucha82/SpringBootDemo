package com.tm.example.kafka;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.FormatFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.tm.example.avro.User;
import org.junit.Test;

import java.io.IOException;

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

  @Test
  public void shouldSerializeAndDeserializeWithJacksonDataFormats() throws IOException {
    //given
    final User user = createSampleUser();

    //when
    final AvroMapper mapper = new AvroMapper();
    final byte[] bytes = mapper.writerFor(User.class)
            .with(new AvroSchema(user.getSchema()))
            .writeValueAsBytes(user);

    //then
    assertThat(bytes)
            .isNotNull()
            .isNotEmpty();
    final User result = mapper.readerFor(User.class).with(new AvroSchema(user.getSchema())).readValue(bytes);
    assertThat(result)
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