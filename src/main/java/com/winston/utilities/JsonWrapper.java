package com.winston.utilities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.elasticsearch.common.xcontent.XContentBuilder;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class JsonWrapper {

	public LinkedHashMap<String, Object> data;
	

	//================================================================================
	// Constructors
	//================================================================================
	public JsonWrapper() {
		// Default for Jackson
	}

	@SuppressWarnings("unchecked")
	public JsonWrapper(JsonNode node) {

		//data = new LinkedHashMap<String, Object>();

		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(node);
			data = mapper.readValue(json, LinkedHashMap.class);

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	//================================================================================
	// Remove data
	//================================================================================
	public void remove(String field) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(data);
			ObjectNode node = (ObjectNode) mapper.readTree(json);
			
			node.remove(field);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//================================================================================
	// Get map
	//================================================================================
	public Map<String, Object> getmMap() {
		return data;
	}

	//================================================================================
	// Get json item
	//================================================================================
	public XContentBuilder getJsonContent() {
		try {
			XContentBuilder json = jsonBuilder().startObject();
			for (Entry<String, Object> e : data.entrySet()) {
				json.field(e.getKey().toString(), e.getValue());
			}

			json.endObject();
			
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void remove(String... path){
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(data);
			ObjectNode node = (ObjectNode) mapper.readTree(json);
			ObjectNode tempNode = node;
			int i;
			for(i = 0; i < (path.length - 1); i++){
				tempNode = (ObjectNode) tempNode.path(path[i]);
			}
			tempNode.remove(path[i++]);
		
			json = mapper.writeValueAsString(node);
			data = mapper.readValue(json, LinkedHashMap.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public JsonNode getJsonNode() {
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(data);
			JsonNode node = mapper.readTree(json);
			return node;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
