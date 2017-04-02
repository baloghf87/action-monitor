package hu.ferencbalogh.actionmonitor.activemq.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.JmsException;
import org.springframework.test.context.ContextConfiguration;

import hu.ferencbalogh.actionmonitor.activemq.status.ActiveMQStatusChecker;
import hu.ferencbalogh.actionmonitor.status.AbstractServiceStatusCheckerTest;

@ContextConfiguration(classes = {ActiveMQStatusCheckerTest.class,ActiveMQStatusChecker.class})
public class ActiveMQStatusCheckerTest extends AbstractServiceStatusCheckerTest {

	@Autowired
	private ActiveMQConnectionFactory activeMQConnectionFactory;

	@Autowired
	private ActiveMQStatusChecker activeMQStatusChecker;

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		return Mockito.mock(ActiveMQConnectionFactory.class);
	}

	@Before
	public void resetMock() {
		Mockito.reset(activeMQConnectionFactory);
	}

	@Override
	public void testReportTrueWhenRunning() throws JMSException {
		Connection mockConnection = Mockito.mock(Connection.class);
		Mockito.when(activeMQConnectionFactory.createConnection()).thenReturn(mockConnection);
		assertTrue(activeMQStatusChecker.isRunning());
	}

	@Override
	public void testReportFalseWhenNotRunning() throws Exception {
		Mockito.when(activeMQConnectionFactory.createConnection()).thenThrow(new JmsException("") {
		});
		assertFalse(activeMQStatusChecker.isRunning());
	}
	
	@Override
	public void testGetName() {
		assertEquals("ActiveMQ", activeMQStatusChecker.getName());
	}
}
