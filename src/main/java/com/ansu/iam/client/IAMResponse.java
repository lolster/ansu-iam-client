package com.ansu.iam.client;

public class IAMResponse {
	private String status;
	private String payload;
	
	protected IAMResponse() {
	}
	
	protected IAMResponse(String status, String payload) {
		this.status = status;
		this.payload = payload;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status.trim();
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload.trim();
	}
}
