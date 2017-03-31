package hu.ferencbalogh.actionmonitor.database.monitor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hsqldb.Trigger;
import org.hsqldb.types.TimestampData;

public class ModificationMonitor implements Trigger {

	private static final int INDEX_TIMESTAMP = 2;
	
	private static Set<ModificationListener> modificationListeners = new LinkedHashSet<ModificationListener>();

	public static boolean addListener(ModificationListener listener) {
		return modificationListeners.add(listener);
	}

	public static boolean removeListener(ModificationListener listener) {
		return modificationListeners.remove(listener);
	}

	public static interface ModificationListener {
		void onModification(int triggerType, String name, String table, List<Object> oldRow, List<Object> newRow);
	}

	public void fire(int triggerType, String name, String table, Object oldRow[], Object newRow[]) {
		modificationListeners.forEach(l -> {
			List<Object> oldRowAsList = null;
			List<Object> newRowAsList = null;

			if (oldRow != null) {
				oldRow[INDEX_TIMESTAMP] = ((TimestampData) oldRow[INDEX_TIMESTAMP]).getSeconds();
				oldRowAsList = Arrays.asList(oldRow);
			}

			if (newRow != null) {
				newRow[INDEX_TIMESTAMP] = ((TimestampData) newRow[INDEX_TIMESTAMP]).getSeconds();
				newRowAsList = Arrays.asList(newRow);
			}

			l.onModification(triggerType, name, table, oldRowAsList, newRowAsList);
		});
	}

}
