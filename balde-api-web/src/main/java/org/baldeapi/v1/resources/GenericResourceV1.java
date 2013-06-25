package org.baldeapi.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.persistence.MongoManager;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;


import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class GenericResourceV1 {
	
	private String resource;
	private String id;
	
	public GenericResourceV1(String resource, String id) {
		super();
		this.resource = resource;
		this.id = id;
	}

	@GET
	@RolesAllowed({"GRANTED"})
	@Produces({MediaType.APPLICATION_JSON})
	public Response get() {
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		DBObject object = dao.findOne(id);
		
		if (object == null) {
			
			ErrorsRepresentationV1 errors = ErrorsRepresentationV1.notFound();
			
			return Response.status(Status.NOT_FOUND).entity(errors).build();
			
		}

		String json = JSON.serialize(object);
		
		return Response.ok(json).build();
		
	}
		
}
