package com.winston.metadata;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.winston.connections.ConnectionSet;
import com.winston.disambiguation.Disambiguator;
import com.winston.elasticsearch.ElasticSearch;
import com.winston.extraction.Extractor;
import com.winston.graphdb.GraphManager;
import com.winston.mongo.Mongo;

@Component("analyzer")
public class Analyzer {

	@Autowired
	@Qualifier("extractor")
	Extractor extractor;

	@Autowired
	@Qualifier("disambiguator")
	Disambiguator disambiguator;

	@Autowired
	@Qualifier("metadatafetcher")
	MetaDataFetcher fetcher;

	@Autowired
	@Qualifier("graphmanager")
	GraphManager gremlin;

	@Autowired
	@Qualifier("elasticsearch")
	ElasticSearch elasticSearch;
	
	@Autowired
	@Qualifier("mongo")
	Mongo mongo;
	
	//================================================================================
	// Process
	//================================================================================
	public MetaData process(String fullText) {

		// Create MetaData object
		MetaData metaData = new MetaData(fullText);

		// Add keywords
		extractor.extractKeywords(metaData);

		// Add MIDs
		disambiguator.fetchIDs(metaData);

		// Find all connections
		determineAllConnections(metaData);

		// Prune 
		pruneMetaData(metaData);

		// Get Vertex meta data
		grabMetaData(metaData);

		// Grab confluence
		grabConfluence(metaData);
		
		return metaData;

	}

	//================================================================================
	// Process, but only grab entities. No connections or confluence 
	//================================================================================
	public MetaData shallowProcess(String fullText) {
		// Create MetaData object
		MetaData metaData = new MetaData(fullText);

		// Add keywords
		extractor.extractKeywords(metaData);

		// Add MIDs
		disambiguator.fetchIDs(metaData);
		
		// Keep only the top candidates
		pruneForTopCandidates(metaData);
		
		// Get Vertex meta data
		grabMetaData(metaData);
		
		return metaData;
	}
	
	//================================================================================
	// Determine all Connections
	//================================================================================
	public void determineAllConnections(MetaData metaData) {

		// Get the matrix
		metaData.initConnectionMatrix();

		ArrayList<Future<Void>> pending = new ArrayList<Future<Void>>();

		// Fetch all connections
		for (ConnectionSet set : metaData.connection_matrix.grabSets()) {
			pending.add(fetcher.fetchConnections(set));
		}

		// Collect all connections
		for (Future<Void> p : pending) {
			try {
				p.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	//================================================================================
	// Prune metadata
	//================================================================================
	public void pruneMetaData(MetaData metaData) {
		metaData.prune();
	}

	//================================================================================
	// Prune metadata keeping only top candidates
	//================================================================================
	public void pruneForTopCandidates(MetaData metaData) {
		metaData.pruneForTopCandidates();
	}
	
	//================================================================================
	// Fetch Meta Data for all Candidates
	//================================================================================
	public void grabMetaData(MetaData metaData) {
		ArrayList<Future<Void>> pending = new ArrayList<Future<Void>>();

		// Fetch all metadata
		for (Keyword keyword : metaData.keywords) {
			for (Candidate candidate : keyword.candidates) {
				pending.add(fetcher.fetchMetaData(candidate));
			}
		}

		// Wait for metadata
		for (Future<Void> p : pending) {
			try {
				p.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	//================================================================================
	// Grab news, twitter, etc...
	//================================================================================
	public void grabConfluence(MetaData metaData) {

		try {
			ArrayList<String> topic = metaData.topicIDList();
			ArrayList<String> related = new ArrayList<String>();

			metaData.confluence = fetcher.fetchConfluence(topic, related).get();
			
			elasticSearch.findTwitterConfluence(metaData.confluence);
			mongo.findNewsConfluence(metaData.confluence);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
