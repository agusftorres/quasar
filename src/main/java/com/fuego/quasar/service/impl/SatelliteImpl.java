package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.repository.SateliteRepository;
import com.fuego.quasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteImpl implements SatelliteService {

    @Autowired
    @Qualifier("sateliteRepository")
    SateliteRepository sateliteRepository;

    @Override
    public List<Satellite> satellitesInService() {
        return sateliteRepository.findAll();
    }

    @Override
    public List<Satellite> changeState(Satellite satellite) {
        sateliteRepository.save(satellite);
        return sateliteRepository.findAll();
    }

    @Override
    public List<Satellite> addSatellite(Satellite satellite) {
        sateliteRepository.save(satellite);
        return sateliteRepository.findAll();
    }
}
