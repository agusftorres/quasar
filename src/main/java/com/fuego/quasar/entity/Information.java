package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Information {
    private PositionResponse positionResponse;
    private String message;
}
