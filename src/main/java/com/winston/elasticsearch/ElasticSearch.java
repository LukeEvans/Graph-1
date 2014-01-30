package com.winston.elasticsearch;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winston.metadata.Confluence;

@Service("elasticsearch")
public class ElasticSearch {

	// Elasticsearch client 
	Client client;
	ObjectMapper mapper;

	//================================================================================
	// Init
	//================================================================================
	@PostConstruct
	public void init() {
		// Elastic Search
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "es_sg").build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("ec2-54-211-99-5.compute-1.amazonaws.com", 9300));

		// Define mapper
		mapper = new ObjectMapper();
	}

	//================================================================================
	// Find Twitter Confluence
	//================================================================================
	public void findTwitterConfluence(Confluence confluence) {
		LinkedHashMap<String, ActionFuture<SearchResponse>> pending = new LinkedHashMap<String, ActionFuture<SearchResponse>>();

		// Add twitter to list
		for (String id : confluence.grabTwitterIDs()) {
			pending.put(id, client.prepareSearch("twitter").setQuery(QueryBuilders.queryString(id).defaultField("id")).execute());
		}
		
		// Combine them back together
		for (Entry<String, ActionFuture<SearchResponse>> pendingEntry : pending.entrySet()) {
			String id = pendingEntry.getKey();
			SearchResponse resp = pendingEntry.getValue().actionGet();
			Map<String, Object> value = getTopJsonFromSearch(resp);

			// Add node back to confluence
			if (value != null) {
				confluence.addNode(id, value);
			}
		}
	}

	//================================================================================
	// Get top json node from search result
	//================================================================================
	public Map<String, Object> getTopJsonFromSearch(SearchResponse response) {
		try {
			Map<String, Object> map = response.getHits().getAt(0).getSource();
			Map<String, Object> top = null;
			for(SearchHit hit: response.getHits()){
				map = hit.getSource();
				String db = map.get("db").toString();
				if(map.get("valid") != null && ((Boolean) map.get("valid") || db.equalsIgnoreCase("Twitter"))){
					top = map;
					break;
				}
			}

			return top;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//================================================================================
	// Test
	//================================================================================
	public Map<String, Object> test(String index, String id) {
		try {
			SearchResponse response = client.prepareSearch(index).setQuery(QueryBuilders.queryString(id).defaultField("id")).execute().actionGet();
			Map<String, Object> map = response.getHits().getAt(0).getSource();
			return map;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
