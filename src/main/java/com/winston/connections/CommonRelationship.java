package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class CommonRelationship extends Connection{

	public String common_edge_label;
	public DisplayVertex common_vertex;
	
	//================================================================================
	// Constructors
	//================================================================================
	public CommonRelationship() {
		connection_type = "common_relationship";
	}
	
	public CommonRelationship(DisplayVertex v) {
		connection_type = "common_relationship";
		common_vertex = v;
	}
	
	public CommonRelationship(DisplayVertex v, String e) {
		connection_type = "common_relationship";
		common_vertex = v;
		common_edge_label = e;
	}
	
	public CommonRelationship(JsonNode node) {
		super(node);
		
		common_edge_label = node.path("common_edge_label").asText();
		common_vertex = new DisplayVertex(node.path("common_vertex"));
	}
}
