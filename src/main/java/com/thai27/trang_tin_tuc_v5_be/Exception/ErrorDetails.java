package com.thai27.trang_tin_tuc_v5_be.Exception;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ErrorDetails {
	private Instant timestamp;
	private int status;
	private String message;
	private String path;
	private List<String> errors;
	
	public ErrorDetails(Instant timestamp, int status, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
	}

	public ErrorDetails(Instant timestamp, int status, String message, String path, List<String> errors) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
		this.errors = errors;
	}

}
