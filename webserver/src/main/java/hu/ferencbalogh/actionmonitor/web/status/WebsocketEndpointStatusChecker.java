package hu.ferencbalogh.actionmonitor.web.status;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;

/**
 * <p>
 * Check the status of a Websocket endpoint
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
public class WebsocketEndpointStatusChecker implements ServiceStatusChecker {

	private String url;
	private String endpoint;

	/**
	 * Create a new {@link WebsocketEndpointStatusChecker} instance
	 * 
	 * @param url
	 *            the root url of the websocket
	 * @param endpoint
	 *            the name of the endpoint
	 */
	@Autowired
	public WebsocketEndpointStatusChecker(@Value("${statuschecker.websocket.root-url}") String url,
			@Value("${websocket.stomp.endpoint}") String endpoint) {
		this.url = url;
		this.endpoint = endpoint;
	}

	@Override
	public String getName() {
		return "WebSocket endpoint";
	}

	public boolean isRunning() {
		List<Transport> transports = Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketClient webSocketClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());

		AtomicBoolean connected = new AtomicBoolean(false);
		Object connectionSync = new Object();

		stompClient.connect(url + "/" + endpoint, new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				connected.set(true);
				done();
			}

			@Override
			public void handleTransportError(StompSession session, Throwable exception) {
				done();
			}

			private void done() {
				synchronized (connectionSync) {
					connectionSync.notify();
				}
			}
		});

		synchronized (connectionSync) {
			try {
				connectionSync.wait(2000);
			} catch (InterruptedException e) {
			}
		}

		return connected.get();
	}
}
