package com.ixxus.activeMqCommunication.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLogger.class);

    public void logMessage(Message<String> message){
        LOGGER.info(message.getPayload());
    }
}
