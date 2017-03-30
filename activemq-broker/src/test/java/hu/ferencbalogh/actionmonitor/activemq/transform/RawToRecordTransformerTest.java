package hu.ferencbalogh.actionmonitor.activemq.transform;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.junit.Test;
import org.mockito.Mockito;

import hu.ferencbalogh.actionmonitor.entity.Action;
import hu.ferencbalogh.actionmonitor.entity.Record;

public class RawToRecordTransformerTest {

	private static final String NAME = "name";

	private static final String TABLE = "table";

	private static final ObjectMessage MOCK_OBJECT_MESSAGE = Mockito.mock(ObjectMessage.class);

	private RawToRecordTransformer transformer = new RawToRecordTransformer();

	@Test
	public void testProducerTransformNoRows() throws JMSException {
		Session session = getMockSession();
		Message message = getMockMessage();

		Message result = transformer.producerTransform(session, null, message);
		assertEquals(result, MOCK_OBJECT_MESSAGE);

		Action expectedAction = new Action(TABLE, NAME);
		Mockito.verify(session).createObjectMessage(expectedAction);
	}

	@Test
	public void testProducerTransformOldRow() throws JMSException {
		Session session = getMockSession();

		List<Object> oldRow = getRow(1, "hello", 12345678l);
		Message message = getMockMessage(oldRow, null);

		Message result = transformer.producerTransform(session, null, message);
		assertEquals(result, MOCK_OBJECT_MESSAGE);

		Record expectedOldRecord = getRecord(oldRow);
		Action expectedAction = new Action(TABLE, NAME, expectedOldRecord, null);
		Mockito.verify(session).createObjectMessage(expectedAction);
	}

	@Test
	public void testProducerTransformNewRow() throws JMSException {
		Session session = getMockSession();

		List<Object> newRow = getRow(1, "hello", 12345678l);
		Message message = getMockMessage(null, newRow);

		Message result = transformer.producerTransform(session, null, message);
		assertEquals(result, MOCK_OBJECT_MESSAGE);

		Record expectedNewRecord = getRecord(newRow);
		Action expectedAction = new Action(TABLE, NAME, null, expectedNewRecord);
		Mockito.verify(session).createObjectMessage(expectedAction);
	}

	@Test
	public void testProducerTransformOldAndNewRows() throws JMSException {
		Session session = getMockSession();

		List<Object> oldRow = getRow(2, "see ya", 234567l);
		List<Object> newRow = getRow(1, "hello", 12345678l);
		Message message = getMockMessage(oldRow, newRow);

		Message result = transformer.producerTransform(session, null, message);
		assertEquals(result, MOCK_OBJECT_MESSAGE);

		Record expectedOldRecord = getRecord(oldRow);
		Record expectedNewRecord = getRecord(newRow);
		Action expectedAction = new Action(TABLE, NAME, expectedOldRecord, expectedNewRecord);
		Mockito.verify(session).createObjectMessage(expectedAction);
	}

	private Record getRecord(List<Object> row) {
		return new Record((int) row.get(0), (String) row.get(1), (long) row.get(2));
	}

	@Test
	public void testConsumerTransform() throws JMSException {
		Message message = getMockMessage();
		assertEquals(message, transformer.consumerTransform(null, null, message));
	}

	private List<Object> getRow(int id, String payload, long timestamp) {
		return Arrays.asList(id, payload, timestamp);
	}

	private Session getMockSession() throws JMSException {
		Session session = Mockito.mock(Session.class);
		Mockito.when(session.createObjectMessage(Mockito.anyObject())).thenReturn(MOCK_OBJECT_MESSAGE);
		return session;
	}

	private Message getMockMessage() throws JMSException {
		return getMockMessage(null, null);
	}

	private Message getMockMessage(List<Object> oldRow, List<Object> newRow) throws JMSException {
		Message message = Mockito.mock(Message.class);
		Mockito.when(message.getStringProperty(Mockito.eq(NAME))).thenReturn("TRIG_name");
		Mockito.when(message.getStringProperty(Mockito.eq(TABLE))).thenReturn(TABLE);
		Mockito.when(message.getObjectProperty(Mockito.eq("oldRow"))).thenReturn(oldRow);
		Mockito.when(message.getObjectProperty(Mockito.eq("newRow"))).thenReturn(newRow);
		return message;
	}
}
