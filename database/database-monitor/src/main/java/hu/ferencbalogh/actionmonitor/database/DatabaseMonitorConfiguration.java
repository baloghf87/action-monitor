package hu.ferencbalogh.actionmonitor.database;

import org.hsqldb.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/action-monitor.properties")
public class DatabaseMonitorConfiguration {

	@Autowired
    private Environment environment;
	
	@Bean(initMethod="start")
	@Scope("singleton")
	public DatabaseMonitor databaseMonitor() {
		return new DatabaseMonitor();
	}
	
	@Bean
	public Class<Trigger> modificationTrigger() throws ClassNotFoundException{
		return (Class<Trigger>) Class.forName(environment.getProperty("hsql.trigger.class"));
	}
}
