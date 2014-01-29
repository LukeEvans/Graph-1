package com.winston.disambiguation;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.winston.metadata.Candidate;
import com.winston.metadata.Keyword;
import com.winston.utilities.Tools;

@Component("disambiguationfetcher")
public class DisambiguationFetcher {

	@Async
	public Future<Void> search(String url, Keyword keyword) {
		JsonNode result = Tools.fetchURL(url);
		
		// Add candidate
		for (JsonNode node : result.path("result")) {
			keyword.addCandidate(new Candidate(node));
			
			if (keyword.candidateCount() == Keyword.MAX_CANDIDATES) {
				break;
			}
		}
		
		return new AsyncResult<Void>(null);
	}
}
