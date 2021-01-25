package com.fuego.quasar.service;

import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.SatelliteRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LocationService {
    Position getLocation(List<SatelliteRequest> satellites) throws Exception;
    Position getLocation() throws Exception;
}
