package com.ixxus.activeMqCommunication.bootApp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class SpringIntegrationJms {
    private JmsTemplate jmsTemplate;

    public static final String HELLO_WORLD = "Hello World!";
    public static final String QUEUE_HELLO_WORLD_SPRING_INTEGRATION = "queue_spring_integration_1";

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringIntegrationJms springIntExample = (SpringIntegrationJms) context.getBean("springIntegrationJms");
        springIntExample.sendMessage(HELLO_WORLD);
    }

    public void sendMessage(String message) {
        getJmsTemplate().convertAndSend(QUEUE_HELLO_WORLD_SPRING_INTEGRATION, message);
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
