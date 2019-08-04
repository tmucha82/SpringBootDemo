package com.tm.example.avro;


import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTest {

  @Test
  public void shouldCreteProperUserENtity() {
    //when
    final User user = createSampleUser();

    //then
    assertThat(user).isNotNull();
  }

  @Test
  public void shouldSerializeAndDeserializeUser() throws IOException {
    //given
    final User user = createSampleUser();
    final DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.getClassSchema());

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    JsonEncoder encoder = EncoderFactory.get().jsonEncoder(User.getClassSchema(), out);
    userDatumWriter.setSchema(User.getClassSchema());

    //when
    userDatumWriter.write(user, encoder);

    encoder.flush();
    out.close();

    //then
    final String expectedJson = String
        .format("{\"name\":\"%s\",\"favorite_number\":{\"int\":%s},\"favorite_color\":{\"string\":\"%s\"}}",
            user.getName(), user.getFavoriteNumber(), user.getFavoriteColor());
    assertThat(out.toString("UTF-8")).isEqualTo(expectedJson);
  }

  @Test
  public void shouldDeserializeAndDeserializeUser() throws IOException {
    //given
    final User user = createSampleUser();
    final String json = String
        .format("{\"name\":\"%s\",\"favorite_number\":{\"int\":%s},\"favorite_color\":{\"string\":\"%s\"}}",
            user.getName(), user.getFavoriteNumber(), user.getFavoriteColor());

    final DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.getClassSchema());
    final JsonDecoder jsonDecoder = DecoderFactory.get().jsonDecoder(User.getClassSchema(), json);
    final User result = new User();

    //when
    userDatumReader.read(result, jsonDecoder);

    //then
    assertThat(result).isEqualTo(user);
  }

  private User createSampleUser() {
    return User.newBuilder()
        .setName("Tomasz")
        .setFavoriteNumber(13)
        .setFavoriteColor("orange")
        .build();
  }
}