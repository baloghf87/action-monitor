package hu.ferencbalogh.actionmonitor.web.status;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;

/**
 * <p>
 * Check the status of all services of the application
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
public class ApplicationStatusChecker {

	private static final Logger log = LoggerFactory.getLogger(ApplicationStatusChecker.class);

	/**
	 * All the {@link ServiceStatusChecker}s in the {@link ApplicationContext}
	 */
	@Autowired
	private List<ServiceStatusChecker> serviceStatusCheckers;

	/**
	 * Invoke all the configured {@link ServiceStatusChecker}s and return a
	 * {@link String} describing the status of the application
	 * 
	 * @return "OK" if all the services are running, otherwise the list of
	 *         problematic services
	 */
	public String getStatus() {
		log.info("Checking status of services...");

		List<String> servicesNotRunning = serviceStatusCheckers.stream().filter(sc -> !checkStatus(sc))
				.map(sc -> sc.getName()).collect(Collectors.toList());

		log.info("...done");

		if (servicesNotRunning.size() > 0) {
			String problematicServices = servicesNotRunning.stream().collect(Collectors.joining(", "));
			return String.format("There are problems with: %s", problematicServices);
		} else {
			return "OK";
		}
	}

	/**
	 * Invoke {@link ServiceStatusChecker#isRunning()} and log the result
	 * 
	 * @param serviceStatusChecker
	 *            the {@link ServiceStatusChecker} instance
	 * @return the value returned by {@link ServiceStatusChecker#isRunning()}
	 */
	private boolean checkStatus(ServiceStatusChecker serviceStatusChecker) {
		boolean running = serviceStatusChecker.isRunning();
		log.info("{} is {}", serviceStatusChecker.getName(), running ? "running" : "not running");
		return running;
	}
}
