package com.fuego.quasar.service;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.SatelliteRequest;
import org.springframework.stereotype.Component;

@Component
public interface InformationService {
    void receivedInformation(SatelliteRequest request) throws Exception;
    Information buildInformation() throws Exception;
}
