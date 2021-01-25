package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.repository.EquationXYRepository;
import com.fuego.quasar.repository.SateliteRepository;
import com.fuego.quasar.service.EquationService;
import com.fuego.quasar.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocationImpl implements LocationService {

    @Autowired
    @Qualifier("sateliteRepository")
    SateliteRepository sateliteRepository;

    @Autowired
    @Qualifier("equationXYRepository")
    EquationXYRepository equationXYRepository;

    @Autowired
    EquationService equationService;

    @Override
    public Position getLocation(List<SateliteRequest> satellites) throws Exception {
        log.info("Valido que el request no este vacío");
        if(!satellites.isEmpty()){
            log.info("Construyo las ecuaciones con X e Y como variables");
            satellites.forEach(satellite -> {
                try {
                    equationService.buildEquations(satellite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else{
            throw new Exception("Los datos no son suficientes para localizar al emisor");
        }

        return findPosition();
    }

    @Override
    public Position getLocation() throws Exception {
        return findPosition();
    }

    private Position findPosition() throws Exception {

        List<EquationXY> xies = equationXYRepository.findAll();

        EquationXY equationK = xies.stream().filter(xy -> xy.getName().equals("KENOBI")).findAny().get();
        EquationXY equationS = xies.stream().filter(xy -> xy.getName().equals("SKYWALKER")).findAny().get();
        EquationXY equationSa = xies.stream().filter(xy -> xy.getName().equals("SATO")).findAny().get();

        log.info("Despejo X de la ecuación Kenobi {}", equationK);
        LinearEquation linearEquation = equationService.reduceAndClearX(equationK, equationS);
        log.info("Reemplazo X = {} en {}", linearEquation, equationK);
        QuadraticEquation quadraticEquation = equationService.replaceX(linearEquation, equationK);
        log.info("Se resuelve la cuadratica: {}", quadraticEquation);
        List<Double> possibleValue = equationService.solveQuadratic(quadraticEquation);
        log.info("Se reemplazan x1 y x2: {}, en {}", possibleValue, linearEquation);
        List<Position> positionList = equationService.validateValues(possibleValue, linearEquation);
        log.info("Se prueban los puntos obtenidos {}", positionList);
        Position position = equationService.validatePosition(positionList, equationSa);

        log.info("La posicion del emisor del mensaje es: {}", position);
        return position;
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

//    private List<EquationXY> getEcuations(){
//       return List.of(
//               EquationXY.builder()
//                       .id("1")
//                       .name("KENOBI")
//                       .quadraticTermX(100.0)
//                       .quadraticTermY(100.0)
//                       .linearTermX(1000.0)
//                       .linearTermY(400.0)
//                       .independentTerm(290000.0)
//                       .build(),
//               EquationXY.builder()
//                       .id("2")
//                       .name("SKYWALKER")
//                       .quadraticTermX(1.0)
//                       .quadraticTermY(1.0)
//                       .linearTermX(-200.0)
//                       .linearTermY(200.0)
//                       .independentTerm(20000.0)
//                       .build(),
//               EquationXY.builder()
//                       .id("3")
//                       .name("SATO")
//                       .quadraticTermX(1.0)
//                       .quadraticTermY(1.0)
//                       .linearTermX(-1000.0)
//                       .linearTermY(-200.0)
//                       .independentTerm(260000.0)
//                       .build()
//       );
//    }
}
