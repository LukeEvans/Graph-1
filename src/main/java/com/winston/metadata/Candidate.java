package com.winston.metadata;

import java.util.ArrayList;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.JsonNode;

public class Candidate {

	public String name;
	public String id;
	public String notable_for;
	public String notable_type;
	public ArrayList<String> types;
	String mid;
	public String wikipedia_title;
	public String wikipedia_description;
	public String type;
	public ArrayList<String> images;
	public String icon;

	//================================================================================
	// Constructors
	//================================================================================
	public Candidate() {
		// Default
	}

	public Candidate(JsonNode node) {
		try {
			name = node.path("name").asText();
			id = node.path("mid").asText();

			types = new ArrayList<String>();

			// Translate id to the graph version of the id
			translateMid();
			
			// Set static fields
			type = "Wikipedia";
			icon = "https://s3.amazonaws.com/Channel_Icons/Wikipedia-logo-v2.png";
			images = new ArrayList<String>();
			images.add("https://usercontent.googleapis.com/freebase/v1/image" + id +  "?maxwidth=960");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//================================================================================
	// Translate mid to Titan mid
	//================================================================================
	public void translateMid() {
		try {

			if (id.startsWith("/m/")) {
				mid = id.replaceAll("/m/", "ns:m.");
			}

			else if (id.startsWith("/g/")) {
				mid = id.replaceAll("/g/", "ns:g.");
			}

		} catch (Exception e) {
			// Ignore
		}
	}

	//================================================================================
	// Get translated id
	//================================================================================
	public String grabMid() {
		return mid;
	}

	//================================================================================
	// Grab metadata from Vertex
	//================================================================================
	public void grabVertexMetaData(JsonNode details) {

		try {
			wikipedia_description = details.path("wikipedia_description").asText();
			wikipedia_title = details.path("wikipedia_title").asText();
			
			try {
				notable_for = details.path("notable_for").asText().replaceAll("@en", "");
				notable_type = details.path("notable_type").asText().replaceAll("@en", "");
			} catch (Exception e) {
				// Ignore
			}
			
			if (types == null) {
				types = new ArrayList<String>();
			}
			
			for (JsonNode n : details.path("types")) {
				types.add(n.asText());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore
		}
	}

	//================================================================================
	// House Keeping
	//================================================================================
	public String toString() {
		return name + " -- " + mid + "\n";
	}

	public boolean equals(Object object) {
		try { 
			Candidate other = (Candidate) object;
			if (mid.equalsIgnoreCase(other.mid) && (name.equalsIgnoreCase(other.name))) {
				return true;
			}
			return false;

		} catch (Exception e) {
			return false;
		}
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				append(mid).
				append(name).
				toHashCode();
	}
}
