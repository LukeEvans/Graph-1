package com.winston.response;

import java.util.ArrayList;
import java.util.Map;

import com.winston.metadata.Confluence;
import com.winston.metadata.Keyword;
import com.winston.metadata.MetaData;
import com.winston.youtube.YoutubeVideo;

public class ResponseObject {

	public String status;
	public String time;
	long timeLong;
	long start;
	long stop;
	public Object data;
	
	//================================================================================
	// Constructor
	//================================================================================
	public ResponseObject() {
		status = "OK";
	}
	
	//================================================================================
	// Start timer
	//================================================================================
	public void startTimer() {
		start = System.currentTimeMillis();
	}
	
	//================================================================================
	// Stop timer
	//================================================================================
	public void stopTimer() {
		stop = System.currentTimeMillis();
		
		timeLong = stop - start;
		
		time = timeLong + " ms";
	}
	
	//================================================================================
	// Add data
	//================================================================================
	public void addData(MetaData object, boolean alternateFormat) {
		if(alternateFormat)
			addData(object);
		else
			data = object;
	}
	
	public void addData(MetaData metaData){
		if(metaData == null || metaData.confluence == null)
			return;
	
		ArrayList<ArrayList<Object>> newData = new ArrayList<ArrayList<Object>>();	
		if(metaData.keywords!= null && !metaData.keywords.isEmpty())
			newData.add(extractWiki(metaData.keywords));
			
		Confluence confluence = metaData.confluence;	
		if(confluence.topic_facebook != null && !confluence.topic_facebook.isEmpty())
			newData.add(arrayFromDataMap(confluence.topic_facebook));
		if(confluence.topic_twitter != null && !confluence.topic_twitter.isEmpty())
			newData.add(arrayFromDataMap(confluence.topic_twitter));
		if(confluence.topic_news != null && !confluence.topic_news.isEmpty())
			newData.add(arrayFromDataMap(confluence.topic_news));
		if(confluence.topic_youtube != null && !confluence.topic_youtube.isEmpty())
			newData.add(arrayFromData(confluence.topic_youtube));
		if(confluence.related_facebook != null && !confluence.related_facebook.isEmpty())
			newData.add(arrayFromDataMap(confluence.related_facebook));
		if(confluence.related_twitter != null && !confluence.related_twitter.isEmpty())
			newData.add(arrayFromDataMap(confluence.related_twitter));
		if(confluence.related_news != null && !confluence.related_news.isEmpty())
			newData.add(arrayFromDataMap(confluence.related_news));
		data = newData;
	}
	
	private ArrayList<Object> arrayFromDataMap(ArrayList<Map<String, Object>> dataMap){
		ArrayList<Object> array = new ArrayList<Object>();		
		for(Map<String, Object> data: dataMap)
			array.add(data);
		
		return array;
	}
	
	private ArrayList<Object> arrayFromData(ArrayList<YoutubeVideo> dataMap){
		ArrayList<Object> array = new ArrayList<Object>();		
		for(Object data: dataMap)
			array.add(data);
		
		return array;
	}
	
	private ArrayList<Object> extractWiki(ArrayList<Keyword> keywords){
		ArrayList<Object> wikiEntries = new ArrayList<Object>();
		for(Keyword keyword: keywords){
			if(keyword.candidates != null && !keyword.candidates.isEmpty())
				wikiEntries.add(keyword.candidates.get(0));
		}
		return wikiEntries;
	}
}
