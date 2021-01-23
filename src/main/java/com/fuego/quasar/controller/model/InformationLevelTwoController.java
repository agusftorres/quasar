package com.fuego.quasar.controller.model;

import com.fuego.quasar.entity.Information;
import com.fuego.quasar.entity.PositionResponse;
import com.fuego.quasar.entity.SateliteRequest;
import com.fuego.quasar.service.DecodeMessageService;
import com.fuego.quasar.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/decode")
public class InformationLevelTwoController {

    @Autowired
    private LocationService  locationService;

    @Autowired
    private DecodeMessageService decodeMessageService;

    @PostMapping(path = "/l2/topsecret/")
    public Information getInformation(@RequestBody List<SateliteRequest> request) throws Exception {

        List<String[]> messages = request.stream().map(SateliteRequest::getMessage).collect(Collectors.toList());
        PositionResponse position = locationService.getLocation(request);

        return Information.builder()
                .positionResponse(position)
                .message(decodeMessageService.getMessage(messages))
                .build();
    }



}
