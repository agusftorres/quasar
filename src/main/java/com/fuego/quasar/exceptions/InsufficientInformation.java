package com.fuego.quasar.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientInformation extends Exception{
    public InsufficientInformation(String s, HttpStatus notFound) {
    }
}
