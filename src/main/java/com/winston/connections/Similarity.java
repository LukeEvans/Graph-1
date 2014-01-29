package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class Similarity extends Connection {

	public String common_type;
	
	//================================================================================
	// Constructors
	//================================================================================
	public Similarity() {
		connection_type = "similarly_notable";
	}
	
	public Similarity(String t) {
		connection_type = "similarly_notable";
		common_type = t;
	}
	
	public Similarity(JsonNode node) {
		super(node);
		
		common_type = node.path("common_type").asText();
	}
}
