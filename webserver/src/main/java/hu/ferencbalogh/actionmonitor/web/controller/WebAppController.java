package hu.ferencbalogh.actionmonitor.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebAppController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

//	@RequestMapping("/status")
//	@ResponseBody
//	public String status() {
//		return "OK";
//	}
//
//	@RequestMapping("/version")
//	@ResponseBody
//	public String version() {
//		return "1.0";
//	}
}