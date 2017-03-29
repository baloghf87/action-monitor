package hu.ferencbalogh.actionmonitor.activemq.receiver;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
 
@Configuration
@ComponentScan(basePackages="hu.ferencbalogh.actionmonitor.activemq.receiver")
public class ActiveMQReceiverConfiguration {
    @Autowired
    private ConnectionFactory connectionFactory;
     
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-1");
        return factory;
    }
    
}