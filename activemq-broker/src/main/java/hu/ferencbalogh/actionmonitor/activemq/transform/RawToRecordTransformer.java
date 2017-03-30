package hu.ferencbalogh.actionmonitor.activemq.transform;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.MessageTransformer;

import hu.ferencbalogh.actionmonitor.entity.Action;
import hu.ferencbalogh.actionmonitor.entity.Record;

public class RawToRecordTransformer implements MessageTransformer {
	@Override
	public Message producerTransform(Session session, MessageProducer producer, Message message) throws JMSException {
		String actionName = message.getStringProperty("name").substring("TRIG_".length());
		String table = message.getStringProperty("table");

		Action action = new Action(table, actionName);

		List<Object> oldRow = (List<Object>) message.getObjectProperty("oldRow");
		if (oldRow != null) {
			int id = (int) oldRow.get(0);
			String payload = (String) oldRow.get(1);
			long timestamp = (long) oldRow.get(2);
			Record record = new Record(id, payload, timestamp);
			action.setOldRecord(record);
		}

		List<Object> newRow = (List<Object>) message.getObjectProperty("newRow");
		if (newRow != null) {
			int id = (int) newRow.get(0);
			String payload = (String) newRow.get(1);
			long timestamp = (long) newRow.get(2);
			Record record = new Record(id, payload, timestamp);
			action.setNewRecord(record);
		}

		return session.createObjectMessage(action);
	}

	@Override
	public Message consumerTransform(Session session, MessageConsumer consumer, Message message) throws JMSException {
		return message;
	}
}
