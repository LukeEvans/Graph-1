package com.winston.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoURI;

@Configuration
public class SpringDataConfig {

	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		MongoURI uri = new MongoURI("mongodb://levans002:dakota1@ds031887.mongolab.com:31887/winston-db");
		return new SimpleMongoDbFactory(uri);
	}

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}
	
	

}