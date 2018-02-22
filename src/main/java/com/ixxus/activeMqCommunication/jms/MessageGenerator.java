package com.ixxus.activeMqCommunication.jms;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service("messageGenerator")
public class MessageGenerator {
    public Message<String> createMessage(){
        return MessageBuilder.withPayload("Hello World!").build();
    }
}
