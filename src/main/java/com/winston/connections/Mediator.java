package com.winston.connections;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class Mediator extends DisplayVertex {

	public ArrayList<DisplayEdge> edges;
	
	//================================================================================
	// Constructors
	//================================================================================
	public Mediator() {
		super();
	}
	
	public Mediator(String mid) {
		super(mid);
		edges = new ArrayList<DisplayEdge>();
	}
	
	public Mediator(String mid, String n, ArrayList<String> t, ArrayList<DisplayEdge> e) {
		super(mid, n, t);
		edges = e;
		buildName();
	}
	
	public Mediator(JsonNode node) {
		super(node);
		
		edges = new ArrayList<DisplayEdge>();
		
		for (JsonNode n : node.path("edges")) {
			edges.add(new DisplayEdge(n));
		}
	}
	
	//================================================================================
	// Build Name
	//================================================================================
	public void buildName() {
		if (edges == null) {
			return;
		}
		
		for (DisplayEdge e : edges) {
			if (e != null && e.vertex_name != null) {
				name += e.vertex_name + " -- ";
			}
		}
		
		name = name.substring(0, name.length()-4);
	}
}
