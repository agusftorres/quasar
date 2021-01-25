package com.fuego.quasar.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundPosition extends Exception{
    public NotFoundPosition(String s, HttpStatus notFound) {
    }
}
