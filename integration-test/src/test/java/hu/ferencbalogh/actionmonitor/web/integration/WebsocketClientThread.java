package hu.ferencbalogh.actionmonitor.web.integration;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

public class WebsocketClientThread extends Thread {

	private BlockingQueue<String> queue;
	private String url;
	private String topic;
	private Object exitSync;
	private Object connectSync;

	public WebsocketClientThread(String url, String topic, BlockingQueue<String> queue, Object connectSync,
			Object exitSync) {
		this.url = url;
		this.topic = topic;
		this.queue = queue;
		this.connectSync = connectSync;
		this.exitSync = exitSync;
	}

	@Override
	public void run() {
		super.run();
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketClient transport = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		stompClient.setMessageConverter(new StringMessageConverter());
		stompClient.connect(url, sessionHandler);

		synchronized (exitSync) {
			try {
				exitSync.wait();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
	}

	private StompFrameHandler frameHandler = new StompFrameHandler() {

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			try {
				queue.put((String) payload);
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}

		@Override
		public Type getPayloadType(StompHeaders headers) {
			return String.class;
		}
	};

	StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
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
}
