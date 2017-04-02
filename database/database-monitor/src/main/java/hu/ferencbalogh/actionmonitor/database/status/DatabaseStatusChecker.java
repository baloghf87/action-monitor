package hu.ferencbalogh.actionmonitor.database.status;

import org.hsqldb.HsqlException;
import org.hsqldb.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;

/**
 * Check the status of the HSQLDB {@link Server} 
 */
@Component
public class DatabaseStatusChecker implements ServiceStatusChecker{

	@Autowired
	private Server server;

	@Override
	public String getName() {
		return "Database";
	}
	
	public boolean isRunning() {
		try {
			server.checkRunning(true);
			return true;
		} catch (HsqlException e) {
			return false;
		}
	}
}
