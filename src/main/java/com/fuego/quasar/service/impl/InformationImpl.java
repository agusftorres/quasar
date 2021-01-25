package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.service.DecodeMessageService;
import com.fuego.quasar.service.EquationService;
import com.fuego.quasar.service.InformationService;
import com.fuego.quasar.service.LocationService;
import com.fuego.quasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InformationImpl implements InformationService {

    @Autowired
    private EquationService equationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private DecodeMessageService decodeMessageService;

    @Autowired
    private SatelliteService satelliteService;

    @Override
    public void receivedInformation(SatelliteRequest request) throws Exception {
        satelliteService.changeSatellite(request);
        equationService.buildEquations(request);
    }

    @Override
    public Information buildInformation() throws Exception {
        return Information.builder()
                .position(locationService.getLocation())
                .message(decodeMessageService.getMessage())
                .build();
    }
}
