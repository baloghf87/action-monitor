package hu.ferencbalogh.actionmonitor.activemq.status;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;

/**
 * 
 * Check if it is possible to connect to the ActiveMQ broker
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
public class ActiveMQStatusChecker implements ServiceStatusChecker{

	@Autowired
	private ActiveMQConnectionFactory activeMQConnectionFactory;

	@Override
	public String getName() {
		return "ActiveMQ";
	}
	
	public boolean isRunning() {
		try {
			Connection connection = activeMQConnectionFactory.createConnection();
			connection.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
