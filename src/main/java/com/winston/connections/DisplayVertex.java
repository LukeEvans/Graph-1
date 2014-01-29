package com.winston.connections;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class DisplayVertex {
	
	public String name;
	public String id;
	public ArrayList<String> types;
	
	//================================================================================
	// Constructors
	//================================================================================
	public DisplayVertex() {
		types = new ArrayList<String>();
	}
	
	public DisplayVertex(String mid) {
		id = mid;
		types = new ArrayList<String>();
	}
	
	public DisplayVertex(String mid, String n) {
		name = n;
		id = mid;
		types = new ArrayList<String>();
	}
	
	public DisplayVertex(String mid, String n, ArrayList<String> t) {
		name = n;
		id = mid;
		types = t;
		
		if (name == null) {
			name = "";
		}
	}
	
	public DisplayVertex(JsonNode node) {
		name = node.path("name").asText();
		id = node.path("id").asText();
		types = new ArrayList<String>();
		
		for (JsonNode n : node.path("types")) {
			types.add(n.asText());
		}
	}
}
