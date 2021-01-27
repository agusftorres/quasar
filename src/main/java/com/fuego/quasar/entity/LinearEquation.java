package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class LinearEquation {
    private Double linearTerm;
    private Double independentTerm;
    private Double var;

    public List<Position> validateValues(List<Double> possiblesX, LinearEquation equation) {
        Position dot1 = Position.builder()
                .y(possiblesX.get(0))
                .x((equation.getLinearTerm() * possiblesX.get(0))
                        + equation.getIndependentTerm())
                .build();
        Position dot2 = Position.builder()
                .y(possiblesX.get(1))
                .x((equation.getLinearTerm() * possiblesX.get(1))
                        + equation.getIndependentTerm())
                .build();

        List<Position> list = new ArrayList<>();
        list.add(dot1);
        list.add(dot2);
        return list;
        //return List.of(dot1, dot2);
    }
}
