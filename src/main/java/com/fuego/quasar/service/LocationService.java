package com.fuego.quasar.service;

import com.fuego.quasar.entity.Position;
import com.fuego.quasar.entity.SateliteRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LocationService {
    Position getLocation(List<SateliteRequest> satellites) throws Exception;
    Position getLocation() throws Exception;
}
