package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class Relationship extends Connection {

	public String edge_label;
	
	//================================================================================
	// Constructors
	//================================================================================
	public Relationship() {
		connection_type = "direct_relationship";
	}
	
	public Relationship(String e) {
		edge_label = e;
		connection_type = "direct_relationship";
	}
	
	public Relationship(JsonNode node) {
		super(node);
		
		edge_label = node.path("edge_label").asText();
	}
}
