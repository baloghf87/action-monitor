package hu.ferencbalogh.actionmonitor.activemq.receiver;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.jms.JMSException;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	private Set<MessageListener> listeners = new LinkedHashSet<MessageListener>();

	public boolean register(MessageListener listener) {
		return listeners.add(listener);
	}

	public boolean unregister(MessageListener listener) {
		return listeners.remove(listener);
	}

	@JmsListener(destination = "${message-broker.destination}")
	public void receiveMessage(final Message<?> message) throws JMSException {
		listeners.stream().forEach(l -> l.onMessage((String) message.getPayload()));
	}
	
	public interface MessageListener {
		void onMessage(String message); 
	}
}