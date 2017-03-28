package hu.ferencbalogh.actionmonitor.database.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseUtilBannerProvider extends DefaultBannerProvider  {

	public String getBanner() {
		//@formatter:off
		return 	"/---------------------------------\\"+IOUtils.LINE_SEPARATOR+
				"| Action-monitor database utility |"+IOUtils.LINE_SEPARATOR+
				"\\---------------------------------/"+IOUtils.LINE_SEPARATOR;
		//@formatter:on
	}

	public String getWelcomeMessage() {
		return "To list available commands use the command 'help'"+IOUtils.LINE_SEPARATOR;
	}
	
	@Override
	public String getProviderName() {
		return "Action-monitor Database Utility";
	}
}