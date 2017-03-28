package hu.ferencbalogh.actionmonitor.database;

import java.util.Arrays;

import org.hsqldb.Trigger;

public class ModificationMonitorTrigger implements Trigger {

	public void fire(int triggerType, String name, String table, Object oldRow[], Object newRow[]) {
		System.out.println(String.format("triggerType: %d, name: %s, table: %s, row1: %s, row2: %s", triggerType, name,
				table, Arrays.toString(oldRow), Arrays.toString(newRow)));
	}

}
