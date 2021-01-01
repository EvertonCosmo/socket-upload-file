package com.redes.atividade.socketserver.status;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/ping")
	@ResponseBody
	public ResponseEntity<?> ping() {
		logger.info("pinged");
		return ResponseEntity.ok().body(String.format("{pinged: %s", Calendar.getInstance().toString()));
	}
}
