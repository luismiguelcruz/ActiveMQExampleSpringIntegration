package com.ixxus.activeMqCommunication.bootApp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageGeneratorService {
    private JmsTemplate jmsTemplate;

    public static final String HELLO_WORLD = "Hello World!";
    public static final String QUEUE_HELLO_WORLD = "queue_spring_integration_1";
    public static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        MessageGeneratorService messageService = (MessageGeneratorService) context.getBean("messageGeneratorService");
        messageService.sendMessage(HELLO_WORLD);
    }

    public void sendMessage(String message) {
        getJmsTemplate().convertAndSend(QUEUE_HELLO_WORLD, message);
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void processMessage(String message) {
        System.out.println("Message: " + message + " [processed]");
    }
}
