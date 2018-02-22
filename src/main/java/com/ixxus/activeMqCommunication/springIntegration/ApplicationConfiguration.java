package com.ixxus.activeMqCommunication.springIntegration;

import com.ixxus.activeMqCommunication.config.ActiveMqConfiguration;
import com.ixxus.activeMqCommunication.jms.MessageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;

@Configuration
public class ApplicationConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Value(value = "${spring.activemq.queue-name}")
    public String QUEUE_HELLO_WORLD;

    @Autowired
    @Qualifier("messageGenerator")
    private MessageGenerator messageGenerator;

    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;


    @Bean(name="pollingMessageGenerator")
    @InboundChannelAdapter(value = "generateMessageChannel", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
    public MessageSource<String> getInboundChannelAdapter() {
        final MessageSource messageSource= new MethodInvokingMessageSource();
        final MethodInvokingMessageSource methodInvokingMessageSource = (MethodInvokingMessageSource) messageSource;
        methodInvokingMessageSource.setObject(messageGenerator);
        methodInvokingMessageSource.setMethodName("createMessage");
        return messageSource;
    }

    @Bean(name="generateMessageChannel")
    public DirectChannel getGenerateMessageChannel() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow jmsOutboundFlow() {
        return IntegrationFlows.from("generateMessageChannel")
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

    @ServiceActivator(inputChannel = "inboundJMSChannel")
    public void logMessage(Message<String> message){
        LOGGER.info(message.getPayload());
    }
}
