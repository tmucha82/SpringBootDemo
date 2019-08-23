package com.tm.example.service;

import com.tm.example.model.Measure;
import com.tm.example.model.Paa;
import com.tm.example.model.Pnl;

public enum MeasureValue {
    PNL, RB, RV;

    public static Class<? extends Measure> map(final MeasureValue measureValue) {
        if (measureValue == PNL) {
            return Pnl.class;
        } else if (measureValue == RB || measureValue == RV) {
            return Paa.class;
        } else {
            throw new IllegalArgumentException(
                    String.format("Cannot recognize measureValue = %s", measureValue));
        }
    }
}
