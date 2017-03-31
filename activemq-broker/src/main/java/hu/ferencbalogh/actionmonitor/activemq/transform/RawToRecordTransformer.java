package hu.ferencbalogh.actionmonitor.activemq.transform;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.MessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import hu.ferencbalogh.actionmonitor.entity.Action;
import hu.ferencbalogh.actionmonitor.entity.Record;

/**
 * 
 * Create a {@link Message} containing an {@link Action} from the raw data
 * coming from the database
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class RawToRecordTransformer implements MessageTransformer {

	private static final Logger log = LoggerFactory.getLogger(RawToRecordTransformer.class);
	
	@Value("${action.name}")
	private String name;

	@Value("${action.trigger-prefix}")
	private String triggerPrefix;

	@Value("${action.table}")
	private String table;

	@Value("${action.oldRow}")
	private String oldRowName;

	@Value("${action.newRow}")
	private String newRowName;

	@Value("${db.index.id}")
	private int indexId;

	@Value("${db.index.payload}")
	private int indexPayload;

	@Value("${db.index.timestamp}")
	private int indexTimestamp;

	@Override
	public Message producerTransform(Session session, MessageProducer producer, Message message) throws JMSException {
		String actionName = message.getStringProperty(name).substring(triggerPrefix.length());
		String tableName = message.getStringProperty(table);

		Action action = new Action(tableName, actionName);

		List<Object> oldRow = (List<Object>) message.getObjectProperty(oldRowName);
		if (oldRow != null) {
			int id = (int) oldRow.get(indexId);
			String payload = (String) oldRow.get(indexPayload);
			long timestamp = (long) oldRow.get(indexTimestamp);
			Record record = new Record(id, payload, timestamp);
			action.setOldRecord(record);
		}

		List<Object> newRow = (List<Object>) message.getObjectProperty(newRowName);
		if (newRow != null) {
			int id = (int) newRow.get(indexId);
			String payload = (String) newRow.get(indexPayload);
			long timestamp = (long) newRow.get(indexTimestamp);
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
