package hu.ferencbalogh.actionmonitor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.ferencbalogh.actionmonitor.web.status.ApplicationStatusChecker;

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

	@Value("${app.version}")
	private String version;

	@Autowired
	private ApplicationStatusChecker statusChecker;

	@RequestMapping("/")
	public String index(ModelMap model) {
		model.put("topic", topic);
		model.put("endpoint", endpoint);
		return "index";
	}

	@RequestMapping("/version")
	@ResponseBody
	public String version() {
		if (version != null) {
			return version;
		} else {
			return "Unknown";
		}
	}

	@RequestMapping("/status")
	@ResponseBody
	public String status() {
		return statusChecker.getStatus();
	}
}