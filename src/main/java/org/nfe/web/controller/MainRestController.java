package org.nfe.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class MainRestController {

	@RequestMapping("greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return "greeting name=" + name;
	}
}
