package hu.ferencbalogh.actionmonitor.database.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Trigger;
import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * Abstract ancestor class for tests using {@link ApplicationContext}
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class DatabaseMonitor {

	private static final Logger log = LoggerFactory.getLogger(DatabaseMonitor.class);

	@Value("${hsql.tablename}")
	private String tableName;

	@Autowired
	private Class<Trigger> triggerClass;

	@Autowired
	private Server server;

	@Autowired
	private ApplicationContext applicationContext;

	private Connection connection;

	/**
	 * Start HSQLDB and create tables if not yet exist
	 */
	@SuppressWarnings("unused")
	private void initialize() throws ClassNotFoundException, SQLException {
		log.info("Starting Database");

		server.start();
		connection = applicationContext.getBean(Connection.class);

		if (!tableExists()) {
			log.info("Initializing database");
			createTableAndTriggers();
		} else {
			log.info("Database already initialized");
		}
	}

	/**
	 * Look for the configured tablename in database metadata
	 * 
	 * @return true if the table already exists
	 */
	private boolean tableExists() throws SQLException {
		ResultSet tables = connection.getMetaData().getTables(null, null, tableName, new String[] { "TABLE" });
		return tables.next();
	}

	private void createTableAndTriggers() throws SQLException {
		log.info("Creating table");
		createTable();

		log.info("Creating triggers");
		createInsertTrigger();
		createUpdateTrigger();
		createDeleteTrigger();
	}

	private void executeUpdate(String statement) throws SQLException {
		log.debug("Executing: {}", statement);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(statement);
		stmt.close();
	}

	private void createDeleteTrigger() throws SQLException {
		executeUpdate("CREATE TRIGGER trig_insert AFTER INSERT ON ACTIONS FOR EACH ROW CALL \""
				+ triggerClass.getCanonicalName() + "\";");
	}

	private void createUpdateTrigger() throws SQLException {
		executeUpdate("CREATE TRIGGER trig_update AFTER UPDATE ON ACTIONS FOR EACH ROW CALL \""
				+ triggerClass.getCanonicalName() + "\";");
	}

	private void createInsertTrigger() throws SQLException {
		executeUpdate("CREATE TRIGGER trig_delete AFTER DELETE ON ACTIONS FOR EACH ROW CALL \""
				+ triggerClass.getCanonicalName() + "\";");
	}

	private void createTable() throws SQLException {
		//@formatter:off
		executeUpdate("CREATE TABLE " + tableName + " (" + 
			"id INT IDENTITY," + 
			"payload VARCHAR(255) NOT NULL,"
			+ "timestamp TIMESTAMP default CURRENT_TIMESTAMP" + 
		");");
		//@formatter:on
	}
}
