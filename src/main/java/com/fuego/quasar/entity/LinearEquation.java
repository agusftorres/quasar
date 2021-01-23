package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LinearEquation {
    private Double linearTerm;
    private Double independentTerm;
    private Double var;
}
