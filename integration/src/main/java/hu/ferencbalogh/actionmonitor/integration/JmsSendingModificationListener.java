package hu.ferencbalogh.actionmonitor.integration;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import hu.ferencbalogh.actionmonitor.database.monitor.ModificationTrigger.ModificationListener;

/**
 * <p>
 * {@link ModificationListener} implementation which sends JMS messages
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class JmsSendingModificationListener implements ModificationListener {

	private static final Logger log = LoggerFactory.getLogger(JmsSendingModificationListener.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void onModification(int triggerType, String name, String table, List<Object> oldRow, List<Object> newRow) {
		jmsTemplate.send(new MessageCreator() {
			public javax.jms.Message createMessage(Session session) throws JMSException {
				javax.jms.Message message = session.createMessage();
				message.setObjectProperty("oldRow", oldRow);
				message.setObjectProperty("newRow", newRow);
				message.setStringProperty("name", name);
				message.setStringProperty("table", table);
				log.debug("Sending message via JMS: {}", message);
				return message;
			}
		});
	}
}
