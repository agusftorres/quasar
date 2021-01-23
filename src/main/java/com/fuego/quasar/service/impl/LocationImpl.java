package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.PositionResponse;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.Satelite;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.repository.EquationXYRepository;
import com.fuego.quasar.repository.SateliteRepository;
import com.fuego.quasar.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class LocationImpl implements LocationService {

    private final String ACTIVE = "ACTIVE";
    private final String INACTIVE = "INACTIVE";

    @Autowired
    @Qualifier("sateliteRepository")
    SateliteRepository sateliteRepository;

    @Autowired
    @Qualifier("equationXYRepository")
    EquationXYRepository equationXYRepository;

    @Override
    public PositionResponse getLocation(List<SateliteRequest> receivedSatellites) throws Exception {
        log.info("Construyo las ecuaciones con X e Y como variables");
        buildEquations(receivedSatellites);

        EquationXY equationK = equationXYRepository.findByName("KENOBI");
        EquationXY equationS = equationXYRepository.findByName("SKYWALKER");
        EquationXY equationSa = equationXYRepository.findByName("SATO");

        log.info("Despejo X de la ecuación Kenobi {}", equationK);
        LinearEquation linearEquation = reduceAndClearX(equationK, equationS);
        log.info("Reemplazo X = {} en {}", linearEquation, equationK);
        QuadraticEquation quadraticEquation = replaceX(linearEquation, equationK);
        log.info("Se resuelve la cuadratica: {}", quadraticEquation);
        List<Double> possibleValue = solveQuadratic(quadraticEquation);
        log.info("Se reemplazan x1 y x2: {}, en {}", possibleValue, linearEquation);
        List<PositionResponse> positionResponseList = validateValues(possibleValue, linearEquation);
        log.info("Se prueban los puntos obtenidos {}", positionResponseList);
        PositionResponse positionResponse = validatePosition(positionResponseList, equationSa);

        log.info("La posicion del emisor del mensaje es: {}", positionResponse);
        return positionResponse;
    }

    public List<EquationXY> buildEquations(List<SateliteRequest> requests) throws Exception {

        Satelite satelite;
        EquationXY equationXY;

        for (SateliteRequest request : requests) {
            //TODO: try catch
            satelite = sateliteRepository.findByName(request.getName());
            equationXY = equationXYRepository.findByName(request.getName());

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
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,"El satelite no esta en servicio");
    }

    public QuadraticEquation squareOfBinomialWithParams(Double t1, Double t2){
        return QuadraticEquation.builder()
                .quadraticTerm(Math.pow(t1,2))
                .linearTerm(2 * t1 * t2)
                .independentTerm(Math.pow(t2, 2))
                .build();
    }

    public LinearEquation reduceAndClearX(EquationXY sateliteK, EquationXY sateliteS){

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

    public QuadraticEquation replaceX(LinearEquation linear, EquationXY equationXY){

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

    public List<Double> solveQuadratic(QuadraticEquation equation){
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

    public List<PositionResponse> validateValues(List<Double> possiblesX, LinearEquation equation){
        PositionResponse dot1 = PositionResponse.builder()
                .y(possiblesX.get(0))
                .x((equation.getLinearTerm() * possiblesX.get(0))
                    + equation.getLinearTerm()
                    - equation.getVar())
                .build();
        PositionResponse dot2 = PositionResponse.builder()
                .y(possiblesX.get(1))
                .x((equation.getLinearTerm() * possiblesX.get(1))
                        + equation.getLinearTerm()
                        - equation.getVar())
                .build();

        return List.of(dot1, dot2);
    }

    public PositionResponse validatePosition(List<PositionResponse> positionResponses, EquationXY equation) throws Exception {
        Double x1 = positionResponses.get(0).getX();
        Double y1 = positionResponses.get(0).getY();

        Double x2 = positionResponses.get(1).getX();
        Double y2 = positionResponses.get(2).getY();

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
            return positionResponses.get(0);
        } else if(secondPosition == equation.getVar()){
            return positionResponses.get(1);
        }

        throw new Exception("No es posible hallar la posición");
    }


//    public List<Satelite> getSatelites(){
//        PositionResponse kenobi = PositionResponse.builder()
//                .x(-500.00)
//                .y(-200.00)
//                .build();
//        PositionResponse skywalker = PositionResponse.builder()
//                .x(100.00)
//                .y(-100.00)
//                .build();
//        PositionResponse sato = PositionResponse.builder()
//                .x(500.00)
//                .y(100.00)
//                .build();
//
//        return List.of(
//                Satelite.builder()
//                        .id("1")
//                        .name("KENOBI")
//                        .position(kenobi)
//                        .state(String.valueOf(SateliteState.ACTIVE))
//                        .build(),
//                Satelite.builder()
//                        .id("2")
//                        .name("SKYWALKER")
//                        .position(skywalker)
//                        .state(String.valueOf(SateliteState.ACTIVE))
//                        .build(),
//                Satelite.builder()
//                        .id("3")
//                        .name("SATO")
//                        .position(sato)
//                        .state(String.valueOf(SateliteState.ACTIVE))
//                        .build()
//        );
//    }

    public List<EquationXY> getEcuations(){
       return List.of(
               EquationXY.builder()
                       .id("1")
                       .name("KENOBI")
                       .quadraticTermX(100.0)
                       .quadraticTermY(100.0)
                       .linearTermX(1000.0)
                       .linearTermY(400.0)
                       .independentTerm(290000.0)
                       .build(),
               EquationXY.builder()
                       .id("2")
                       .name("SKYWALKER")
                       .quadraticTermX(1.0)
                       .quadraticTermY(1.0)
                       .linearTermX(-200.0)
                       .linearTermY(200.0)
                       .independentTerm(20000.0)
                       .build(),
               EquationXY.builder()
                       .id("3")
                       .name("SATO")
                       .quadraticTermX(1.0)
                       .quadraticTermY(1.0)
                       .linearTermX(-1000.0)
                       .linearTermY(-200.0)
                       .independentTerm(260000.0)
                       .build()
       );
    }
}
