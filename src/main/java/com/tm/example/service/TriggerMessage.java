package com.tm.example.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TriggerMessage {

    private final MeasureValue measureValue;

}
