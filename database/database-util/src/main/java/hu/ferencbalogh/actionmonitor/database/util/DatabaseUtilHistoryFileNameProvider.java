package hu.ferencbalogh.actionmonitor.database.util;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * <p>Provide custom history filename</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseUtilHistoryFileNameProvider extends DefaultHistoryFileNameProvider {

	public String getHistoryFileName() {
		return "db-util.log";
	}

	@Override
	public String getProviderName() {
		return "Database utility history file name provider";
	}
	
}
