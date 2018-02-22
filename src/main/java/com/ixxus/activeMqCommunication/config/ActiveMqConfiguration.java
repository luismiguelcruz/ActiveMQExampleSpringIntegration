package com.ixxus.activeMqCommunication.config;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class ActiveMqConfiguration {

    @Value(value = "${spring.activemq.broker-url}")
    public String BROKER_URL;

    @Value(value = "${spring.activemq.queue-name}")
    public String QUEUE_HELLO_WORLD;


    @Bean(name="activeMQconnectionFactory")
    public ActiveMQConnectionFactory getConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        return connectionFactory;
    }

    @Bean(name="jmsTemplate")
    public JmsTemplate getJmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(getConnectionFactory());
        template.setDefaultDestinationName(QUEUE_HELLO_WORLD);
        return template;
    }

}
