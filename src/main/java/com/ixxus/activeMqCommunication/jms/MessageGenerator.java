package com.ixxus.activeMqCommunication.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


public class MessageGenerator {
    public Message<String> createMessage(){
        return MessageBuilder.withPayload("Hello World!").build();
    }
}
