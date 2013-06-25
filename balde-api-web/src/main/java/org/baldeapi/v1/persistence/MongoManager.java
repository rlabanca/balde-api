package org.baldeapi.v1.persistence;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoManager {

	private static DB database;
	
	private static final Logger log = LoggerFactory.getLogger(MongoManager.class);
	
	public static void init(String uri, String databaseName) {
		if (database != null) {
			throw new IllegalStateException("Database has already been initialized");
		}
		
		//TODO implementar a configura��o de banco de dados
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(new MongoClientURI(uri));
			
		} catch (UnknownHostException e) {
			log.error("Error initializing mongodb", e);
		}
		
		if (mongoClient == null) {
			database = null;
		} else {
			database = mongoClient.getDB(databaseName);
		}
	}
	
	public static DB getDatabase() {
		if (database == null) {
			throw new IllegalStateException("Database has not been initialized");
		}
		return database;
	}
	
}
