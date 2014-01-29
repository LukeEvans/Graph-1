package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class Connection {

	public String connection_type;
	
	//================================================================================
	// Base Class
	//================================================================================
	public Connection() {
		connection_type = "connection";
	}
	
	public Connection(JsonNode node) {
		connection_type = node.path("connection_type").asText();
	}
	
}
