package hu.ferencbalogh.actionmonitor.database.util;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * <p>Provide custom prompt</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseUtilPromptProvider extends DefaultPromptProvider {

	@Override
	public String getPrompt() {
		return "db-util>";
	}

	
	@Override
	public String getProviderName() {
		return "Database utility prompt provider";
	}

}
