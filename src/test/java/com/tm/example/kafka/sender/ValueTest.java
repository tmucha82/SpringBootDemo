package com.tm.example.kafka.sender;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@DirtiesContext
//@TestPropertySource(properties = {
//    "app.topic.example=dummyValue"
//})
@SpringBootTest(classes = ValueTest.class)
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {
    "app.topic.example=dummyValue"
})
public class ValueTest {

  @Value("${app.topic.example}")
  private String topic;

  @Before
  public void setUp() {
    log.warn("Value in setUp = {}", topic);
  }

  @Test
  public void test() {
    log.warn("Value in test = {}", topic);

  }
}
