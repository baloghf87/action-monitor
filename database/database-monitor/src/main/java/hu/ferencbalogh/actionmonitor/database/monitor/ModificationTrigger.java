package hu.ferencbalogh.actionmonitor.database.monitor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hsqldb.Trigger;
import org.hsqldb.types.TimestampData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Trigger to be called by HSQLDB on modifying a table</p>
 *
 * <p>On action it calls the registered listeners.</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class ModificationTrigger implements Trigger {

	private static final Logger log = LoggerFactory.getLogger(ModificationTrigger.class);

	private static final int INDEX_TIMESTAMP = 2;

	private static Set<ModificationListener> modificationListeners = new LinkedHashSet<ModificationListener>();

	public static boolean addListener(ModificationListener listener) {
		log.debug("Adding listener: {}", listener);
		return modificationListeners.add(listener);
	}

	public static boolean removeListener(ModificationListener listener) {
		log.debug("Removing listener: {}", listener);
		return modificationListeners.remove(listener);
	}

	/**
	 * <p>Trigger to be called by HSQLDB on modification of a table</p>
	 *
	 * <p>On action it calls the registered listeners.</p>
	 * 
	 * @author Ferenc Balogh - baloghf87@gmail.com
	 *
	 */
	public static interface ModificationListener {
		void onModification(int triggerType, String name, String table, List<Object> oldRow, List<Object> newRow);
	}

	public void fire(int triggerType, String name, String table, Object oldRow[], Object newRow[]) {
		log.debug("Trigger - Action: {}, table: {}, old row: {}, new row: {}", name, table, oldRow, newRow);
		modificationListeners.forEach(listener -> {
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

			log.debug("Calling listener: {}", listener);
			listener.onModification(triggerType, name, table, oldRowAsList, newRowAsList);
		});
	}

}
