package com.winston.disambiguation;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.winston.metadata.Keyword;
import com.winston.metadata.MetaData;

@Component("disambiguator")
public class Disambiguator {

	private String disambigURL = "https://www.googleapis.com/freebase/v1/search?key=AIzaSyDgEm2hDVbVeruKXokiMyrShPZaVL4VICU&query=";

	@Autowired
	@Qualifier("disambiguationfetcher")
	DisambiguationFetcher fetcher;

	//================================================================================
	// Process
	//================================================================================
	public MetaData fetchIDs(MetaData metaData) {

		ArrayList<Future<Void>> pending = new ArrayList<Future<Void>>();
		
		// Send all off to be fetched
		for (Keyword keyword : metaData.keywords) {
			pending.add(fetcher.search(disambigURL + keyword.original_text, keyword));
		}

		// Collect all
		for (Future<Void> p : pending) {
			try {
				p.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return metaData;
	}
}
