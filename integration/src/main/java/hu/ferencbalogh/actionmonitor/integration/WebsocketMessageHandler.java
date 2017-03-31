package hu.ferencbalogh.actionmonitor.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * <p>Sends messages to the defined topic on websocket(s)</p>
 *
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */

public class WebsocketMessageHandler implements MessageHandler {
	
	private static final Logger log = LoggerFactory.getLogger(WebsocketMessageHandler.class);
	
	@Autowired
	private SimpMessagingTemplate template;

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		log.debug("Sending message to topic '{}': {}", topic, message.getPayload());
		template.convertAndSend("/" + topic, message.getPayload());
	}
}
