package hu.ferencbalogh.actionmonitor.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hsqldb.HsqlException;
import org.hsqldb.server.Server;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import hu.ferencbalogh.actionmonitor.database.status.DatabaseStatusChecker;
import hu.ferencbalogh.actionmonitor.status.AbstractServiceStatusCheckerTest;

/**
 * Unit test for {@link DatabaseStatusChecker} 
 */
@ContextConfiguration(classes = DatabaseStatusCheckerTest.class)
public class DatabaseStatusCheckerTest extends AbstractServiceStatusCheckerTest {

	@Autowired
	private Server server;

	@Autowired
	private DatabaseStatusChecker databaseStatusChecker;

	@Bean
	public Server server() {
		return Mockito.mock(Server.class);
	}

	@Bean
	public DatabaseStatusChecker DatabaseStatusChecker() {
		return new DatabaseStatusChecker();
	}

	@Before
	public void resetMock() {
		Mockito.reset(server);
	}

	@Override
	public void testReportFalseWhenNotRunning() {
		Mockito.doThrow(new HsqlException(new Exception(), "", 0)).when(server).checkRunning(true);
		assertFalse(databaseStatusChecker.isRunning());
	}

	@Override
	public void testReportTrueWhenRunning() throws Exception {
		assertTrue(databaseStatusChecker.isRunning());
	}
	
	@Override
	public void testGetName() {
		assertEquals("Database", databaseStatusChecker.getName());
	}
}
