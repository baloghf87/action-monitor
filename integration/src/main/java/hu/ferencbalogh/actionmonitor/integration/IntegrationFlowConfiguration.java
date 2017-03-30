package hu.ferencbalogh.actionmonitor.integration;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import hu.ferencbalogh.actionmonitor.activemq.broker.ActiveMQBrokerConfiguration;
import hu.ferencbalogh.actionmonitor.database.DatabaseMonitorConfiguration;
import hu.ferencbalogh.actionmonitor.database.ModificationMonitor;
import hu.ferencbalogh.actionmonitor.entity.Action;

@Configuration
@EnableIntegration
@ComponentScan
@Import(value = { DatabaseMonitorConfiguration.class, ActiveMQBrokerConfiguration.class })
public class IntegrationFlowConfiguration {
	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private SimpMessagingTemplate template;

	@PostConstruct
	public void addModificationListener() {
		ModificationMonitor.addListener(databaseModificationListener());
	}

	@Bean
	public JmsSendingModificationListener databaseModificationListener(){
		return new JmsSendingModificationListener();
	};
	
	@Bean
	public DirectChannel inboundChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel outboundChannel() {
		return new DirectChannel();
	}

	@Transformer(inputChannel = "inboundChannel", outputChannel = "outboundChannel")
	public String actionToStringTransformer(Action action) {
		String text = String.format("Table: %s, Action: %s:, Old: %s, New: %s", action.getTable(), action.getAction(),
				action.getOldRecord(), action.getNewRecord());
		return text;
	}

	@Bean
	public IntegrationFlow inboundFlow() {
		return IntegrationFlows.from(Jms.inboundGateway(connectionFactory).destination("actions").get())
				.channel(inboundChannel()).get();
	}

	@Bean
	public IntegrationFlow outboundFlow() {
		return IntegrationFlows.from(outboundChannel()).handle(websocketMessageHandler()).get();
	}

	@Bean
	public MessageHandler websocketMessageHandler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				template.convertAndSend("/actions", message.getPayload());
			}
		};
	}

}