package hu.ferencbalogh.actionmonitor.database;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.hsqldb.types.TimestampData;
import org.junit.Test;
import org.mockito.Mockito;

import hu.ferencbalogh.actionmonitor.database.monitor.ModificationMonitor;
import hu.ferencbalogh.actionmonitor.database.monitor.ModificationMonitor.ModificationListener;

public class ModificationMonitorTest {

	@Test
	public void testAddListener() {
		ModificationListener listener = getMockListener();
		assertTrue(ModificationMonitor.addListener(listener));
		assertFalse(ModificationMonitor.addListener(listener));
	}

	@Test
	public void testRemoveListener() {
		ModificationListener listener = getMockListener();
		assertFalse(ModificationMonitor.removeListener(listener));
		assertTrue(ModificationMonitor.addListener(listener));
		assertTrue(ModificationMonitor.removeListener(listener));
	}

	@Test
	public void testFireCallsAllListeners() {
		ModificationListener l1 = getMockListener();
		ModificationListener l2 = getMockListener();
		ModificationMonitor.addListener(l1);
		ModificationMonitor.addListener(l2);

		new ModificationMonitor().fire(1, null, null, null, null);
		Mockito.verify(l1).onModification(1, null, null, null, null);
		Mockito.verify(l2).onModification(1, null, null, null, null);
	}

	@Test
	public void testFireProcessesRows() {
		ModificationListener listener = getMockListener();
		ModificationMonitor.addListener(listener);

		Object[] oldRow = getRow(1, "hello", 1234567l);
		Object[] newRow = getRow(2, "hallo", 9876l);
		new ModificationMonitor().fire(1, "name", "table", oldRow, newRow);

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
