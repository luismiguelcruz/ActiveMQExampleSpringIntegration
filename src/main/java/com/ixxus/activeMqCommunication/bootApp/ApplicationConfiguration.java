package com.ixxus.activeMqCommunication.bootApp;

import com.ixxus.activeMqCommunication.jms.MessageGenerator;
import com.ixxus.activeMqCommunication.jms.MessageLogger;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;

import java.lang.annotation.Target;

import static com.ixxus.activeMqCommunication.bootApp.ActiveMqConfiguration.QUEUE_HELLO_WORLD;

@Configuration
public class ApplicationConfiguration {
    public static final String BROKER_URL = "tcp://localhost:61616";

    @Autowired
    @Qualifier("messageGenerator")
    private MessageGenerator messageGenerator;

    @Autowired
    @Qualifier("messageLogger")
    private MessageLogger messageLogger;


    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;


    @Bean(name="pollingMessageGenerator")
    @InboundChannelAdapter(value = "generateMessageChanel", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
    public MessageSource<String> getInboundChannelAdapter() {
        final MessageSource messageSource= new MethodInvokingMessageSource();
        final MethodInvokingMessageSource methodInvokingMessageSource = (MethodInvokingMessageSource) messageSource;
        methodInvokingMessageSource.setObject(messageGenerator);
        methodInvokingMessageSource.setMethodName("createMessage");
        return messageSource;
    }

    @Bean(name="generateMessageChanel")
    public DirectChannel getGenerateMessageChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow jmsOutboundFlow() {
        return IntegrationFlows.from("generateMessageChanel")
                .handle(Jms.outboundAdapter(jmsTemplate).destination(QUEUE_HELLO_WORLD))
                .get();
    }

    @Bean(name="inboundJMSChannel")
    public DirectChannel getInboundJMSChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jmsInboundFlow() {
        return IntegrationFlows.from(Jms.inboundAdapter(jmsTemplate),
                c -> c.poller(Pollers.fixedRate(1000).maxMessagesPerPoll(1)))
                .channel("inboundJMSChannel")
                .get();
    }

    /*@Bean
    @ServiceActivator(inputChannel = "inboundJMSChannel")
    public IntegrationFlow inboundJMSChannelReader() {
        return IntegrationFlows.from("inboundJMSChannel")
                .handle(messageLogger)
                .get();
    }*/


    /*@ServiceActivator(inputChannel = "inboundJMSChannel", )
    public void processMessage(MessageLoger messageLogger) {
        System.out.println("ExampleLuismi: "+messageLogger.logMessage());
    }*/
}
