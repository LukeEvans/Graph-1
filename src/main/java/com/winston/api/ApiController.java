package com.winston.api;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.winston.elasticsearch.ElasticSearch;
import com.winston.metadata.Analyzer;
import com.winston.response.ResponseObject;
import com.winston.utilities.Tools;


/**
 * Handles and retrieves main requests
 */
@Controller
@RequestMapping("/")
public class ApiController {

	protected static Logger logger = Logger.getLogger("controller");

	//================================================================================
	// Default
	//================================================================================
	@RequestMapping(value = "/")
	@ResponseBody
	public Object home() {		
		return "Winston Story Graph Mark II";
	}


	//================================================================================
	// Process
	//================================================================================
	@Autowired
	@Qualifier("analyzer")
	Analyzer analyzer;
	
	@RequestMapping(value = "/metadata")
	@ResponseBody
	public Object metadata(@RequestParam Map<String,String> params, @RequestBody String input) {
		
		logger.info("=====================================");
		logger.info("Received metadata request");
		logger.info("=====================================");
		
		try {

			JsonNode request = Tools.mergeBodyAndURL(params, input);
			String text = request.path("text").asText();
			
			boolean shallow = false;
			if (request != null && request.path("shallow") != null && !request.path("shallow").isMissingNode()) {
				shallow = request.path("shallow").asBoolean();
			}
			
			// Start timer
			ResponseObject responseObject = new ResponseObject();
			responseObject.startTimer();
			
			// Just look for entities and basic metadata
			if (shallow) {
				responseObject.addData(analyzer.shallowProcess(text));
			}
			
			// Find entities, metadata, connections, and confluence
			else {
				responseObject.addData(analyzer.process(text));
			}
			
			// Stop timer
			responseObject.stopTimer();
			
			return responseObject;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//================================================================================
	// Test
	//================================================================================
	@Autowired
	@Qualifier("elasticsearch")
	ElasticSearch elasticSearch;
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public Object test(@RequestParam Map<String,String> params, @RequestBody String input) {
		
		logger.info("=====================================");
		logger.info("Received metadata request");
		logger.info("=====================================");
		
		try {

			JsonNode request = Tools.mergeBodyAndURL(params, input);
			String id = request.path("id").asText();
			String index = request.path("index").asText();
			
			return elasticSearch.test(index, id);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//================================================================================
	// Health
	//================================================================================
	// AWS uses this to check the status of the server

	@RequestMapping(value = "/health")
	@ResponseBody
	public Object health() {
		return "OK";
	}


}
