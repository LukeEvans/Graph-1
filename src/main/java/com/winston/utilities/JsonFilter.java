package com.winston.utilities;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class JsonFilter {

	public ArrayList<JsonNode> results;
	public ArrayList<String> strings;
	public boolean found;

	//================================================================================
	// Constructor
	//================================================================================
	public JsonFilter() {
		results = new ArrayList<JsonNode>();
	}

	//================================================================================
	// Rest
	//================================================================================
	public void reset() {
		results = new ArrayList<JsonNode>();
	}

	//================================================================================
	// Find value
	//================================================================================
	public void process(String search, JsonNode node) {
		Iterator<String> fieldNames = node.getFieldNames();
		while(fieldNames.hasNext()){
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);

			if (!fieldValid(fieldName)) {
				return;
			}

			if (fieldValue.isObject()) {
				process(search, fieldValue);
			}

			if (fieldValue.isArray()) {
				for (JsonNode element : fieldValue) {
					process(search, element);
				}
			}

			else {
				try {
					String value = fieldValue.getTextValue();
					if (value.equalsIgnoreCase(search)) {
						found = true;
					}
				} catch (Exception e) {
					// Ignore
				}
			}
		}
	}

	//================================================================================
	// Determine if field is valid
	//================================================================================
	public boolean fieldValid(String field) {
		if (field.equalsIgnoreCase("/type/object/attribution")) {
			return false;
		}

		if (field.equalsIgnoreCase("/type/object/type")) {
			return false;
		}

		return true;
	}
	//================================================================================
	// Process properties
	//================================================================================
	public void processProperties(String search, JsonNode node) {

		// Drill deeper if possible
		if (node.path("result") != null && !node.path("result").isMissingNode()) {
			node = node.path("result");
		}

		if (node.path("property") != null && !node.path("property").isMissingNode()) {
			node = node.path("property");
		}
		
		// Full properties
		try {

			for (JsonNode n : node.findValues("property")) {

				found = false;
				process(search, n);

				if (found) {
					results.add(n);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		// Look for top level objects
		Iterator<String> fieldNames = node.getFieldNames();
		while(fieldNames.hasNext()){
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);

			try {

				// Force an exception if this fails
				String verify = fieldValue.path("values").path(0).path("text").getTextValue();
				if (verify == null) {
					continue;
				}
				
				found = false;
				process(search, fieldValue);
				
				if (found) {
					results.add(fieldValue);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	//================================================================================
	// Single hops
	//================================================================================
	public void processProperties(JsonNode node1, JsonNode node2) {
		try {

			for (JsonNode n : node1.path("property").findValues("property")) {

				for (JsonNode json : n.findValues("id")) {

					try {
						String string = json.getTextValue();

						reset();

						if (string != null) {
							processProperties(string, node2);
						}

						if (results.size() > 0) {
							System.out.println("n1 : " + n);

							for (JsonNode node : results) {
								System.out.println("result : " + node);
							}
						}
					}
					catch (Exception e) {
						// Ignore
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//================================================================================
	// Make Property Node
	//================================================================================
	public JsonNode createPropertyNode(String type, JsonNode old) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode property = mapper.createObjectNode();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode array = mapper.createArrayNode();
		ObjectNode val = mapper.createObjectNode();

		val.put("id", type);
		array.add(val);
		node.put("values", array);
		property.put("/type/object/type", node);
		property.put(type, old);

		return property;
	}
}
