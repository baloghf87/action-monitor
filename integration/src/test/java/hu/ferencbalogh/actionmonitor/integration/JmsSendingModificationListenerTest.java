package hu.ferencbalogh.actionmonitor.integration;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;

@ContextConfiguration(classes = JmsSendingModificationListenerTest.class)
public class JmsSendingModificationListenerTest extends AbstractSpringTest {

	@Autowired
	private JmsSendingModificationListener jmsSendingModificationListener;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Bean
	private JmsTemplate jmsTemplate() {
		JmsTemplate mockJmsTemplate = Mockito.mock(JmsTemplate.class);
		return mockJmsTemplate;
	}

	@Bean
	public JmsSendingModificationListener jmsSendingModificationListener() {
		return new JmsSendingModificationListener();
	}

	@Test
	public void testOnModification() throws JMSException {
		String name = "name";
		String table = "table";
		List<Object> oldRow = Arrays.asList("1", "2", "3");
		List<Object> newRow = Arrays.asList("2", "3", "4");
		jmsSendingModificationListener.onModification(1, name, table, oldRow, newRow);

		ArgumentCaptor<MessageCreator> argument = ArgumentCaptor.forClass(MessageCreator.class);
		Mockito.verify(jmsTemplate).send(argument.capture());
		MessageCreator messageCreator = argument.getValue();

		Session mockSession = Mockito.mock(Session.class);
		Message mockMessage = Mockito.mock(Message.class);
		Mockito.when(mockSession.createMessage()).thenReturn(mockMessage);
		Message createdMessage = messageCreator.createMessage(mockSession);
		assertEquals(mockMessage, createdMessage);
		
		Mockito.verify(mockMessage).setStringProperty("name", name);
		Mockito.verify(mockMessage).setStringProperty("table", table);
		Mockito.verify(mockMessage).setObjectProperty("oldRow", oldRow);
		Mockito.verify(mockMessage).setObjectProperty("newRow", newRow);
	}
}
