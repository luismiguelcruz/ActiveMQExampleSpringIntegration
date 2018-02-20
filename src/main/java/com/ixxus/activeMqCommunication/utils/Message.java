package com.ixxus.activeMqCommunication.utils;


import org.springframework.integration.core.MessageSource;

public class Message implements MessageSource {
    @Override
    public org.springframework.messaging.Message receive() {
        return null;
    }
}
