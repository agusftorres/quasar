package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionResponse {
    private Double x;
    private Double y;
}