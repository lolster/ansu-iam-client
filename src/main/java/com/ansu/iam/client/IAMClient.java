package com.ansu.iam.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ansu.iam.client.exception.IAMClientErrorException;
import com.ansu.iam.client.exception.IAMInputException;
import com.ansu.iam.client.exception.IAMServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class IAMClient {
	private static final String PROP_FILE_NAME = "config.properties";

	private static final String IAM_SERVER_IP_KEY = "com.ansu.client.server_ip";
	private static String IAM_SERVER_IP;

	private static final String IAM_SERVER_PORT_KEY = "com.ansu.client.server_port";
	private static String IAM_SERVER_PORT;
	
	private static final String IAM_ENDPOINT_KEY = "com.ansu.client.iam_endpoint";
	private static String IAM_ENDPOINT;
	
	private static String IAM_SERVER_URL;

	static {
		// Load the config values from properties file
		InputStream is = IAMClient.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
		Properties prop = new Properties();

		try {
			prop.load(is);
		} catch (IOException e) {
			System.err.println("Something went wrong trying to get the config file:");
			e.printStackTrace();
		}

		IAM_SERVER_IP = prop.getProperty(IAM_SERVER_IP_KEY);
		IAM_SERVER_PORT = prop.getProperty(IAM_SERVER_PORT_KEY);
		IAM_ENDPOINT = prop.getProperty(IAM_ENDPOINT_KEY);
		
		IAM_SERVER_URL = "http://" + IAM_SERVER_IP + ":" + IAM_SERVER_PORT + IAM_ENDPOINT;

		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = 
					new com.fasterxml.jackson.databind.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	public IAMResponse createApplication(String appName) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appName == null || appName.length() == 0) {
			throw new IAMInputException("Invalid appName param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.post(IAM_SERVER_URL + "/app/create")
					.field("appName", appName)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse getAppId(String appName) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appName == null || appName.length() == 0) {
			throw new IAMInputException("Invalid appName param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.get(IAM_SERVER_URL + "/app")
					.queryString("appName", appName)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse updateApplicationTime(String appId, int seconds) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(seconds == 0) {
			throw new IAMInputException("Invalid seconds param (cannot be 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.put(IAM_SERVER_URL + "/app/update")
					.field("appId", appId)
					.field("seconds", seconds)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse registerUser(String appId, String name, String password) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(name == null || name.length() == 0) {
			throw new IAMInputException("Invalid name param (null or length 0)");
		}
		else if(password == null || password.length() == 0) {
			throw new IAMInputException("Invalid password param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.post(IAM_SERVER_URL + "/user/create")
					.field("appId", appId)
					.field("name", name)
					.field("pass", password)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse deleteUser(String appId, String uid) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(uid == null || uid.length() == 0) {
			throw new IAMInputException("Invalid uid param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.delete(IAM_SERVER_URL + "/user/delete")
					.field("appId", appId)
					.field("uid", uid)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse generateToken(String appId, String uid, String password) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(uid == null || uid.length() == 0) {
			throw new IAMInputException("Invalid uid param (null or length 0)");
		}
		else if(password == null || password.length() == 0) {
			throw new IAMInputException("Invalid password param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.post(IAM_SERVER_URL + "/token/generate")
					.field("appId", appId)
					.field("uid", uid)
					.field("pass", password)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse validateToken(String appId, String tokenString) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(tokenString == null || tokenString.length() == 0) {
			throw new IAMInputException("Invalid token param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.get(IAM_SERVER_URL + "/token/checkValid")
					.queryString("appId", appId)
					.queryString("token", tokenString)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
	
	public IAMResponse deleteToken(String appId, String tokenString) 
			throws IAMInputException, IAMServerErrorException, IAMClientErrorException {
		if(appId == null || appId.length() == 0) {
			throw new IAMInputException("Invalid appId param (null or length 0)");
		}
		else if(tokenString == null || tokenString.length() == 0) {
			throw new IAMInputException("Invalid token param (null or length 0)");
		}
		
		HttpResponse<IAMResponse> response = null;
		IAMResponse res = null;
		
		try {
			response = Unirest.delete(IAM_SERVER_URL + "/token/delete")
					.field("appId", appId)
					.field("token", tokenString)
					.asObject(IAMResponse.class);
		} catch (UnirestException e) {
			throw new IAMClientErrorException("Client error, please try later " 
					+ "(check your network connectivity)");
		}
		
		res = response.getBody();
		
		if(res.getStatus().equalsIgnoreCase("Error")) {
			throw new IAMServerErrorException("Server error: " + res.getPayload());
		}
		
		return res;
	}
}
