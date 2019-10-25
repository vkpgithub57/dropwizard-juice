package com.poc.util;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.setup.Environment;


public class MongoDBConnection {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBConnection.class);

	private static MongoCollection<Document> collection;
	private MongoClient mongoClient;
	public MongoDBConnection(MongoDBConfig config, Environment env) {		
		MongoClient client= new MongoClient(config.getMongoHost(), config.getMongoPort());
        MongoManaged mongoManaged = new MongoManaged(client);
        env.lifecycle().manage(mongoManaged);
        MongoDatabase db = client.getDatabase(config.getMongoDB());
        MongoCollection<Document> collect = db.getCollection(config.getCollectionName());
        if(collect.count()==0) {
        	logger.info("First time Default document get created in collection");
        	Document defaultDocument = new Document();
        	defaultDocument.put("firstName", "Test");
        	defaultDocument.put("lastName", "Default");
        	defaultDocument.put("email", "test@gmail.com");
        	defaultDocument.put("phone", 989898);
        	collect.insertOne(defaultDocument);
        }
        collection = collect;
        mongoClient = client;
	}

	public static MongoCollection<Document> getCollection() {
		return collection;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}	

	/*public static void setCollection(MongoCollection<Document> collection) {
		MongoDBFactoryConnection.collection = collection;
	}*/
}
