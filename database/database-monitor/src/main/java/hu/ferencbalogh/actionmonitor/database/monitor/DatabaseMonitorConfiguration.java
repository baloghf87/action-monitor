package hu.ferencbalogh.actionmonitor.database.monitor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.Server;
import org.hsqldb.Trigger;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl.AclFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/action-monitor.properties")
public class DatabaseMonitorConfiguration {

	@Autowired
    private Environment environment;

	@Bean 
	public HsqlProperties hsqlProperties(){
		HsqlProperties hsqlProperties = new HsqlProperties();
		hsqlProperties.setProperty("server.database.0", "file:" + environment.getProperty("hsql.filename"));
		hsqlProperties.setProperty("server.dbname.0", environment.getProperty("hsql.alias"));
		return hsqlProperties;
	}
	
	@Bean
	public Server server() throws IOException, AclFormatException{
		Server server = new Server();
		server.setProperties(hsqlProperties());
		return server;		
	}
	
	@Bean(initMethod="initialize")
	@Scope("singleton")
	public DatabaseMonitor databaseMonitor() {
		return new DatabaseMonitor();
	}
	
	@Bean
	public Class<Trigger> modificationTrigger() throws ClassNotFoundException{
		return (Class<Trigger>) Class.forName(environment.getProperty("hsql.trigger.class"));
	}
	
	@Bean
	@DependsOn("server")
	public Connection connection() throws SQLException, ClassNotFoundException{
		String jdbcDriver = environment.getProperty("jdbc.driver");
		String jdbcPassword = environment.getProperty("jdbc.password");
		String jdbcUser= environment.getProperty("jdbc.user");
		String jdbcUrl= environment.getProperty("jdbc.url");
		
		Class.forName(jdbcDriver);
		return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
	}
}
