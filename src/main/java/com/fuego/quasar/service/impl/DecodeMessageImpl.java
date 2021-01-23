package com.fuego.quasar.service.impl;

import com.fuego.quasar.service.DecodeMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DecodeMessageImpl implements DecodeMessageService {
    @Override
    public String getMessage(List<String[]> messages) throws Exception {

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
