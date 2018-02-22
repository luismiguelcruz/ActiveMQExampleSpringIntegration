package com.ixxus.activeMqCommunication.bootApp;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class ActiveMqConfiguration {
    public static final String BROKER_URL = "tcp://localhost:61616";
    public static final String QUEUE_HELLO_WORLD = "SpringIntegrationAnnotationActiveMQ-queue";



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
        template.setDefaultDestinationName("generateMessageChanel");
        return template;
    }

    @Bean(name="jmsTemplateOutput")
    public JmsTemplate getJmsTemplateOutput(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(getConnectionFactory());
        template.setDefaultDestinationName("inboundJMSChannel");
        return template;
    }

}
