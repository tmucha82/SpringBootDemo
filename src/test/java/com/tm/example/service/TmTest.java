package com.tm.example.service;

import com.tm.example.model.Measure;
import com.tm.example.model.Pnl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PnlProcessor.class, PaaProcessor.class})
public class TmTest {

    @Autowired
    private List<Processor<? extends Measure>> processors;

    @Test
    public void name() {
        Class<? extends Measure> measure = Pnl.class;
        Processor<? extends Measure> pnlProcessor = processors.stream()
                .filter(processor -> processor.getClass()
                        .getGenericInterfaces()[0].getTypeName().contains(measure.getCanonicalName()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        log.info("pnlProcessor = {}", pnlProcessor);
    }
}
