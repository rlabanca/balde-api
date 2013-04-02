package org.baldeapi.web;


import static spark.Spark.*;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

/**
 * This class encapsulates the controllers for the blog web application.  It delegates all interaction with MongoDB
 * to three Data Access Objects (DAOs).
 * <p/>
 * It is also the entry point into the web application.
 */
public class ApiController {
    
    public static void main(String[] args) throws IOException {
    	/*
    	 * Define arguments as host, database, etc...
    	 */
        if (args.length == 0) {
            new ApiController("mongodb://localhost");
        }
        else {
            new ApiController(args[0]);
        }
    }

    public ApiController(String mongoURIString) throws IOException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final DB database = mongoClient.getDB("api");
        
        setPort(8082);
        initializeRoutes(database);
    }

    private void initializeRoutes(final DB database) throws IOException {
    	
    	before(new Filter() {
			@Override
			public void handle(Request req, Response res) {
				String apiKey = req.headers("X-API-Key");
				GenericDAO dao = new GenericDAO(database, "api");
				DBObject object = dao.findOne(new BasicDBObject("apiKey", apiKey));
				if (object == null) {
					halt(401, "You must send a valid X-API-Key header");
				}
			}
		});
    	
        get(new Route(":bucket") {
			@Override
			public Object handle(Request req, Response res) {
				
				String bucket = req.params("bucket");
				String jsonFilter = req.queryParams("filter");
				String jsonSort = req.queryParams("sort");
				String skipParam = req.queryParams("skip");
				String limitParam = req.queryParams("limit");
				
				DBObject filter = (DBObject) JSON.parse(jsonFilter);
				DBObject sort = (DBObject) JSON.parse(jsonSort);
				
				int skip = 0;
				if (skipParam != null) {
					skip = Integer.parseInt(skipParam);
				}
				
				int limit = 0;
				if (limitParam != null) { 
					limit = Integer.parseInt(limitParam);
				}
				
				GenericDAO dao = new GenericDAO(database, bucket);
				List<DBObject> result = dao.find(filter, sort, skip, limit);
				
				if (result == null || result.size() == 0) {
					res.status(404);
					return "";
				}
				
				return result;
				
			}
		});
        
        post(new Route(":bucket") {
			
			@Override
			public Object handle(Request req, Response res) {
				try {
					String bucket = req.params("bucket");
					String json = req.body();
					
					GenericDAO dao = new GenericDAO(database, bucket);
					DBObject object = (DBObject) JSON.parse(json);
					
					object = dao.insert(object);
					
					res.status(201);
					res.header("Location", req.url() + "/" + object.get("_id"));
					
					return object;
				} catch (Exception e) {
					res.status(500);
					//TODO create an error representation
					return e.toString();
				}
			}
		});
        
	    get(new Route(":bucket/:id") {
	    	@Override
			public Object handle(Request req, Response res) {
				
				String bucket = req.params("bucket");
				String id = req.params("id");
				
				GenericDAO dao = new GenericDAO(database, bucket);
				DBObject result = dao.findOne(id);
				
				if (result == null) {
					res.status(404);
					return "";
				}
				
				return result;
				
			}
	    });
	    
	    put(new Route(":bucket/:id") {
	    	@Override
			public Object handle(Request req, Response res) {
	    		try {
					String bucket = req.params("bucket");
					String id = req.params("id");
					String json = req.body();
					
					GenericDAO dao = new GenericDAO(database, bucket);
					
					DBObject object = (DBObject) JSON.parse(json);
					if (!object.containsField("_id")) {
						object.put("_id", new ObjectId(id));
					}
					
					object = dao.insertOrUpdate(object);
					
					return object;
				} catch (Exception e) {
					res.status(500);
					//TODO create an error representation
					return e.toString();
				}
	    	}
	    });
	    
	    delete(new Route(":bucket/:id") {
	    	@Override
			public Object handle(Request req, Response res) {
				
				String bucket = req.params("bucket");
				String id = req.params("id");
				
				GenericDAO dao = new GenericDAO(database, bucket);
				dao.delete(id);
				
				return "";
				
			}
	    });
	    
    
    }
}
