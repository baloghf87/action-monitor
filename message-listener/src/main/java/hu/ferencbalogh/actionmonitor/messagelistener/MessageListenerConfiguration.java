package hu.ferencbalogh.actionmonitor.messagelistener;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
 
@Configuration
@EnableJms
@PropertySource("classpath:/action-monitor.properties")
@ComponentScan(basePackages="hu.ferencbalogh.actionmonitor.messagelistener")
public class MessageListenerConfiguration {
 
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