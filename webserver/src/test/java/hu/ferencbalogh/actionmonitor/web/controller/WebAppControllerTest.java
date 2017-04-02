package hu.ferencbalogh.actionmonitor.web.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.ModelMap;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;
import hu.ferencbalogh.actionmonitor.status.ServiceStatusChecker;
import hu.ferencbalogh.actionmonitor.web.status.ApplicationStatusChecker;

@ContextConfiguration(classes = WebAppControllerTest.class)
public class WebAppControllerTest extends AbstractSpringTest {

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Value("${websocket.stomp.endpoint}")
	private String endpoint;

	@Autowired
	private WebAppController webAppController;

	@Autowired
	private ApplicationStatusChecker applicationStatusChecker;

	@Bean
	public WebAppController webAppController() {
		return new WebAppController();
	}

	@Bean
	public ApplicationStatusChecker applicationStatusChecker() {
		return Mockito.mock(ApplicationStatusChecker.class);
	}

	@Bean
	public ServiceStatusChecker statusChecker() {
		return Mockito.mock(ServiceStatusChecker.class);
	}

	@Test
	public void testIndex() {
		ModelMap mockModelMap = Mockito.mock(ModelMap.class);
		webAppController.index(mockModelMap);
		Mockito.verify(mockModelMap).put("topic", topic);
		Mockito.verify(mockModelMap).put("endpoint", endpoint);
	}

	@Test
	public void testStatus() {
		webAppController.status();
		Mockito.verify(applicationStatusChecker).getStatus();
	}

}