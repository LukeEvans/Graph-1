package com.winston.graphdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.rexster.client.RexProException;
import com.tinkerpop.rexster.client.RexsterClient;
import com.tinkerpop.rexster.client.RexsterClientFactory;
import com.winston.connections.ConnectionSet;
import com.winston.metadata.Confluence;

@Component("graphmanager")
public class GraphManager {

	// Rexster client
	static RexsterClient client;
	ObjectMapper mapper;

	@PostConstruct
	public void init() {

		try {
//			client = RexsterClientFactory.open("ec2-54-211-25-154.compute-1.amazonaws.com");
			client = RexsterClientFactory.open("10.170.43.190");
			
			// Going to be used with multiple Rexster servers
//			BaseConfiguration conf = new BaseConfiguration() {{
//			    addProperty(RexsterClientTokens.CONFIG_HOSTNAME, "10.170.43.190,10.171.7.176");
//			    addProperty(RexsterClientTokens.CONFIG_MESSAGE_RETRY_WAIT_MS, 0);
//			}};
//			client = RexsterClientFactory.open(conf);
			
			mapper = new ObjectMapper();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//================================================================================
	// Get Vertex by mid
	//================================================================================
	@SuppressWarnings("serial")
	public JsonNode findVertexDetails(final String mid) {
		try {
			List<String> results = client.execute("g = rexster.getGraph('reactorgraph'); r = new Reactor(g); r.findDetailsForMid(mid)", 
					new HashMap<String, Object>(){{
						put("mid", mid);
					}});
			
			
			String jsonString = results.get(0); 
			JsonNode detailNode = mapper.readTree(jsonString);
			return detailNode;

		} catch (Exception e) {
			// Ignore
			e.printStackTrace();
			return null;
		}
	}

	//================================================================================
	// Find Connections
	//================================================================================
	@SuppressWarnings("serial")
	public ConnectionSet findConnections(final String mid1, final String mid2, ConnectionSet set) {
		
		if (mid1.equalsIgnoreCase(mid2)) {
			return set;
		}
		
		try {

			List<String> results = client.execute("g = rexster.getGraph('reactorgraph'); r = new Reactor(g); r.defineConnections(mid1,mid2)", 
				    new HashMap<String, Object>(){{
				        put("mid1", mid1);
				        put("mid2", mid2);
				    }});

			String jsonString = results.get(0); 
			JsonNode setNode = mapper.readTree(jsonString);
			
			ConnectionSet newSet = new ConnectionSet(setNode);
			set.connections = newSet.connections;

		} catch (RexProException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	//================================================================================
	// Find confluence
	//================================================================================
	@SuppressWarnings("serial")
	public Confluence findConfluence(final ArrayList<String> topic, final ArrayList<String> related) {
		try {
			List<String> results = client.execute("g = rexster.getGraph('reactorgraph'); r = new Reactor(g); r.findConfluence(a,b)", 
				    new HashMap<String, Object>(){{
				        put("a", topic);
				        put("b", related);
				    }});
			
			String jsonString = results.get(0);
			JsonNode confluenceNode = mapper.readTree(jsonString);
			Confluence confluence = new Confluence(confluenceNode);
			
			return confluence;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
