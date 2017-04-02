package hu.ferencbalogh.actionmonitor.status;

import org.junit.Test;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;

/**
 *
 * Abstract ancestor class for testing {@link ServiceStatusChecker}s
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public abstract class AbstractServiceStatusCheckerTest extends AbstractSpringTest {
	/**
	 * Test that {@link ServiceStatusChecker#isRunning()} returns true when the
	 * service is running
	 */
	@Test
	public abstract void testReportTrueWhenRunning() throws Exception;

	/**
	 * Test that {@link ServiceStatusChecker#isRunning()} returns false when the
	 * service is not running
	 */
	@Test
	public abstract void testReportFalseWhenNotRunning() throws Exception;

	/**
	 * Test that {@link ServiceStatusChecker#getName()} returns the name of the
	 * service
	 */
	@Test
	public abstract void testGetName();
}
