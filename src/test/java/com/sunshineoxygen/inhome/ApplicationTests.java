package com.sunshineoxygen.inhome;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.Scanner;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() throws IOException {

		/* URL url = new URL("http://localhost:8443/oauth/token");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpConn.setRequestProperty("Authorization",getBasicAuthenticationHeader("server-server", "server-server"));
		httpConn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
		writer.write("grant_type=client_credentials");
		writer.flush();
		writer.close();
		httpConn.getOutputStream().close();
		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : ""; */

	}

	@Test
	void useUnirest() throws UnirestException {
		/* HttpResponse<String> response = Unirest.post("http://localhost:8443/oauth/token")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Authorization", getBasicAuthenticationHeader("server-server", "server-server"))
				//.basicAuth(env.getProperty("app.oauth2.client-id"),env.getProperty("app.oauth2.client-secret"))
				.body("grant_type=client_credentials").asString();
		System.out.println(response.getBody()); */
	}

	private static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

}
