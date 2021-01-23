package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuadraticEquation {
    private String id;
    private Double quadraticTerm;
    private Double linearTerm;
    private Double independentTerm;
    private Double var;
}
