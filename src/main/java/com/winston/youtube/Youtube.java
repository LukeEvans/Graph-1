package com.winston.youtube;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.winston.metadata.MetaData;
import com.winston.utilities.Tools;

/***
 * Youtube popular video streams
 */
public class Youtube {
	private static String baseDataUrl = "https://www.googleapis.com/youtube/v3/";
	private static String apiKey = "AIzaSyDEv9_zQNRGpw789wTE5NbnUn4IywHUR5U";

	// Most popular
	public static ArrayList<YoutubeVideo> mostPopularFeed(){
		JsonNode responseNode = Tools.fetchURL(baseDataUrl 
				+ "videos?part=statistics,snippet&chart=mostPopular&maxResults=10&regionCode=us&key="
				+ apiKey); 

		if(responseNode.has("items"))
			return createVideoSet(responseNode.get("items"));
		else
			return new ArrayList<YoutubeVideo>();
	}

	// Create list of youtube videos
	private static ArrayList<YoutubeVideo> createVideoSet(JsonNode videosNode){
		ArrayList<YoutubeVideo> videoSet = new ArrayList<YoutubeVideo>();

		for(JsonNode videoNode: videosNode){
			videoSet.add(new YoutubeVideo(videoNode));
		}

		return videoSet;
	}

	// Topic videos
	public static void topicVideos(MetaData metaData) {
		ArrayList<YoutubeVideo> videoSet = new ArrayList<YoutubeVideo>();
		
		// Phrase search
		for (YoutubeVideo video : phraseSearch(3, metaData.free_text)) {
			addVideoToList(videoSet, video);
		}
		
		// Add all videos to confluence
		for (YoutubeVideo video : videoSet) {
			metaData.confluence.addVideoNode(video);
		}
	}

	// Freebase topic video search
	public static ArrayList<YoutubeVideo> freebaseSearch(int max, String id) {

		ArrayList<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();

		try {
			JsonNode response = Tools.fetchURL(baseDataUrl
					+ "search?part=snippet&type=video&maxResults=10&regionCode=us&key="
					+ apiKey 
					+ "&topicId="
					+ id);

			for (JsonNode videoNode : response.path("items")) {
				videos.add(new YoutubeVideo(videoNode));
				
				if (videos.size() >= max) break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<YoutubeVideo>();
		}

		return videos;
	}
	
	// Topic video search
	public static ArrayList<YoutubeVideo> phraseSearch(int max, String text) {
		ArrayList<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
		JsonNode response = Tools.fetchURL(baseDataUrl
				+ "search?part=snippet&type=video&maxResults=10&regionCode=us&key="
				+ apiKey 
				+ "&q="
				+ text);		

		for (JsonNode videoNode : response.path("items")) {
			videos.add(new YoutubeVideo(videoNode));
			
			if (videos.size() >= max) break;
		}
		
		return videos;
	}
	
	// Determine if video is already in list of videos
	public static void addVideoToList(ArrayList<YoutubeVideo> videos, YoutubeVideo newVid) {
		for (YoutubeVideo video : videos) {
			if (newVid.id.equalsIgnoreCase(video.id)) {
				return;
			}
		}
		
		videos.add(newVid);
	}
}