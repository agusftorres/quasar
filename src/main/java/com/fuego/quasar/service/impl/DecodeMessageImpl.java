package com.fuego.quasar.service.impl;

import com.fuego.quasar.entity.CodeMessages;
import com.fuego.quasar.entity.Satellite;
import com.fuego.quasar.repository.SateliteRepository;
import com.fuego.quasar.service.DecodeMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DecodeMessageImpl implements DecodeMessageService {

    @Autowired
    @Qualifier("sateliteRepository")
    private SateliteRepository sateliteRepository;


    @Override
    public String getMessage(List<String[]> messages) throws Exception {

        return buildMessage(messages);
    }

    @Override
    public String getMessage() throws Exception {
        List<Satellite> satellites = sateliteRepository.findAll();
        List<List<CodeMessages>>  messages = satellites.stream().map(Satellite::getMessages).collect(Collectors.toList());
        List<String[]> arrayMessages = messages.stream().map(Satellite::fromStorage).collect(Collectors.toList());

        return buildMessage(arrayMessages);
    }

    public String buildMessage(List<String[]> messages) throws Exception {
        String[] incomingMessage;

        if(!messages.isEmpty()) {
            int numOfWords = messages.get(0).length;
            incomingMessage = new String[numOfWords];

            for(int i = 0; i < messages.size(); i++){
                for(int j = 0; j < numOfWords ; j++){
                    if(!messages.get(i)[j].equals("")){
                        incomingMessage[j] = messages.get(i)[j];
                    }
                }
            }

            return Arrays.toString(incomingMessage);
        }
        throw new Exception("Los datos no son suficientes para descifrar el mensaje");
    }
}
