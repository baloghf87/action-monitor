package hu.ferencbalogh.actionmonitor.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hu.ferencbalogh.actionmonitor.entity.Action;
import hu.ferencbalogh.actionmonitor.entity.Record;
	
public class IntegrationFlowConfigurationTest {

	private IntegrationFlowConfiguration integrationFlowConfiguration = new IntegrationFlowConfiguration();

	@Test
	public void testActionToStringTransformer() {
		Record oldRecord = new Record(1, "old", 1l);
		Record newRecord = new Record(2, "new", 2l);
		Action action = new Action("table", "action", oldRecord, newRecord);
		String result = integrationFlowConfiguration.actionToStringTransformer(action);
		assertEquals("Table: table, Action: action:, Old: " + oldRecord + ", New: " + newRecord, result);
	}
}