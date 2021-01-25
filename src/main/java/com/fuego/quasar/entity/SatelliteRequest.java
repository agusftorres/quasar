package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class SatelliteRequest implements Serializable {
    private String name;
    private Double distance;
    private String[] message;

    public static List<CodeMessages> toStorage(String[] array){
        List<CodeMessages> messages = new ArrayList<>();
        Arrays.stream(array).forEach(item -> messages.add(CodeMessages.builder().message(item).build()));
        return messages;
    }

}
