package com.fuego.quasar.entity;

import com.sun.tools.javac.util.List;

public abstract class Equation {
    abstract void reduceAndClearX();
    abstract void replaceX();
    abstract void squareOfBinomial(Double a, Double b);
    abstract void solveQuadratic(Double a, Double b, Double c);
    abstract void validatePosition(List<Position> positions);
    abstract void validateValues(List<Double> values);
    abstract void buildEquations();
}
