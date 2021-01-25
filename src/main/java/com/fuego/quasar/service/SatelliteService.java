package com.fuego.quasar.service;

import com.fuego.quasar.entity.Satellite;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface SatelliteService {
    List<Satellite>  satellitesInService();
    List<Satellite>  changeState(Satellite satellite);
    List<Satellite>  addSatellite(Satellite satellite);
}
