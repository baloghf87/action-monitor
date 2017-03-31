package hu.ferencbalogh.actionmonitor.integration;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.MessageHandler;

import hu.ferencbalogh.actionmonitor.database.monitor.DatabaseMonitor;
import hu.ferencbalogh.actionmonitor.database.monitor.ModificationTrigger;
import hu.ferencbalogh.actionmonitor.entity.Action;

/**
 * <p>
 * Contains {@link IntegrationFlow}(s)
 * </p>
 * 
 * <p>
 * <ol>
 * <li>Action objects from ActiveMQ -> inboundChannel -> Transform to string ->
 * outboundChannel -> WebSocket clients</li>
 * </ol>
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Configuration
@EnableIntegration
@ComponentScan
public class IntegrationFlowConfiguration {
	private static final Logger log = LoggerFactory.getLogger(DatabaseMonitor.class);

	@Autowired
	private ConnectionFactory connectionFactory;

	@Value("${websocket.stomp.topic}")
	private String topic;

	@PostConstruct
	public void addModificationListener() {
		ModificationTrigger.addListener(databaseModificationListener());
	}

	@Bean
	public JmsSendingModificationListener databaseModificationListener() {
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
		log.debug("Converted action to string: {}, result: {} ", action, text);
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
		return new WebsocketMessageHandler();
	}

}