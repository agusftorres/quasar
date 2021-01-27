package com.fuego.quasar.entity;

import com.fuego.quasar.exceptions.NotFoundPosition;
import com.sun.tools.javac.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.text.DecimalFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EquationXY extends Equation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private Double quadraticTermX;
    private Double quadraticTermY;
    private Double linearTermX;
    private Double linearTermY;
    private Double independentTerm;
    private Double var;

    @Override
    void reduceAndClearX() {

    }

    @Override
    void replaceX() {

    }

    @Override
    void squareOfBinomial(Double a, Double b) {

    }

    @Override
    void solveQuadratic(Double a, Double b, Double c) {

    }

    @Override
    void validatePosition(List<Position> positions) {
        
    }

    @Override
    void validateValues(List<Double> values) {

    }

    @Override
    void buildEquations() {

    }
}
