package com.fuego.quasar.controller.model;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.PositionResponse;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.service.DecodeMessageService;
import com.fuego.quasar.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/decode/l3/topsecret_split")
public class InformationLevelThreeController {

    @Autowired
    private LocationService  locationService;

    @Autowired
    private DecodeMessageService decodeMessageService;

    @PostMapping(path = "/{satellite}")
    public Information getInformation(@PathVariable String name, @RequestBody SateliteRequest request) throws Exception {

        request.setName(name);
        locationService.receivedInformation(request);

        return Information.builder()
                .positionResponse(position)
                .message(decodeMessageService.getMessage(messages))
                .build();
    }





}
