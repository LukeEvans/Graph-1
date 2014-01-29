package com.winston.metadata;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class Confluence {

	// Topic IDs
	ArrayList<String> topicNewsIds;
	ArrayList<String> topicTwitterIds;
	ArrayList<String> topicFacebookIds;

	// Topic stories
	public ArrayList<Map<String, Object>> topic_news;
	public ArrayList<Map<String, Object>> topic_twitter;
	public ArrayList<Map<String, Object>> topic_facebook;
	
	// Related IDs
	ArrayList<String> relatedNewsIds;
	ArrayList<String> relatedTwitterIds;
	ArrayList<String> relatedFacebookIds;
	
	// Related stories
	public ArrayList<Map<String, Object>> related_news;
	public ArrayList<Map<String, Object>> related_twitter;
	public ArrayList<Map<String, Object>> related_facebook;

	//================================================================================
	// Constructors
	//================================================================================
	public Confluence() {
		topicNewsIds = new ArrayList<String>();
		topicTwitterIds = new ArrayList<String>();
		topicFacebookIds = new ArrayList<String>();

		topic_news = new ArrayList<Map<String, Object>>();
		topic_twitter = new ArrayList<Map<String, Object>>();
		topic_facebook = new ArrayList<Map<String, Object>>();
		
		relatedNewsIds = new ArrayList<String>();
		relatedTwitterIds = new ArrayList<String>();
		relatedFacebookIds = new ArrayList<String>();
		
		related_news = new ArrayList<Map<String, Object>>();
		related_twitter = new ArrayList<Map<String, Object>>();
		related_facebook = new ArrayList<Map<String, Object>>();
	}

	//================================================================================
	// Constructor
	//================================================================================
	public Confluence(JsonNode node) {
		topicNewsIds = buildList("topicNewsIds", node);
		topicTwitterIds = buildList("topicTwitterIds", node);
		topicFacebookIds = buildList("topicFacebookIds", node);
		
		relatedNewsIds = buildList("relatedNewsIds", node);
		relatedTwitterIds = buildList("relatedTwitterIds", node);
		relatedFacebookIds = buildList("relatedFacebookIds", node);
		
		topic_news = new ArrayList<Map<String, Object>>();
		topic_twitter = new ArrayList<Map<String, Object>>();
		topic_facebook = new ArrayList<Map<String, Object>>();
		
		related_news = new ArrayList<Map<String, Object>>();
		related_twitter = new ArrayList<Map<String, Object>>();
		related_facebook = new ArrayList<Map<String, Object>>();
	}
	
	//================================================================================
	// Build List from jsonnode
	//================================================================================
	public ArrayList<String> buildList(String key, JsonNode node) {
		ArrayList<String> list = new ArrayList<String>();
		
		try {
			for (JsonNode n : node.path(key)) {
				if (n.isTextual()) {
					list.add(n.asText());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		
		return list;
	}
	
	//================================================================================
	// Get List of news IDs
	//================================================================================
	public ArrayList<String> grabNewsIDs() {
		ArrayList<String> ids = new ArrayList<String>();
		
		for (String s : topicNewsIds) {
			if (!ids.contains(s)) {
				ids.add(s);
			}
		}
		
		return ids;
	}
	
	//================================================================================
	// Get List of twitter IDs
	//================================================================================
	public ArrayList<String> grabTwitterIDs() {
		ArrayList<String> ids = new ArrayList<String>();
		
		for (String s : topicTwitterIds) {
			if (!ids.contains(s)) {
				ids.add(s);
			}
		}
		
		return ids;
	}
	
	//================================================================================
	// Add Node to proper list
	//================================================================================
	public void addNode(String id, Map<String, Object> node) {
		
		if (node == null) {
			return;
		}
		
		// Clean map
		cleanMap(node);
		
		if (topicNewsIds.contains(id)) {
			topic_news.add(node);
			return;
		}
		
		if (topicTwitterIds.contains(id)) {
			topic_twitter.add(node);
			return;
		}
		
		if (topicFacebookIds.contains(id)) {
			topic_facebook.add(node);
			return;
		}
		
		
		if (relatedNewsIds.contains(id)) {
			related_news.add(node);
			return;
		}
		
		if (relatedTwitterIds.contains(id)) {
			related_twitter.add(node);
			return;
		}
		
		if (relatedFacebookIds.contains(id)) {
			related_facebook.add(node);
			return;
		}
	}
	
	//================================================================================
	// Prune story
	//================================================================================
	public void cleanMap(Map<String,Object> map) {
		try {
			map.remove("nlp");
			map.remove("speech");
		} catch (Exception e) {
			// Ignore
		}
	}
}