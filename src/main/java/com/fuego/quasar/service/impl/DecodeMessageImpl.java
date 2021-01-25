package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.CodeMessages;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.exceptions.InsufficientInformation;
import com.fuego.quasar.repository.SatelliteRepository;
import com.fuego.quasar.service.DecodeMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DecodeMessageImpl implements DecodeMessageService {

    @Autowired
    @Qualifier("satelliteRepository")
    private SatelliteRepository satelliteRepository;

    @Override
    public String getMessage(List<String[]> messages){
        return buildMessage(messages);
    }

    @Override
    public String getMessage() throws Exception {
        try{
            List<Satellite> satellites = satelliteRepository.findAll();
            List<List<CodeMessages>>  messages = satellites.stream().map(Satellite::getMessages).collect(Collectors.toList());
            List<String[]> arrayMessages = messages.stream().map(Satellite::fromStorage).collect(Collectors.toList());
            return buildMessage(arrayMessages);
        }catch (Exception e){
            throw new InsufficientInformation("Los datos no son suficientes para descifrar el mensaje", HttpStatus.NOT_FOUND);
        }
    }

    public String buildMessage(List<String[]> messages) {
        String[] incomingMessage;

        int numOfWords = messages.get(0).length;
        incomingMessage = new String[numOfWords];

        for(int i = 0; i < messages.size(); i++){
            for(int j = 0; j < numOfWords ; j++){
                if(!messages.get(i)[j].equals("")){
                    incomingMessage[j] = messages.get(i)[j];
                   }
            }
        }
        log.info("Mensaje recibido: {}", String.join(" ", incomingMessage));
        return String.join(" ", incomingMessage);
    }
}
