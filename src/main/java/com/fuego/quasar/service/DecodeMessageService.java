package com.fuego.quasar.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DecodeMessageService {
    String getMessage(List<String[]> messages) throws Exception;
}
