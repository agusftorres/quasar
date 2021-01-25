package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {
    private Double x;
    private Double y;
}
