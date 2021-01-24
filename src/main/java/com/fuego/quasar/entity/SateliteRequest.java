package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SateliteRequest implements Serializable {
    private String name;
    private Double distance;
    private String[] message;


}
