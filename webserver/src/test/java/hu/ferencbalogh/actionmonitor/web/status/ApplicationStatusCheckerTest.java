package hu.ferencbalogh.actionmonitor.web.status;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;
import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;

@ContextConfiguration(classes = { ApplicationStatusCheckerTest.class, ApplicationStatusChecker.class })
public class ApplicationStatusCheckerTest extends AbstractSpringTest {

	@Autowired
	private ApplicationStatusChecker applicationStatusChecker;

	private static boolean RUNNING_1;
	private static boolean RUNNING_2;

	@Bean
	public ServiceStatusChecker statusChecker1() {
		return new ServiceStatusChecker() {

			@Override
			public boolean isRunning() {
				return RUNNING_1;
			}

			@Override
			public String getName() {
				return "1";
			}
		};
	}

	@Bean
	public ServiceStatusChecker statusChecker2() {
		return new ServiceStatusChecker() {

			@Override
			public boolean isRunning() {
				return RUNNING_2;
			}

			@Override
			public String getName() {
				return "2";
			}
		};
	}

	@Test
	public void testReturnOKWhenAllServicesAreRunning() {
		RUNNING_1 = true;
		RUNNING_2 = true;
		assertEquals("OK", applicationStatusChecker.getStatus());
	}
	
	@Test
	public void testReturnThereAreProblemsWithOneWhenOneServiceIsNotRunning() {
		RUNNING_1 = false;
		RUNNING_2 = true;
		assertEquals("There are problems with: 1", applicationStatusChecker.getStatus());
	}
	
	@Test
	public void testReturnThereAreProblemsWithMultipleWhenMultipleServicesAreNotRunning() {
		RUNNING_1 = false;
		RUNNING_2 = false;
		assertEquals("There are problems with: 1, 2", applicationStatusChecker.getStatus());
	}


}
