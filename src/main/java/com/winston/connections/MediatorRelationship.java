package com.winston.connections;

import com.fasterxml.jackson.databind.JsonNode;

public class MediatorRelationship extends Relationship {
	
	public Mediator mediator;
	
	//================================================================================
	// Constructors
	//================================================================================
	public MediatorRelationship() {
		connection_type = "mediator_relationship";
	}
	
	public MediatorRelationship(String e) {
		edge_label = e;
		connection_type = "mediator_relationship";
	}
	
	public MediatorRelationship(String e, Mediator m) {
		edge_label = e;
		mediator = m;
		connection_type = "mediator_relationship";
	}
	
	public MediatorRelationship(JsonNode node) {
		super(node);
		mediator = new Mediator(node.path("mediator"));
	}
}
