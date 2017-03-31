package hu.ferencbalogh.actionmonitor.integration;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;

@ContextConfiguration(classes = WebsocketMessageHandlerTest.class)
public class WebsocketMessageHandlerTest extends AbstractSpringTest {

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Autowired
	private WebsocketMessageHandler websocketMessageHandler;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Bean
	@Scope("singleton")
	public WebsocketMessageHandler websocketMessageHandler() {
		return new WebsocketMessageHandler();
	}

	@Bean
	@Scope("singleton")
	public SimpMessagingTemplate simpMessagingTemplate() {
		return Mockito.mock(SimpMessagingTemplate.class);
	}

	@Test
	public void testHandleMessage() {
		Object payload = new Object();
		Message<Object> message = Mockito.mock(Message.class);
		Mockito.when(message.getPayload()).thenReturn(payload);
		websocketMessageHandler.handleMessage(message);
		Mockito.verify(simpMessagingTemplate).convertAndSend("/"+topic, payload);
	}
}
