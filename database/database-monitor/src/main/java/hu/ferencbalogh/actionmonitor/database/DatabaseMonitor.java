package hu.ferencbalogh.actionmonitor.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Trigger;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DatabaseMonitor {

	@Value("${jdbc.driver}")
	private String jdbcDriver;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Value("${jdbc.user}")
	private String jdbcUser;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${hsql.alias}")
	private String dbAlias;

	@Value("${hsql.filename}")
	private String dbFileName;

	@Value("${hsql.tablename}")
	private String tableName;
	
	@Autowired
	private Class<Trigger> triggerClass;

	private Connection connection;

	public void start() throws Exception {
		startServer();
		initializeIfNeeded();
	}

	private void startServer() throws IOException, AclFormatException {
		HsqlProperties p = new HsqlProperties();
		p.setProperty("server.database.0", "file:" + dbFileName);
		p.setProperty("server.dbname.0", dbAlias);

		Server server = new Server();
		server.setProperties(p);
		// server.setLogWriter(null); // can use custom writer
		// server.setErrWriter(null); // can use custom writer
		server.start();
	}

	private boolean tableExists() throws SQLException {
		ResultSet tables = connection.getMetaData().getTables(null, null, tableName, new String[] { "TABLE" });
		return tables.next();
	}

	public void initializeIfNeeded() throws ClassNotFoundException, SQLException {
		Class.forName(jdbcDriver);
		connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

		if (tableExists()) {
			System.out.println("db already initialized");
		} else {
			initialize();
		}
	}

	private void executeUpdate(String statement) throws SQLException {
		System.out.println("Executing: " + statement);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(statement);
		stmt.close();
	}

	private void initialize() throws SQLException {
		createTable();
		
		createInsertTrigger();
		createUpdateTrigger();
		createDeleteTrigger();
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
