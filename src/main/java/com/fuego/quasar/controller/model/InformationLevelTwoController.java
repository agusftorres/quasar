package com.fuego.quasar.controller.model;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.service.DecodeMessageService;
import com.fuego.quasar.service.LocationService;
import com.fuego.quasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/decode")
public class InformationLevelTwoController {

    @Autowired
    private LocationService  locationService;

    @Autowired
    private DecodeMessageService decodeMessageService;

    @Autowired
    private SatelliteService satelliteService;

    @PostMapping(path = "/l2/topsecret/")
    public Information getInformation(@RequestBody List<SateliteRequest> request) throws Exception {

        List<String[]> messages = request.stream().map(SateliteRequest::getMessage).collect(Collectors.toList());
        Position position = locationService.getLocation(request);

        return Information.builder()
                .position(position)
                .message(decodeMessageService.getMessage(messages))
                .build();
    }

    @PostMapping(path = "/l2/satellites")
    public List<Satellite> satellitesState(){

        List<Satellite> list = satelliteService.satellitesInService();
        if(list.isEmpty()){

            Satellite kenobi = Satellite.builder()
                        .name("KENOBI")
                        .x(-500.00)
                        .y(-200.00)
                        .state("ACTIVE")
                        .build();

            Satellite skywalker =  Satellite.builder()
                        .id("2")
                        .name("SKYWALKER")
                        .x(100.00)
                        .y(-100.00)
                        .state("ACTIVE")
                        .build();

            Satellite sato =  Satellite.builder()
                        .name("SATO")
                        .x(500.00)
                        .y(100.00)
                        .state("ACTIVE")
                        .build();

            satelliteService.addSatellite(kenobi);
            satelliteService.addSatellite(skywalker);
            satelliteService.addSatellite(sato);

            list.add(kenobi);
            list.add(sato);
            list.add(skywalker);
        }

        return list;
    }



}
