package hu.ferencbalogh.actionmonitor.web.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import hu.ferencbalogh.actionmonitor.status.AbstractServiceStatusCheckerTest;
import hu.ferencbalogh.actionmonitor.web.config.WebSocketConfig;

@SpringBootTest(classes = { WebsocketEndpointStatusCheckerTest.class, WebSocketConfig.class,
		WebsocketEndpointStatusChecker.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class WebsocketEndpointStatusCheckerTest extends AbstractServiceStatusCheckerTest {

	@Autowired
	private WebsocketEndpointStatusChecker websocketEndpointStatusChecker;

	@Override
	public void testReportFalseWhenNotRunning() throws Exception {
		assertFalse(new WebsocketEndpointStatusChecker("ws://wrongURL:1234", "").isRunning());
	}

	@Override
	public void testReportTrueWhenRunning() throws Exception {
		assertTrue(websocketEndpointStatusChecker.isRunning());
	}

	@Override
	public void testGetName() {
		assertEquals("WebSocket endpoint", websocketEndpointStatusChecker.getName());
	}
}
