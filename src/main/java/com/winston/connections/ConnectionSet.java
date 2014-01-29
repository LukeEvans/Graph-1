package com.winston.connections;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class ConnectionSet {
	public String source_id;
	public String target_id;
	public ArrayList<Connection> connections;
	
	//================================================================================
	// Constructors
	//================================================================================
	public ConnectionSet() {
		// Default
		connections = new ArrayList<Connection>();
	}
	
	public ConnectionSet(String s, String t) {
		source_id = s;
		target_id = t;
		connections = new ArrayList<Connection>();
	}
	
	public ConnectionSet(JsonNode node) {
		source_id = node.path("source_id").asText();
		target_id = node.path("target_id").asText();
		connections = new ArrayList<Connection>();
		
		// Build the connections one by one
		for (JsonNode n : node.path("connections")) {
			if (n.path("connection_type").asText().equals("mediator_relationship")) {
				connections.add(new MediatorRelationship(n));
			}
			
			if (n.path("connection_type").asText().equals("direct_relationship")) {
				connections.add(new Relationship(n));
			}
			
			if (n.path("connection_type").asText().equals("similarly_notable")) {
				connections.add(new Similarity(n));
			}
			
			if (n.path("connection_type").asText().equals("common_relationship")) {
				connections.add(new CommonRelationship(n));
			}
			
		}
	}
	//================================================================================
	// Add Connection
	//================================================================================
	public void addConnection(Connection c) {
		connections.add(c);
	}
	
	//================================================================================
	// Determine if we have any connections
	//================================================================================
	public boolean connected() {
		return connections.size() != 0;
	}

	//================================================================================
	// Determine if we have a common type
	//================================================================================
	public boolean commonTypeExists() {
		for (Connection c : connections) {
			if (c instanceof Similarity) {
				return true;
			}
		}
		
		return false;
	}
}
