package hu.ferencbalogh.actionmonitor.status;

/**
 * 
 * Interface to be implemented by components checking the status of services
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public interface ServiceStatusChecker {

	/**
	 * @return true if the service is running
	 */
	boolean isRunning();

	/**
	 * @return the name of the service
	 */
	String getName();
}
