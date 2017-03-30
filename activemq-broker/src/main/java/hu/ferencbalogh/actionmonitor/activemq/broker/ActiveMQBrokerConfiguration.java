package hu.ferencbalogh.actionmonitor.activemq.broker;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.MessageTransformer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;

import hu.ferencbalogh.actionmonitor.activemq.transform.RawToRecordTransformer;

@Configuration
@PropertySource("classpath:/action-monitor.properties")
public class ActiveMQBrokerConfiguration {

	@Value("${message-broker.url}")
	private String brokerUrl;

	@Value("${message-broker.destination}")
	private String destination;

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUrl);
		connectionFactory.setTransformer(rawToRecordTransformer());
		connectionFactory.setTrustedPackages(Arrays.asList("java.lang", "hu.ferencbalogh.actionmonitor"));
		return connectionFactory;
	}

	@Bean
	@Scope("singleton")
	public MessageTransformer rawToRecordTransformer() {
		return new RawToRecordTransformer();
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDefaultDestinationName(destination);
		return template;
	}

}
