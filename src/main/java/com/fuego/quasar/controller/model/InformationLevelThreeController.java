package com.fuego.quasar.controller.model;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.SatelliteRequest;
import com.fuego.quasar.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/decode/l3/topsecret_split")
public class InformationLevelThreeController {

    @Autowired
    private InformationService informationService;

    @PostMapping(path = "/{satellite}")
    public void postInformation(@PathVariable String satellite, @RequestBody SatelliteRequest request) throws Exception {
        request.setName(satellite);
        informationService.receivedInformation(request);
    }

    @GetMapping
    public ResponseEntity<Information> getInformation() throws Exception {
        log.info("Información proveniente del satelite: {}", informationService.buildInformation());
        return new ResponseEntity<>(informationService.buildInformation(), HttpStatus.OK);
    }





}
