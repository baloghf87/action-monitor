package hu.ferencbalogh.actionmonitor.database;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Server;
import org.hsqldb.Trigger;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseMonitorTest.class)
@PropertySource("classpath:/action-monitor.properties")
public class DatabaseMonitorTest {

	private static Statement MOCK_STATEMENT;
	private static ResultSet MOCK_DATABASE_METADATA_RESULT_SET;
	private static DatabaseMetaData MOCK_DATABASE_METADATA;
	private static Connection MOCK_CONNECTION;
	private static Server MOCK_SERVER;
	private static boolean TABLE_EXISTS;

	@Autowired
	private DatabaseMonitor databaseMonitor;
	
	private String triggerClass = ModificationMonitor.class.getCanonicalName();
	
	private String tableName = "ACTIONS";

	@BeforeClass
	public static void init() throws SQLException {
		MOCK_DATABASE_METADATA_RESULT_SET = Mockito.mock(ResultSet.class);
		MOCK_DATABASE_METADATA = Mockito.mock(DatabaseMetaData.class);
		MOCK_CONNECTION = Mockito.mock(Connection.class);
		MOCK_SERVER = Mockito.mock(Server.class);
		MOCK_STATEMENT = Mockito.mock(Statement.class);

		Mockito.when(MOCK_CONNECTION.getMetaData()).thenReturn(MOCK_DATABASE_METADATA);
		Mockito.when(MOCK_DATABASE_METADATA.getTables(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(MOCK_DATABASE_METADATA_RESULT_SET);
		Mockito.when(MOCK_DATABASE_METADATA_RESULT_SET.next()).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return TABLE_EXISTS;
			}
		});
		Mockito.when(MOCK_CONNECTION.createStatement()).thenReturn(MOCK_STATEMENT);

	}

	@Test
	public void testStartsServer() throws SQLException {
		TABLE_EXISTS = true;
		initialize();
		Mockito.verify(MOCK_SERVER).start();
	}

	@Test
	public void testInitializesWhenTableDoesntExists() throws SQLException {
		TABLE_EXISTS = false;
		initialize();

		verifyStatementCalled(
				"CREATE TABLE "+tableName+" (id INT IDENTITY,payload VARCHAR(255) NOT NULL,timestamp TIMESTAMP default CURRENT_TIMESTAMP);");
		verifyStatementCalled(
				"CREATE TRIGGER trig_delete AFTER DELETE ON ACTIONS FOR EACH ROW CALL \"hu.ferencbalogh.actionmonitor.database.ModificationMonitor\";");
		verifyStatementCalled(
				"CREATE TRIGGER trig_update AFTER UPDATE ON ACTIONS FOR EACH ROW CALL \"hu.ferencbalogh.actionmonitor.database.ModificationMonitor\";");
		verifyStatementCalled(
				"CREATE TRIGGER trig_insert AFTER INSERT ON ACTIONS FOR EACH ROW CALL \"hu.ferencbalogh.actionmonitor.database.ModificationMonitor\";");
	}
	
	@Test
	public void testDoesntInitializesWhenTableExists() throws SQLException {
		TABLE_EXISTS = true;
		initialize();

		Mockito.verify(MOCK_STATEMENT, Mockito.times(0)).executeQuery(Mockito.any());
	}

	private void verifyStatementCalled(String statement) throws SQLException {
		Mockito.verify(MOCK_STATEMENT).executeUpdate(Mockito.eq(statement));
	}

	@Bean
	public Server server() throws IOException, AclFormatException {
		return MOCK_SERVER;
	}

	@Bean
	@Scope("singleton")
	public DatabaseMonitor databaseMonitor() {
		return Mockito.spy(new DatabaseMonitor());
	}

	@Bean
	public Class<Trigger> modificationTrigger() throws ClassNotFoundException {
		return (Class<Trigger>) Class.forName(triggerClass);
	}

	@Bean
	@Scope("singleton")
	public Connection connection() throws SQLException, ClassNotFoundException {
		return MOCK_CONNECTION;
	}

	private void initialize() {
		try {
			Method initialize = DatabaseMonitor.class.getDeclaredMethod("initialize");
			initialize.setAccessible(true);
			initialize.invoke(databaseMonitor);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

}
