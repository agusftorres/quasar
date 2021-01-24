package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.service.DecodeMessageService;
import com.fuego.quasar.service.EquationService;
import com.fuego.quasar.service.InformationService;
import com.fuego.quasar.service.LocationService;
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

    @Override
    public void receivedInformation(SateliteRequest request) throws Exception {
        equationService.buildEquations(request);
    }

    @Override
    public Information buildInformation() throws Exception {
        Position position = locationService.getLocation();

        return Information.builder()
                .position(position)
                .message(decodeMessageService.getMessage())
                .build();
    }
}
