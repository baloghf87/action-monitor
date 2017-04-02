package hu.ferencbalogh.actionmonitor.database;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.hsqldb.types.TimestampData;
import org.junit.Test;
import org.mockito.Mockito;

import hu.ferencbalogh.actionmonitor.database.monitor.ModificationTrigger;
import hu.ferencbalogh.actionmonitor.database.monitor.ModificationTrigger.ModificationListener;

/**
 * <p>Unit test for {@link ModificationMonitor}</p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
public class ModificationListenerTest {

	@Test
	public void testAddListener() {
		ModificationListener listener = getMockListener();
		assertTrue(ModificationTrigger.addListener(listener));
		assertFalse(ModificationTrigger.addListener(listener));
	}

	@Test
	public void testRemoveListener() {
		ModificationListener listener = getMockListener();
		assertFalse(ModificationTrigger.removeListener(listener));
		assertTrue(ModificationTrigger.addListener(listener));
		assertTrue(ModificationTrigger.removeListener(listener));
	}

	@Test
	public void testFireCallsAllListeners() {
		ModificationListener l1 = getMockListener();
		ModificationListener l2 = getMockListener();
		ModificationTrigger.addListener(l1);
		ModificationTrigger.addListener(l2);

		new ModificationTrigger().fire(1, null, null, null, null);
		Mockito.verify(l1).onModification(1, null, null, null, null);
		Mockito.verify(l2).onModification(1, null, null, null, null);
	}

	@Test
	public void testFireProcessesRows() {
		ModificationListener listener = getMockListener();
		ModificationTrigger.addListener(listener);

		Object[] oldRow = getRow(1, "hello", 1234567l);
		Object[] newRow = getRow(2, "hallo", 9876l);
		new ModificationTrigger().fire(1, "name", "table", oldRow, newRow);

		Mockito.verify(listener).onModification(1, "name", "table", Arrays.asList(oldRow), Arrays.asList(newRow));
	}

	private Object[] getRow(int id, String payload, long timestamp) {
		return new Object[] { id, payload, getMockTimestampData(timestamp) };
	}

	private TimestampData getMockTimestampData(long timestamp) {
		TimestampData timestampData = Mockito.mock(TimestampData.class);
		Mockito.when(timestampData.getSeconds()).thenReturn(timestamp);
		return timestampData;
	}

	private ModificationListener getMockListener() {
		return Mockito.mock(ModificationListener.class);
	}
}
