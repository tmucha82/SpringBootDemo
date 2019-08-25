package com.tm.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("application.yml")
@DirtiesContext
public class MeasureProcessingTest {

    @Autowired
    private MeasureProcessing measureProcessing;

    @Test
    public void shouldChoosePnlProcessor() {
        //given
        final TriggerMessage triggerMessage = TriggerMessage.builder()
                .measureValue(MeasureValue.PNL)
                .build();

        //when
        measureProcessing.startProcessing(triggerMessage);
    }

    @Test
    public void shouldChoosePaaProcessor() {
        //given
        final TriggerMessage triggerMessage = TriggerMessage.builder()
                .measureValue(MeasureValue.RB)
                .build();

        //when
        measureProcessing.startProcessing(triggerMessage);
    }
}