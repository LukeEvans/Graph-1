package com.winston.connections;

import java.util.ArrayList;

import com.winston.metadata.Candidate;
import com.winston.metadata.Keyword;

public class ConnectionMatrix {

	public ArrayList<ConnectionSet> connection_sets;
	
	//================================================================================
	// Constructors
	//================================================================================
	public ConnectionMatrix() {
		connection_sets = new ArrayList<ConnectionSet>();
	}

	public ConnectionMatrix(ArrayList<Keyword> keywords) {
		connection_sets = new ArrayList<ConnectionSet>();
		init(keywords);
	}
	
	//================================================================================
	// Init
	//================================================================================
	public void init(ArrayList<Keyword> keywords) {
		
		for (int i=0; i<keywords.size() - 1; i++) {
			Keyword k1 = keywords.get(i);

			for (int j=i+1; j<keywords.size(); j++) {
				Keyword k2 = keywords.get(j);
				
				for (Candidate c1 : k1.candidates) {
					
					for (Candidate c2 : k2.candidates) {
						ConnectionSet set = new ConnectionSet(c1.grabMid(), c2.grabMid());
						connection_sets.add(set);
					}
				}
			}
			
		}
	}
	
	//================================================================================
	// Get all sets
	//================================================================================
	public ArrayList<ConnectionSet> grabSets() {
		return connection_sets;
	}
	
	//================================================================================
	// Prune all non-connected things
	//================================================================================
	public void prune() {
		
		ArrayList<ConnectionSet> newSet = new ArrayList<ConnectionSet>();
		
		for (ConnectionSet set : connection_sets) {
			if (set.connected()) {
				newSet.add(set);
			}
		}
		
		connection_sets = newSet;
	}
	
	//================================================================================
	// Get list of candidate ids that are allowed to stay
	//================================================================================
	public ArrayList<String> findConnectedIds() {
		
		// First, prune the list
		prune();
		
		ArrayList<String> ids = new ArrayList<String>();
		
		for (ConnectionSet set : connection_sets) {
			String s = set.source_id;
			String t = set.target_id;
			
			if (!ids.contains(s)) {
				ids.add(s);
			}
			
			if (!ids.contains(t)) {
				ids.add(t);
			}
		}
		
		return ids;
	}
	
	//================================================================================
	// Get list of ids that are common and therefore possibly related
	//================================================================================
	public ArrayList<String> findRelatedIds() {
		ArrayList<String> relatedIds = new ArrayList<String>();
		
		for (ConnectionSet set : connection_sets) {
			for (Connection connection : set.connections) {
				if (connection instanceof CommonRelationship) {
					CommonRelationship relationship = (CommonRelationship) connection;
					
					String id = relationship.common_vertex.id;
					
					if (!relatedIds.contains(id)) {
						relatedIds.add(id);
					}
				}
			}
		}
		
		return relatedIds;
	}
}
