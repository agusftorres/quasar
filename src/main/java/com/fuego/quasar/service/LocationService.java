package com.fuego.quasar.service;

import com.fuego.quasar.entity.PositionResponse;
import com.fuego.quasar.entity.SateliteRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LocationService {
    PositionResponse getLocation(List<SateliteRequest> satelites) throws Exception;
}
