package com.winston.extraction;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.winston.metadata.Keyword;
import com.winston.metadata.MetaData;
import com.winston.utilities.Tools;

@Component("extractor")
public class Extractor {

	private String nlpURL = "http://access.alchemyapi.com/calls/text/TextGetRankedKeywords?apikey=fb5e30fc9358653bee86ffd698ed024aae33325c&outputMode=json&text=";

	//================================================================================
	// Process
	//================================================================================
	public MetaData extractKeywords(MetaData metaData) {

		try {

			// Add all keywords
			JsonNode results = Tools.fetchURL(nlpURL + metaData.free_text);

			for (JsonNode r : results.path("keywords")) {
				metaData.addKeyword(new Keyword(r));
			}

			// Also add word shape hints
			for (String s : findPossibleWordShapeHints(metaData.free_text)) {
				metaData.addKeyword(new Keyword(s));
			}

			return metaData;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//================================================================================
	// Get list of possible phrases based on Word Shape
	//================================================================================
	public ArrayList<String> findPossibleWordShapeHints(String text) {
		ArrayList<String> hints = new ArrayList<String>();

		Pattern pattern = Pattern.compile("[A-Z]\\w+\\s+[A-Z]\\w+");

		Matcher m = pattern.matcher(text);
		while (m.find()) {
			String s = m.group(0);
			hints.add(s);

		}

		pattern = Pattern.compile("(?:the|an|a|that|this)\\s+([A-Z]\\w+)(?:[\\s\\W]($|[^A-Z]))");
		
		m = pattern.matcher(text);
		while (m.find()) {
			String s = m.group(1);
			hints.add(s);
		}
		
		return hints;
	}
}
