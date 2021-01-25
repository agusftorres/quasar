package com.fuego.quasar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Information implements Serializable {
    private Position position;
    private String message;
}
