package com.winston.youtube;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

public class YoutubeVideo {
    public String id;
    public String title;
    public String channel;
    public String embedded_html;
    public String description;
    public String thumbnail;
    public Date date;
    public final String image_url = "https://s3.amazonaws.com/Channel_Icons/youtube_icon.png";
    public final String type = "youtube";
    public int view_count;
    public int likes;
    public int dislikes;
    
    public YoutubeVideo(JsonNode videoNode){
            if(videoNode.has("id") && videoNode.path("id").has("videoId"))
                    id = videoNode.path("id").path("videoId").asText();
            if(id != null){
                embedded_html = "<iframe width=\"560\" height=\"315\" "
                                + "src=\"//www.youtube.com/embed/" + id
                                + " frameborder=\"0\" allowfullscreen></iframe>";
            }
            if(videoNode.has("snippet")){
                    JsonNode snipNode = videoNode.get("snippet");
                    if(snipNode.has("title"))
                            title = snipNode.get("title").asText();
                    if(snipNode.has("channelTitle"))
                            channel = snipNode.get("channelTitle").asText();
                    if(snipNode.has("description"))
                            description = snipNode.get("description").asText();
                    if(snipNode.has("thumbnails"))
                            thumbnail = snipNode.get("thumbnails").get("high").get("url").asText();
                    if(snipNode.has("publishedAt")){
                            try{
                                String dateString = snipNode.get("publishedAt").asText();
                                    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
                                    
                            } catch(Exception e){
                                    e.printStackTrace();
                            }
                    }
            }
            if(videoNode.has("statistics")){
                    JsonNode statNode = videoNode.get("statistics");
                    if(statNode.has("viewCount"))
                            view_count = statNode.get("viewCount").asInt();
                    if(statNode.has("likeCount"))
                            likes = statNode.get("likeCount").asInt();
                    if(statNode.has("dislikeCount"))
                            dislikes = statNode.get("dislikeCount").asInt();                   
            }        
    }
}