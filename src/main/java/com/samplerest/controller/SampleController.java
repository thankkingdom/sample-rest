package com.samplerest.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SampleController {

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> index(@RequestBody(required = false) Map<String, String> request) {
		Map<String, Object> result = new HashMap<>();
		if (request != null) {
			result.put("reservation_id", request.getOrDefault("reservation_id", null));
			result.put("subject", "こんにちは");
		}
		return ResponseEntity.ok().body(result);
	}

	@RequestMapping(value = "bad", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> bad(@RequestBody(required = false) Map<String, String> request) {
		List<ErrorInfo> errors = new ArrayList<>();
		ErrorInfo error = new ErrorInfo("E001", "エラー");
		errors.add(error);
		Map<String, Object> result = new HashMap<>();
		if (request != null) {
			result.put("reservation_id", request.getOrDefault("reservation_id", "null"));
		}
		result.put("errors", errors);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "info", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> info(@RequestParam(name = "api_token") String apiToken,
			@RequestParam(name = "reservation_id") String reservation_id) {
		Map<String, Object> result = new HashMap<>();
		List<ErrorInfo> errors = new ArrayList<>();
		if (apiToken == null) {
			errors.add(new ErrorInfo("E002", "トークンが不正です"));
		}
		if (StringUtils.isEmpty(reservation_id)) {
			errors.add(new ErrorInfo("E003", "予約IDを指定して下さい"));
		}
		if (errors.size() > 0) {
			result.put("errors", errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		result.put("reservation_id", reservation_id);
		result.put("scheduled_date", LocalDateTime.now().plusDays(1).withMinute(0).withNano(0));
		result.put("subject", "こんにちは");
		result.put("text_message", "ありがとう。");
		return ResponseEntity.ok().body(result);
	}

	class ErrorInfo {
		private String code;
		private String message;

		public ErrorInfo() {
		}

		public ErrorInfo(String code, String message) {
			super();
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
