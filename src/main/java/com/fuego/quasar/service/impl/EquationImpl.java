package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.repository.EquationXYRepository;
import com.fuego.quasar.repository.SateliteRepository;
import com.fuego.quasar.service.EquationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class EquationImpl implements EquationService {

    @Autowired
    @Qualifier("sateliteRepository")
    SateliteRepository sateliteRepository;

    @Autowired
    @Qualifier("equationXYRepository")
    EquationXYRepository equationXYRepository;

    @Override
    public void buildEquations(SateliteRequest request) {

        Satellite satelite;
        EquationXY equationXY;

        //TODO: try catch
        satelite = sateliteRepository.findByName(request.getName());
        equationXY = equationXYRepository.findByName(request.getName());

        String ACTIVE = "ACTIVE";
        if (satelite.getState().equals(ACTIVE)) {
            /*
            * ||(x,y)-(-500,-200)||
            * ||(x+500,y+200)||
            * pow(x+500,2) + pow(y+200,2)
            *
            * */
            QuadraticEquation quadraticX = squareOfBinomialWithParams(1.0, -1 * satelite.getX());
            QuadraticEquation quadraticY = squareOfBinomialWithParams(1.0, -1 * satelite.getY());

            EquationXY equation = EquationXY.builder()
                        .id(equationXY.getId())
                        .name(equationXY.getName())
                        .quadraticTermX(quadraticX.getQuadraticTerm())
                        .quadraticTermY(quadraticY.getQuadraticTerm())
                        .linearTermX(quadraticX.getLinearTerm())
                        .linearTermY(quadraticY.getLinearTerm())
                        .independentTerm(quadraticX.getIndependentTerm() + quadraticY.getIndependentTerm())
                        .var(Math.pow(request.getDistance(), 2))
                        .build();

            equationXYRepository.save(equation);
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,"El satelite no esta en servicio");
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
                                + (firstTermQ.getQuadraticTerm() / Math.pow(1200, 2)))
                .linearTerm(
                        equationXY.getLinearTermY()
                                + firstTermQ.getLinearTerm())
                .independentTerm(
                        equationXY.getIndependentTerm()
                                + (firstTermQ.getIndependentTerm() / Math.pow(1200,2))
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
        double root = Math.sqrt(Math.pow(b, 2) - (4 * a * c));

        Double x1 =  (- b + root) / (2 * a);
        Double x2 = (- b - root) / (2 * a);

        return List.of(x1, x2);
    }

    @Override
    public List<Position> validateValues(List<Double> possiblesX, LinearEquation equation) {
        Position dot1 = Position.builder()
                .y(possiblesX.get(0))
                .x((equation.getLinearTerm() * possiblesX.get(0))
                        + equation.getLinearTerm()
                        - equation.getVar())
                .build();
        Position dot2 = Position.builder()
                .y(possiblesX.get(1))
                .x((equation.getLinearTerm() * possiblesX.get(1))
                        + equation.getLinearTerm()
                        - equation.getVar())
                .build();

        return List.of(dot1, dot2);
    }

    @Override
    public Position validatePosition(List<Position> positionRespons, EquationXY equation) throws Exception {
        Double x1 = positionRespons.get(0).getX();
        Double y1 = positionRespons.get(0).getY();

        Double x2 = positionRespons.get(1).getX();
        Double y2 = positionRespons.get(2).getY();

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

        if(firstPosition == equation.getVar()){
            return positionRespons.get(0);
        } else if(secondPosition == equation.getVar()){
            return positionRespons.get(1);
        }

        throw new Exception("No es posible hallar la posición");
    }
}
