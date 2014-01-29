package com.winston.metadata;

import java.util.ArrayList;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.winston.connections.ConnectionSet;
import com.winston.graphdb.GraphManager;

@Component("metadatafetcher")
public class MetaDataFetcher {

	@Autowired
	@Qualifier("graphmanager")
	GraphManager gremlin;

	//================================================================================
	// Fetch a candidate's full metadata
	//================================================================================
	@Async
	public Future<Void> fetchMetaData(Candidate candidate) {
		JsonNode details = gremlin.findVertexDetails(candidate.mid);
		candidate.grabVertexMetaData(details);
		return new AsyncResult<Void>(null);
	}
	
	//================================================================================
	// Find Connection between two items
	//================================================================================
	@Async
	public Future<Void> fetchConnections(ConnectionSet set) {
		gremlin.findConnections(set.source_id, set.target_id, set);
		return new AsyncResult<Void>(null);
	}
	
	//================================================================================
	// Find confluence
	//================================================================================
	@Async
	public Future<Confluence> fetchConfluence(ArrayList<String> topic, ArrayList<String> related) {
		Confluence confluence = gremlin.findConfluence(topic, related);
		return new AsyncResult<Confluence>(confluence);
	}
}
