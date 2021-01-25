package com.fuego.quasar.service;

import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.SatelliteRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EquationService {
     void buildEquations(SatelliteRequest request) throws Exception;
     QuadraticEquation squareOfBinomialWithParams(Double t1, Double t2);
     LinearEquation reduceAndClearX(EquationXY sateliteK, EquationXY sateliteS);
     QuadraticEquation replaceX(LinearEquation linear, EquationXY equationXY);
     List<Double> solveQuadratic(QuadraticEquation equation);
     List<Position> validateValues(List<Double> possiblesX, LinearEquation equation);
     Position validatePosition(List<Position> positionRespons, EquationXY equation) throws Exception;
}
