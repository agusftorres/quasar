package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.exceptions.InactiveSatellite;
import com.fuego.quasar.exceptions.NotFoundPosition;
import com.fuego.quasar.repository.EquationXYRepository;
import com.fuego.quasar.repository.SatelliteRepository;
import com.fuego.quasar.service.EquationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EquationImpl implements EquationService {

    @Autowired
    @Qualifier("satelliteRepository")
    SatelliteRepository satelliteRepository;

    @Autowired
    @Qualifier("equationXYRepository")
    EquationXYRepository equationXYRepository;

    @Override
    public void buildEquations(SatelliteRequest request) throws InactiveSatellite {

        try {
            List<Satellite> satellites = satelliteRepository.findAll();
            List<EquationXY> xies = equationXYRepository.findAll();

            Satellite satellite;
            satellite = satellites.stream().filter(item -> item.getName().equals(request.getName().toUpperCase())).findAny().get();

            if (satellite.getState().equals("ACTIVE")) {
                /*
                 * ||(x,y)-(-5,-2)||
                 * ||(x+5,y+2)||
                 * pow(x+5,2) + pow(y+2,2)
                 *
                 * */
                QuadraticEquation quadraticX = squareOfBinomialWithParams(1.0, -1 * (satellite.getX() / 100));
                QuadraticEquation quadraticY = squareOfBinomialWithParams(1.0, -1 * (satellite.getY() / 100));
                EquationXY equation;

                if (xies.isEmpty()) {

                    equation = EquationXY.builder()
                            .name(satellite.getName())
                            .quadraticTermX(quadraticX.getQuadraticTerm())
                            .quadraticTermY(quadraticY.getQuadraticTerm())
                            .linearTermX(quadraticX.getLinearTerm())
                            .linearTermY(quadraticY.getLinearTerm())
                            .independentTerm(quadraticX.getIndependentTerm() + quadraticY.getIndependentTerm())
                            .var(Math.pow(request.getDistance() / 100, 2))
                            .build();
                } else {
                    Optional<EquationXY> equationXY = xies.stream().filter(xy -> xy.getName().equals(request.getName().toUpperCase())).findAny();

                    if (equationXY.isPresent()) {
                        equation = EquationXY.builder()
                                .id(equationXY.get().getId())
                                .name(equationXY.get().getName())
                                .quadraticTermX(quadraticX.getQuadraticTerm())
                                .quadraticTermY(quadraticY.getQuadraticTerm())
                                .linearTermX(quadraticX.getLinearTerm())
                                .linearTermY(quadraticY.getLinearTerm())
                                .independentTerm(quadraticX.getIndependentTerm() + quadraticY.getIndependentTerm())
                                .var(Math.pow(request.getDistance() / 100, 2))
                                .build();
                    } else {
                        equation = EquationXY.builder()
                                .name(satellite.getName())
                                .quadraticTermX(quadraticX.getQuadraticTerm())
                                .quadraticTermY(quadraticY.getQuadraticTerm())
                                .linearTermX(quadraticX.getLinearTerm())
                                .linearTermY(quadraticY.getLinearTerm())
                                .independentTerm(quadraticX.getIndependentTerm() + quadraticY.getIndependentTerm())
                                .var(Math.pow(request.getDistance() / 100, 2))
                                .build();
                    }
                }
                equationXYRepository.save(equation);
            }
        }catch (Exception e){
            throw new InactiveSatellite(
                   "El satelite no esta en servicio",  HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public QuadraticEquation squareOfBinomialWithParams(Double t1, Double t2) {
        return QuadraticEquation.builder()
                .quadraticTerm(Math.pow(t1,2))
                .linearTerm(2 * t1 * t2)
                .independentTerm(Math.pow(t2, 2))
                .build();
    }

    @Override
    public LinearEquation reduceAndClearX(EquationXY sateliteK, EquationXY sateliteS) {

        LinearEquation linearEquation;

        log.info("reduzco la primer ecuación con respecto a la segunda {} - {}", sateliteK, sateliteS);
        EquationXY equationXY = EquationXY.builder()
                .linearTermX(sateliteK.getLinearTermX() - sateliteS.getLinearTermX())
                .linearTermY(sateliteK.getLinearTermY() - sateliteS.getLinearTermY())
                .independentTerm(sateliteK.getIndependentTerm() - sateliteS.getIndependentTerm())
                .var(sateliteK.getVar() - sateliteS.getVar())
                .build();

        log.info("Despejo X de {}", equationXY);
        if(equationXY.getLinearTermX() == 1){
            linearEquation = LinearEquation.builder()
                    .linearTerm(-1 * equationXY.getLinearTermY())
                    .independentTerm(equationXY.getVar() - equationXY.getIndependentTerm())
                    .var(0.00)
                    .build();

        } else{
            linearEquation = LinearEquation.builder()
                    .linearTerm((-1 * equationXY.getLinearTermY()) / equationXY.getLinearTermX())
                    .independentTerm((equationXY.getVar() - equationXY.getIndependentTerm()) / equationXY.getLinearTermX())
                    .var(0.00)
                    .build();
        }

        log.info("X despejada: {}", linearEquation);
        return linearEquation;
    }

    @Override
    public QuadraticEquation replaceX(LinearEquation linear, EquationXY equationXY) {
        QuadraticEquation firstTermQ = squareOfBinomialWithParams(linear.getLinearTerm(), linear.getIndependentTerm());

        QuadraticEquation quadraticEquation = QuadraticEquation.builder()
                .quadraticTerm(
                        equationXY.getQuadraticTermY()
                                + firstTermQ.getQuadraticTerm())
                .linearTerm(
                        equationXY.getLinearTermY()
                                + firstTermQ.getLinearTerm()
                                + (equationXY.getLinearTermX() * linear.getLinearTerm()))
                .independentTerm(
                        equationXY.getIndependentTerm()
                                + firstTermQ.getIndependentTerm()
                                + (equationXY.getLinearTermX() * linear.getIndependentTerm())
                                - equationXY.getVar())
                .var(0.00)
                .build();

        log.info("La ecuación con X reemplazada queda: {}", quadraticEquation);
        return quadraticEquation;
    }

    @Override
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

    @Override
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

    @Override
    public Position validatePosition(List<Position> positionResponse, EquationXY equation) throws Exception {
        try{
            DecimalFormat df = new DecimalFormat("###.##");

            Double x1 = (double) Math.round(positionResponse.get(0).getX());
            Double y1 = (double) Math.round(positionResponse.get(0).getY());

            Double x2 = (double) Math.round(positionResponse.get(1).getX());
            Double y2 =(double) Math.round(positionResponse.get(1).getY());

            double firstPosition =
                    Math.pow(equation.getQuadraticTermX() * x1, 2)
                            + Math.pow(equation.getQuadraticTermY() * y1, 2)
                            + (equation.getLinearTermX() * x1)
                            + (equation.getLinearTermY() * y1)
                            + equation.getIndependentTerm();

            double secondPosition =
                    Math.pow(equation.getQuadraticTermX() * x2, 2)
                            + Math.pow(equation.getQuadraticTermY() * y2, 2)
                            + (equation.getLinearTermX() * x2)
                            + (equation.getLinearTermY() * y2)
                            + equation.getIndependentTerm();

            if(df.format(firstPosition).equals(df.format(Math.round(equation.getVar())))){
                return  Position.builder()
                        .x( (double) Math.round(positionResponse.get(0).getX()) * 100)
                        .y( (double) Math.round(positionResponse.get(0).getY()) * 100)
                        .build();
            } else if(df.format(secondPosition).equals(df.format(Math.round(equation.getVar())))){
                return Position.builder()
                        .x( (double) Math.round(positionResponse.get(1).getX()) * 100)
                        .y( (double) Math.round(positionResponse.get(1).getY()) * 100)
                        .build();
            }
            throw new Exception();
        }catch (Exception e){
            throw new NotFoundPosition("No es posible hallar la posición", HttpStatus.FORBIDDEN);
        }
    }
}
