package hu.ferencbalogh.actionmonitor.activemq.sender;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
 
@Configuration
@EnableJms
@ComponentScan(basePackages="hu.ferencbalogh.actionmonitor.activemq.sender")
public class ActiveMQSenderConfiguration {
}