package com.winston.mongo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.winston.metadata.Confluence;

@Component("mongo")
public class Mongo {

	@Autowired 
	MongoOperations mongoOps;

	//================================================================================
	// Find Operations
	//================================================================================
	public Object findOne(Query query, Class<?> class1, String collection) {	
		synchronized (mongoOps) {
			return mongoOps.findOne(query, class1,collection);	
		}
	}
	
	public Object find(Query query, Class<?> class1, String collection) {
		synchronized (mongoOps) {
			return mongoOps.find(query, class1, collection);
		}
	}
	
	
	//================================================================================
	// Save Operations
	//================================================================================
	public void save(Object obj, String collection) {
		synchronized (mongoOps) {
			mongoOps.save(obj,collection);	
		}
	}

	//================================================================================
	// Update
	//================================================================================
	public void updateFirst(Query query, Update update, String collection) {
		synchronized (mongoOps) {
			mongoOps.updateFirst(query, update, collection);
		}
	}
	
	//================================================================================
	// Upsert
	//================================================================================
	public void upsert(Query query, Update update, String collection) {
		synchronized (mongoOps) {
			mongoOps.upsert(query, update, collection);
		}
	}
	
	//================================================================================
	// Find and modify
	//================================================================================
	public void findAndModify(Query query, Update update, Class<?> class1, String collection) {
		synchronized (mongoOps) {
			mongoOps.findAndModify(query, update, class1, collection);
		}
	}
	
	//================================================================================
	// Get Collection
	//================================================================================
	public DBCollection getCollection(String collection) {
		synchronized (mongoOps) {
			return mongoOps.getCollection(collection);
		}
	}
	
	//================================================================================
	// Remove
	//================================================================================
	public void findAndDelete(Query query, String collection) {
		synchronized (mongoOps) {
			mongoOps.remove(query, collection);
			
			//mongoOps.findAndRemove(query, Object.class, collection);
		}
	}
	
	
	//================================================================================
	// Array Lookups
	//================================================================================
	public Object findArrayItem() {
		//Criteria c = new Criteria().elemMatch(new Criteria().where(key))
		//Query query = new Query(
		
		return null;
	}
	
	//================================================================================
	// Find News Confluence
	//================================================================================
	public void findNewsConfluence(Confluence confluence) {
		for (String id : confluence.grabNewsIDs()) {
			Query query = new Query(Criteria.where("id").is(id));
			BasicDBObject obj = (BasicDBObject) findOne(query, BasicDBObject.class, "winston-news");
			
			Map<String, Object> value = obj.toMap();
			
			value.remove("_id");
			value.remove("_class");
			value.put("id", id);
			confluence.addNode(id, value);
		}
	}
}



