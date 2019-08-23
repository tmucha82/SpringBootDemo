package com.tm.example.service;

import com.tm.example.model.Paa;
import com.tm.example.model.Pnl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MeasureValueTest {

    @Test
    public void shouldMapToMeasureClass() {
        //when&then
        assertThat(MeasureValue.map(MeasureValue.PNL)).isEqualTo(Pnl.class);
        assertThat(MeasureValue.map(MeasureValue.RB)).isEqualTo(Paa.class);
        assertThat(MeasureValue.map(MeasureValue.RV)).isEqualTo(Paa.class);
    }
}
