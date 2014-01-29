package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class DisplayEdge {

	public String edge;
	public String vertex_id;
	public String vertex_name;
	
	//================================================================================
	// Constructor
	//================================================================================
	public DisplayEdge() {
		// Default
	}
	
	public DisplayEdge(String e, String i, String n) {
		edge = e;
		vertex_id = i;
		vertex_name = n;
	}
	
	public DisplayEdge(JsonNode node) {
		edge = node.path("edge").asText();
		vertex_id = node.path("vertex_id").asText();
		vertex_name = node.path("vertex_name").asText();
	}
}
