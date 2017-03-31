package hu.ferencbalogh.actionmonitor.database.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.stereotype.Component;

/**
 * <p>Provide custom banner and welcome message with instructions</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseUtilBannerProvider extends DefaultBannerProvider {

	public String getBanner() {
		//@formatter:off
		return
		"		   ___         _    _                       "	+IOUtils.LINE_SEPARATOR+
		"		  / _ \\       | |  (_)                     "	+IOUtils.LINE_SEPARATOR+
		"		 / /_\\ \\  ___ | |_  _   ___   _ __         "	+IOUtils.LINE_SEPARATOR+
		"		 |  _  | / __|| __|| | / _ \\ | '_ \\        "	+IOUtils.LINE_SEPARATOR+
		"		 | | | || (__ | |_ | || (_) || | | |       "	+IOUtils.LINE_SEPARATOR+
		"		 \\_| |_/ \\___| \\__||_| \\___/ |_| |_|       "+IOUtils.LINE_SEPARATOR+
		"		 ___  ___               _  _               "	+IOUtils.LINE_SEPARATOR+
		"		 |  \\/  |              (_)| |              "	+IOUtils.LINE_SEPARATOR+
		"		 | .  . |  ___   _ __   _ | |_  ___   _ __ "	+IOUtils.LINE_SEPARATOR+
		"		 | |\\/| | / _ \\ | '_ \\ | || __|/ _ \\ | '__|"+IOUtils.LINE_SEPARATOR+
		"		 | |  | || (_) || | | || || |_| (_) || |   "	+IOUtils.LINE_SEPARATOR+
		"		 \\_|  |_/ \\___/ |_| |_||_| \\__|\\___/ |_|   "+IOUtils.LINE_SEPARATOR;
		//@formatter:on
	}
	
	public String getWelcomeMessage() {
		//@formatter:off
		return 	"To list all available commands try 'help' or press TAB."+IOUtils.LINE_SEPARATOR+IOUtils.LINE_SEPARATOR+
				"Use 'insert', 'update' and 'delete' commands to interact with the database, examples:"+IOUtils.LINE_SEPARATOR+
				"- Insert a row:"+IOUtils.LINE_SEPARATOR+
				"  - with autogenerated id without payload:  insert"+IOUtils.LINE_SEPARATOR+
				"  - with custom id without payload:         insert --id 123"+IOUtils.LINE_SEPARATOR+
				"  - with autogenerated id with payload:     insert --payload \"this is a multi-word test\""+IOUtils.LINE_SEPARATOR+
				"  - with custom id with payload:            insert --id 234 --payload single_word_test"+IOUtils.LINE_SEPARATOR+
				"- Update a row:  update --id 123 --payload hello"+IOUtils.LINE_SEPARATOR+
				"- Delete a row:  delete --id 123"+
				IOUtils.LINE_SEPARATOR;
		//@formatter:on		
	}

	@Override
	public String getProviderName() {
		return "Action-monitor Database Utility";
	}
}