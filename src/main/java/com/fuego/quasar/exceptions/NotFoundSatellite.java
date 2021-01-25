package com.fuego.quasar.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundSatellite extends Exception{
    public NotFoundSatellite(String s, HttpStatus notFound) {
    }
}
