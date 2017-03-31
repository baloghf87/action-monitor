package hu.ferencbalogh.actionmonitor.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

/**
 * <p>
 * Webserver application entry point
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "hu.ferencbalogh.actionmonitor", excludeFilters = @Filter(type = FilterType.REGEX, pattern = "hu\\.ferencbalogh\\.actionmonitor\\.database\\.util\\..*"))
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		log.info("Starting Action monitor application");
		SpringApplication.run(Application.class, args);
	}
}