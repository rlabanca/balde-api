package org.baldeapi.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.persistence.MongoManager;
import org.baldeapi.v1.representation.ErrorRepresentationV1;
import org.baldeapi.v1.representation.ErrorType;
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
	private User user;
	
	public GenericResourceV1(String resource, String id, User user) {
		super();
		this.resource = resource;
		this.id = id;
		this.user = user;
	}

	@GET
	@RolesAllowed({"GRANTED"})
	@Produces({MediaType.APPLICATION_JSON})
	public Response get() {
		
		try {
			DBObject object = findObject();
			
			if (object == null) {
				
				ErrorsRepresentationV1 errors = ErrorsRepresentationV1.notFound();
				
				return Response.status(Status.NOT_FOUND).entity(errors).build();
				
			}
			
			String json = JSON.serialize(object);
			
			return Response.ok(json).build();
		} catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.LAST_MINUTE_ERROR, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}
	}
	
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	@RolesAllowed({"GRANTED","BPSSOAUTH"})
	public Response post( @PathParam("resource")	String	resource
						, @Context 					UriInfo uriInfo
						, @Context					SecurityContext securityContext
						, String entity
						) {
	
		try {
			User user = (User) securityContext.getUserPrincipal();
			
			DBObject object = (DBObject) JSON.parse(entity);
			
			DBObject query = new BasicDBObject("_id", new ObjectId(id));
			
			BusinessMapper business = BusinessMapper.getInstance(resource, user);
			business.applyPutRules(query, object);
			
			if (business.hasErrors()) {
				
				ErrorsRepresentationV1 errors = business.getErrors();
				
				return Response.status(Status.BAD_REQUEST).entity(errors).build();
				
			}
			
			GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
			object = dao.updateFields(query, object);
			object = dao.findOne(id);
			
			String json = JSON.serialize(object);
			
			return Response.ok(json).build();
		} catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.LAST_MINUTE_ERROR, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}
		
	}

	private DBObject findObject() {
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		Object oid;
		
		if (ObjectId.isValid(id)) {
			oid = new ObjectId(id);
		} else {
			oid = id;
		}
		
		DBObject filter = new BasicDBObject("_id", oid);
		
		BusinessMapper business = BusinessMapper.getInstance(resource, user);
		business.applyGetRules(filter);
		
		if (business.isSearched()) {
			return business.getObject();
		}
		
		DBObject object = dao.findOne(filter);
		return object;
	}
	
	@Path("/{subResource}")
	public GenericCollectionSubResourceV1 subResource( @PathParam("subResource") 	String subResource
												     , @Context						SecurityContext securityContext
												     ) {
		
		//Consulta o subrecurso aplicando as mesmas regras de consulta do recurso principal, como segurança por usuário por exemplo...
		DBObject object = findObject();
		
		User user = (User) securityContext.getUserPrincipal();
		
		return new GenericCollectionSubResourceV1(resource, object, subResource, user);
		
	}
		
}
