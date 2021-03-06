package hu.ferencbalogh.actionmonitor.database.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import hu.ferencbalogh.actionmonitor.entity.Record;

/**
 * <p>Contains the available custom commands</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Component
public class DatabaseUtilCommands implements CommandMarker {

	@Autowired
	private RecordDao recordDao;

	//@formatter:off
	@CliCommand(value = "insert", help = "Insert a new row into the monitored database table")
	public String insert(@CliOption(key = { "id" }, mandatory = false, help = "id of the new record") Integer id,
						 @CliOption(key = { "payload" }, mandatory = false, help = "payload of the new record") String payload) {
		if (payload == null) {
			payload = "";
		}

		recordDao.insert(new Record(id, payload));

		return "OK";
	}

	@CliCommand(value = "update", help = "Update an existing row in the monitored database table")
	public String update(@CliOption(key = { "id" }, mandatory = true, help = "id of the row to update") int id,
						 @CliOption(key = { "payload" }, mandatory = true, help = "new payload of the record") String payload) {
		recordDao.update(new Record(id, payload));

		return "OK";
	}

	@CliCommand(value = "delete", help = "Delete an existing row from the monitored database table")
	public String delete(@CliOption(key = { "id" }, mandatory = true, help = "id of the row to update") Integer id) {
		recordDao.delete(id);
		
		return "OK";
	}
	//@formatter:on
}
