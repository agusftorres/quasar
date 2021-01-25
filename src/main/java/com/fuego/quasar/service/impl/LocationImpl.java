package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.EquationXY;
import com.fuego.quasar.entity.LinearEquation;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.QuadraticEquation;
import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.exceptions.InsufficientInformation;
import com.fuego.quasar.repository.EquationXYRepository;
import com.fuego.quasar.repository.SatelliteRepository;
import com.fuego.quasar.service.EquationService;
import com.fuego.quasar.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocationImpl implements LocationService {

    @Autowired
    @Qualifier("satelliteRepository")
    SatelliteRepository satelliteRepository;

    @Autowired
    @Qualifier("equationXYRepository")
    EquationXYRepository equationXYRepository;

    @Autowired
    EquationService equationService;

    @Override
    public Position getLocation(List<SatelliteRequest> satellites) throws Exception {
        log.info("Valido que el request no este vacío");
        try{
            log.info("Construyo las ecuaciones con X e Y como variables");
            satellites.forEach(satellite -> {
                try {
                    equationService.buildEquations(satellite);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return findPosition();
        }catch (Exception w){
            throw new InsufficientInformation("Los datos no son suficientes para localizar al emisor", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public Position getLocation() throws Exception {
        return findPosition();
    }

    private Position findPosition() throws Exception {

        try{
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
        }catch (Exception e){
            throw new InsufficientInformation("La información disponible no es suficiente para localizar al emisor", HttpStatus.FORBIDDEN);
        }
    }
}
