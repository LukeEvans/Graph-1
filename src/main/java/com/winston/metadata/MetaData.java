package com.winston.metadata;

import java.util.ArrayList;

import com.winston.connections.ConnectionMatrix;

public class MetaData {

	public String free_text;
	public ArrayList<Keyword> keywords;
	public ConnectionMatrix connection_matrix;
	public Confluence confluence;
	
	//================================================================================
	// Constructors
	//================================================================================
	public MetaData() {
		// Default
	}

	public MetaData(String fullText) {
		free_text = fullText;
		keywords = new ArrayList<Keyword>();
		connection_matrix = new ConnectionMatrix();
		confluence = new Confluence();
	}

	//================================================================================
	// Add Keyword
	//================================================================================
	public void addKeyword(Keyword keyword) {
		if (!keywords.contains(keyword)) {
			keywords.add(keyword);
		}
	}

	//================================================================================
	// Init Matrix
	//================================================================================
	public void initConnectionMatrix() {

		// Init matrix
		connection_matrix.init(keywords);
	}

	//================================================================================
	// Prune MetaData
	//================================================================================
	public void prune() {
		ArrayList<String> connectedIds = connection_matrix.findConnectedIds();

		// Remove Candidates that have been proven wrong
		for (Keyword keyword : keywords) {
			keyword.removeOthers(connectedIds);
		}

		// If we have any connections, remove all non-connected ones
		if (connectionsExist()) {
			ArrayList<Keyword> newKeywords = new ArrayList<Keyword>();

			for (Keyword k : keywords) {
				if (k.connection_found) {
					newKeywords.add(k);
				}
			}

			// Set new keywords
			keywords = newKeywords;
		}

		// Remove all keywords that were added additionally
		else {
			ArrayList<Keyword> newKeywords = new ArrayList<Keyword>();
			
			for (Keyword keyword : keywords) {
				if (!keyword.additionalKeyword) {
					newKeywords.add(keyword);
				}
				
				else if (keyword.additionalKeyword && keyword.connection_found) {
					newKeywords.add(keyword);
				}
			}
		}
	}

	//================================================================================
	// Prune for top candidates
	//================================================================================
	public void pruneForTopCandidates() {
		for (Keyword keyword : keywords) {
			keyword.pruneForTop();
		}
	}
	
	//================================================================================
	// Determine if > 0 connections have been found
	//================================================================================
	public boolean connectionsExist() {
		for (Keyword k : keywords) {
			if (k.connection_found) {
				return true;
			}
		}

		return false;
	}
	
	//================================================================================
	// Get list of topic mids
	//================================================================================
	public ArrayList<String> topicMIDList() {
		ArrayList<String> topicIds = new ArrayList<String>();
		
		for (Keyword keyword : keywords) {
			for (Candidate candidate : keyword.candidates) {
				if (!topicIds.contains(candidate.grabMid())) {
					topicIds.add(candidate.grabMid());
				}
			}
		}
		
		return topicIds;
	}
	
	//================================================================================
	// Get list of topic ids
	//================================================================================
	public ArrayList<String> topicIDList() {
		ArrayList<String> topicIds = new ArrayList<String>();
		
		for (Keyword keyword : keywords) {
			for (Candidate candidate : keyword.candidates) {
				if (!topicIds.contains(candidate.id)) {
					topicIds.add(candidate.id);
				}
			}
		}
		
		return topicIds;
	}
	
	//================================================================================
	// Get related ids
	//================================================================================
	public ArrayList<String> relatedIDList() {
		return connection_matrix.findRelatedIds();
	}
}
