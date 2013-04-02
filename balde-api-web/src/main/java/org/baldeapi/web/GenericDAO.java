/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.baldeapi.web;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class GenericDAO {
    DBCollection collection;
    String collectionName;

    public GenericDAO(final DB database, final String collectionName) {
        collection = database.getCollection(collectionName);
        this.collectionName = collectionName;
    }
    
    public List<DBObject> find(DBObject filter, DBObject sort, int skip, int limit) {
    	
		DBCursor cursor = collection.find(filter).sort(sort).skip(skip).limit(limit);
		
		List<DBObject> result = null;
		
		try {
			result = cursor.toArray();
		} finally {
			cursor.close();
		}
        
        return result;
        
    }
    
    public DBObject findOne(String id) {
    	DBObject filter = new BasicDBObject("_id", new ObjectId(id));
		return findOne(filter);
    }

	public DBObject findOne(DBObject filter) {
		return collection.findOne(filter);
	}

	public DBObject insert(DBObject object) {
		
		collection.insert(object);
		
		return object;
		
	}
	
	public DBObject insertOrUpdate(DBObject object) {
		
		DBObject query = new BasicDBObject("_id", object.get("_id"));
		
		object.removeField("_id");
		
		collection.update(query, object, true, false);
		
		return object;
		
	}

	public void delete(String id) {
		
		DBObject object = new BasicDBObject("_id", new ObjectId(id));
		
		collection.remove(object);
		
	}

}
