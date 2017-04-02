package hu.ferencbalogh.actionmonitor.web.integrationtest.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/action-monitor.properties")
@ImportResource("/META-INF/spring/database-util.xml")
public class IntegrationTestContext {
}
