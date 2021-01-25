package com.fuego.quasar.exceptions;

import org.springframework.http.HttpStatus;

public class InactiveSatellite extends Exception{
    public InactiveSatellite(String s, HttpStatus notFound) {
    }
}
