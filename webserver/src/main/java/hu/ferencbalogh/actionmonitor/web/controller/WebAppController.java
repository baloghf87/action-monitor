package hu.ferencbalogh.actionmonitor.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * Web application controller
 * </p>
 * 
 * @author Ferenc Balogh - baloghf87@gmail.com
 *
 */
@Controller
public class WebAppController {

	@Value("${websocket.stomp.topic}")
	private String topic;

	@Value("${websocket.stomp.endpoint}")
	private String endpoint;

	@RequestMapping("/")
	public String index(ModelMap model) {
		model.put("topic", topic);
		model.put("endpoint", endpoint);
		return "index";
	}

	// TODO get from pom
	// @RequestMapping("/version")
	// @ResponseBody
	// public String version() {
	// return "1.0";
	// }
}