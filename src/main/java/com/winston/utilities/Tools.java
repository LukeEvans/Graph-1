package com.winston.utilities;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Tools {

	//================================================================================
	// Clone Jsonnode
	//================================================================================
	@SuppressWarnings("unchecked")
	public static <T extends JsonNode> T copy(T node) {
		  try {
		    return (T) new ObjectMapper().readTree(node.traverse());
		  } catch (IOException e) {
		    throw new AssertionError(e);
		  }
		}
	
	//================================================================================
	// Image Methods
	//================================================================================
	public static Image getImageFromURL(String url) {
		Image image = new ImageIcon(parseUrl(url)).getImage();
		return image;
	}


	//================================================================================
	// URL Fetching Methods
	//================================================================================
	public static JsonNode postJSON(String url, Object object) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.socket.timeout", new Integer(30000));
			HttpPost postRequest = new HttpPost(parseUrl(url).toString());

			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(object);
			StringEntity input = new StringEntity(jsonString);

			input.setContentType("application/json");
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);

			// Return JSON
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			return mapper.readTree(reader);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JsonNode fetchURL(String url) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter("http.socket.timeout", new Integer(10000));
			HttpGet getRequest = new HttpGet(parseUrl(url).toString());
			getRequest.addHeader("accept", "application/json");
			
			HttpResponse response = httpClient.execute(getRequest);
			
			// Return JSON
			ObjectMapper mapper = new ObjectMapper();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			return mapper.readTree(reader);

		} catch (Exception e) {
			System.out.println("Failure: " + url);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String readUrl(String urlString) {
		BufferedReader reader = null;

		try {

			URL encodedURL = parseUrl(urlString);

			if (encodedURL == null) {
				System.out.println("null encoded url");
				return null;
			}

			reader = new BufferedReader(new InputStreamReader(parseUrl(urlString).openStream()));

			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read); 

			if (reader != null)
				reader.close();

			return buffer.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}


		return null;
	}

	//================================================================================
	// URL encoding Methods
	//================================================================================
	public static URL parseUrl(String s) {
		URL u;
		try {
			u = new URL(s);
			try {
				return new URI(
						u.getProtocol(), 
						u.getAuthority(), 
						u.getPath(),
						u.getQuery(), 
						u.getRef()).toURL();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	//================================================================================
	// Random Number generator
	//================================================================================
	// Generate random number
	public static int generateRandomNumber() {

		int Min = 0;
		int Max = 65535;

		int random = Min + (int) (Math.random() * ((Max - Min) + 1));

		random -= 32768;

		return random;
	}
	
	public static int generateRandomNumber(int Min, int Max) {

		int random = Min + (int) (Math.random() * ((Max - Min) + 1));

		return random;
	}


	//================================================================================
	// JSON Mappings
	//================================================================================
	public static ObjectNode nodeFromMap(HashMap<String, Object> map) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.valueToTree(map);
			return node;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ObjectNode addObjectToJson(String field, Object obj, JsonNode json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = (ObjectNode) json;
			JsonNode jsonNode = mapper.valueToTree(obj);
			node.put(field, jsonNode);

			return node;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ObjectNode jsonFromString(String input) {
		if (input != null && input.length() > 0) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				//return objectMapper.readTree(input);
				return objectMapper.readValue(input, ObjectNode.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public static JsonNode mergeBodyAndURL(Map<String,String> params, String input) {
		input = escapeInput(input);

		ObjectNode node = jsonFromString(input);

		if (node == null) {
			ObjectMapper mapper = new ObjectMapper();
			node = mapper.createObjectNode();
		}

		for (String key : params.keySet()) {    		
			node.put(key, params.get(key));
		}

		return node;
	}

	public static String escapeInput(String input) {
		return input.replace("\n", "").replace("\r", "");
	}

	//================================================================================
	// Hash Functions
	//================================================================================
	public static String generateHash(String s) {
		return md5(s.toLowerCase());
	}

	public static String md5(String input) {

		String md5 = null;

		if(null == input) return null;

		try {

			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			//Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return md5;
	}
}
