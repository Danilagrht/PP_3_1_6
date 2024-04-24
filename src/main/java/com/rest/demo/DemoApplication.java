package com.rest.demo;

import com.rest.demo.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication {
	private static String URL = "http://94.198.50.185:7081/api/users";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = getAllUsers(restTemplate);
		String sessionId = getSessionId(response);

		System.out.println(saveUser(restTemplate, sessionId) + updateUser(restTemplate, sessionId) + deleteUser(restTemplate, sessionId));
	}

	private static ResponseEntity<String> getAllUsers(RestTemplate restTemplate) {
		return restTemplate.getForEntity(URL, String.class);
	}

	private static String getSessionId(ResponseEntity<String> response) {
		HttpHeaders headers = response.getHeaders();
		return headers.getFirst(HttpHeaders.SET_COOKIE);
	}

	private static String saveUser(RestTemplate restTemplate, String sessionId) {
		User newUser = new User(3L, "James", "Brown", (byte) 30);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.COOKIE, sessionId);
		HttpEntity<User> request = new HttpEntity<>(newUser, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
		return response.getBody();
	}

	private static String updateUser(RestTemplate restTemplate, String sessionId) {
		User updateUser = new User(3L, "Thomas", "Shelby", (byte) 35);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.COOKIE, sessionId);
		HttpEntity<User> request = new HttpEntity<>(updateUser, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, request, String.class);
		return response.getBody();
	}

	private static String deleteUser(RestTemplate restTemplate, String sessionId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.COOKIE, sessionId);
		HttpEntity<?> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, request, String.class);
		return response.getBody();
	}
}
