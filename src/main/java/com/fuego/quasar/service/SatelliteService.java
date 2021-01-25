package com.fuego.quasar.service;

import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.exceptions.NotFoundSatellite;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface SatelliteService {
    List<Satellite>  satellitesInService();
    List<Satellite>  changeSatellite(SatelliteRequest request) throws NotFoundSatellite;
    List<Satellite>  addSatellite(Satellite satellite);
}
