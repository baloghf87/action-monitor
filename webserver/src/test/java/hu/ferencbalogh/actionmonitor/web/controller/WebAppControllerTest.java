package hu.ferencbalogh.actionmonitor.web.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.ModelMap;

import hu.ferencbalogh.actionmonitor.database.AbstractSpringTest;

@ContextConfiguration(classes = WebAppControllerTest.class)
public class WebAppControllerTest extends AbstractSpringTest {

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Value("${websocket.stomp.endpoint}")
	private String endpoint;

	@Autowired
	private WebAppController webAppController;

	@Bean
	public WebAppController webAppController() {
		return new WebAppController();
	}

	@Test
	public void testIndex() {
		ModelMap mockModelMap = Mockito.mock(ModelMap.class);
		webAppController.index(mockModelMap);
		Mockito.verify(mockModelMap).put("topic", topic);
		Mockito.verify(mockModelMap).put("endpoint", endpoint);
	}

}