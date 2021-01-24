package com.fuego.quasar.controller.model;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/decode/l3/topsecret_split")
public class InformationLevelThreeController {

    @Autowired
    private InformationService informationService;

    @PostMapping(path = "/{satellite}")
    public void postInformation(@PathVariable String satellite, @RequestBody SateliteRequest request) throws Exception {
        request.setName(satellite);
        informationService.receivedInformation(request);
    }

    @GetMapping
    public Information getInformation() throws Exception {
        return informationService.buildInformation();
    }





}
