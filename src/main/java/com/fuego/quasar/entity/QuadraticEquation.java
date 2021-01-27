package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class QuadraticEquation {
    private String id;
    private Double quadraticTerm;
    private Double linearTerm;
    private Double independentTerm;
    private Double var;


    public QuadraticEquation squareOfBinomialWithParams(Double t1, Double t2) {
        return QuadraticEquation.builder()
                .quadraticTerm(Math.pow(t1,2))
                .linearTerm(2 * t1 * t2)
                .independentTerm(Math.pow(t2, 2))
                .build();
    }

    public List<Double> solveQuadratic(QuadraticEquation equation) {
        /*
         * (-b +- sqr(pow(b) -4ac)) / 2a
         * */
        Double a = equation.getQuadraticTerm();
        Double b = equation.getLinearTerm();
        Double c = equation.getIndependentTerm();
        Double root = Math.sqrt(Math.pow(b, 2) - (4 * a * c));

        Double x1 =  (- b + root) / (2 * a);
        Double x2 = (- b - root) / (2 * a);

        List<Double> list = new ArrayList<>();
        list.add(x1);
        list.add(x2);

        return list;
        // return List.of(x1, x2);
    }
}
