package org.baldeapi.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.persistence.MongoManager;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;
import org.baldeapi.v1.resources.business.BusinessMapper;
import org.baldeapi.v1.security.User;
import org.bson.types.ObjectId;


import com.mongodb.BasicDBObject;
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
	
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@RolesAllowed({"GRANTED"})
	public Response put( @PathParam("resource")	String	resource
						, @Context 					UriInfo uriInfo
						, @Context					SecurityContext securityContext
						, String entity
						) {
	
		User user = (User) securityContext.getUserPrincipal();
		
		DBObject object = (DBObject) JSON.parse(entity);
		
		object.put("_id", new ObjectId(id));
		
		BusinessMapper business = BusinessMapper.getInstance(resource, user);
		
		business.applyRules(object);
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		object = dao.insertOrUpdate(object);
		
		String json = JSON.serialize(object);
		
		return Response.ok(json).build();
		
	}
	
	@DELETE
	@Produces("application/json")
	@RolesAllowed({"GRANTED"})
	public Response del( @PathParam("resource")	String	resource
						, @Context 					UriInfo uriInfo
						, @Context					SecurityContext securityContext
						) {
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		dao.delete(id);
		
		return Response.ok(" ").build();
		
	}
	
}
