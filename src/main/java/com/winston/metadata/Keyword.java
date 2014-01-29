package com.winston.metadata;

import java.util.ArrayList;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public class Keyword {

	public static final int MAX_CANDIDATES = 3;
	public String original_text;
	public boolean connection_found;
	public ArrayList<Candidate> candidates;
	boolean additionalKeyword;
	
	//================================================================================
	// Constructors
	//================================================================================
	public Keyword() {
		// Default
	}
	
	public Keyword(String s) {
		original_text = s;
		candidates = new ArrayList<Candidate>();
	}
	
	public Keyword(String s, boolean additional) {
		original_text = s;
		additionalKeyword = additional;
		candidates = new ArrayList<Candidate>();
	}
	
	public Keyword(JsonNode node) {
		try {
			original_text = node.path("text").asText();
			candidates = new ArrayList<Candidate>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//================================================================================
	// Add Candidate
	//================================================================================
	public void addCandidate(Candidate candidate) {
		if (!candidates.contains(candidate)) {
			candidates.add(candidate);
		}
	}
	
	//================================================================================
	// Candidate size
	//================================================================================
	public int candidateCount() {
		return candidates.size();
	}
	
	//================================================================================
	// Remove Others
	//================================================================================
	public void removeOthers(ArrayList<String> good) {
		ArrayList<Candidate> newCandidates = new ArrayList<Candidate>();
		
		for (Candidate c : candidates) {
			if (good.contains(c.grabMid())) {
				newCandidates.add(c);
				connection_found = true;
			}
		}
		
		if (newCandidates.size() > 0) {
			candidates = newCandidates;
		}
	}
	
	//================================================================================
	// Remove all but the top candidate
	//================================================================================
	public void pruneForTop() {
		ArrayList<Candidate> newCandidates = new ArrayList<Candidate>();
		
		if (candidates != null && candidates.size() > 0) {
			newCandidates.add(candidates.get(0));
			candidates = newCandidates;
		}
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	public String toString() {
		String s = "";

		s = original_text + "\n";
		
		for (Candidate c : candidates) {
			s += c.toString();
		}
		
		return s;
	}
	
	public boolean equals(Object object) {
		try { 
			Keyword other = (Keyword) object;
			if (original_text.equalsIgnoreCase(other.original_text)) {
				return true;
			}
			return false;
			
		} catch (Exception e) {
			return false;
		}
	}
	
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(original_text).
            toHashCode();
    }
}
