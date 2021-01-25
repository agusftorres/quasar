package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.exceptions.NotFoundSatellite;
import com.fuego.quasar.repository.CodeMessagesRepository;
import com.fuego.quasar.repository.SatelliteRepository;
import com.fuego.quasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SatelliteImpl implements SatelliteService {

    @Autowired
    @Qualifier("satelliteRepository")
    SatelliteRepository satelliteRepository;

    @Autowired
    @Qualifier("codeMessagesRepository")
    CodeMessagesRepository codeMessagesRepository;

    @Override
    public List<Satellite> satellitesInService() {
        return satelliteRepository.findAll();
    }

    @Override
    public List<Satellite> changeSatellite(SatelliteRequest request) throws NotFoundSatellite {

        try{
            List<Satellite> satellites = satelliteRepository.findAll();
            Optional<Satellite> satellite = satellites
                    .stream()
                    .filter(item -> item.getName().equals(request.getName().toUpperCase()))
                    .findAny();

            satellite.get().setMessages(SatelliteRequest.toStorage(request.getMessage()));
            satelliteRepository.save(satellite.get());
            return satelliteRepository.findAll();
        }catch (Exception e){
            throw new NotFoundSatellite("No se ha encontrado el satelite", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Satellite> addSatellite(Satellite satellite) {
        satelliteRepository.save(satellite);
        return satelliteRepository.findAll();
    }
}
