package com.fuego.quasar.service;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.SateliteRequest;
import org.springframework.stereotype.Component;

@Component
public interface InformationService {
    void receivedInformation(SateliteRequest request) throws Exception;
    Information buildInformation() throws Exception;
}
