package hu.ferencbalogh.actionmonitor.web.integrationtest.util;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

/**
 * <p>
 * Websocket client that puts incoming messages in a {@link BlockingQueue}
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class WebsocketClientThread extends Thread {

	private BlockingQueue<String> queue;
	private String url;
	private String topic;
	private Object exitSync;
	private Object connectSync;
	private WebSocketStompClient stompClient;

	/**
	 * 
	 * Create a new WebsocketClientThread
	 * 
	 * @param url
	 *            the URL to connect to
	 * @param topic
	 *            the topic to subscribe to
	 * @param queue
	 *            the queue to put the incoming messages into
	 * @param connectSync
	 *            notify when connected
	 * @param exitSync
	 *            exit when gets notified
	 */
	public WebsocketClientThread(String url, String topic, BlockingQueue<String> queue, Object connectSync,
			Object exitSync) {
		this.url = url;
		this.topic = topic;
		this.queue = queue;
		this.connectSync = connectSync;
		this.exitSync = exitSync;

		List<Transport> transports = Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketClient webSocketClient = new SockJsClient(transports);
		stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());
	}

	@Override
	public void run() {
		stompClient.connect(url, sessionHandler);

		synchronized (exitSync) {
			try {
				exitSync.wait();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}

	private StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
		@Override
		public Type getPayloadType(StompHeaders headers) {
			return String.class;
		}

		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			session.subscribe(topic, frameHandler);

			synchronized (connectSync) {
				connectSync.notify();
			}
		}
	};
	private StompFrameHandler frameHandler = new StompFrameHandler() {

		@Override
		public Type getPayloadType(StompHeaders headers) {
			return String.class;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			try {
				queue.put((String) payload);
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	};

}
