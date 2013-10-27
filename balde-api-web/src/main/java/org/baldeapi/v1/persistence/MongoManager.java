package org.baldeapi.v1.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.helpers.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoManager {

	private static DB database;
	
	private static final Logger log = LoggerFactory.getLogger(MongoManager.class);
	
	

	public synchronized static String getDatabaseFromEnviroment() {
		URL url = Loader.getResource("database-resources.properties");
		String dbName = null;
		try {
			if(dbName == null) {
				ResourceBundle bundle = new PropertyResourceBundle(new FileInputStream(url.getFile()));
				dbName = bundle.getString("mongo.db");
				System.out.println("load config in " + url);
				System.out.println("mongo database name " + dbName);
			}
		}  catch (IOException e) {
			e.printStackTrace();
		}	
		
		return dbName;
	}
	
	public static void init(String uri, String databaseName) {
		if (database != null) {
			throw new IllegalStateException("Database has already been initialized");
		}
		
		//TODO implementar a configuracao de banco de dados
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
