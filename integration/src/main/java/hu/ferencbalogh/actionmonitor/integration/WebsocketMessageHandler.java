package hu.ferencbalogh.actionmonitor.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class WebsocketMessageHandler implements MessageHandler {
	@Autowired
	private SimpMessagingTemplate template;

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		template.convertAndSend("/" + topic, message.getPayload());
	}
}
