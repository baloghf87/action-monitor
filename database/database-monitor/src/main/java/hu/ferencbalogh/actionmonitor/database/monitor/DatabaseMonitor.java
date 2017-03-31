package hu.ferencbalogh.actionmonitor.database.monitor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Trigger;
import org.hsqldb.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

public class DatabaseMonitor {

	@Value("${hsql.tablename}")
	private String tableName;

	@Autowired
	private Class<Trigger> triggerClass;

	@Autowired
	private Server server;

	@Autowired
	private ApplicationContext applicationContext;

	private Connection connection;
	
	private void initialize() throws ClassNotFoundException, SQLException {
		server.start();
		connection = applicationContext.getBean(Connection.class);

		if (!tableExists()) {
			createTableAndTriggers();
		}
	}

	private boolean tableExists() throws SQLException {
		ResultSet tables = connection.getMetaData().getTables(null, null, tableName, new String[] { "TABLE" });
		return tables.next();
	}
	
	private void createTableAndTriggers() throws SQLException {
		createTable();
		
		createInsertTrigger();
		createUpdateTrigger();
		createDeleteTrigger();
	}

	private void executeUpdate(String statement) throws SQLException {
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
