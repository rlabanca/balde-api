package org.baldeapi.v1.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.baldeapi.v1.persistence.GenericDAO;
import org.baldeapi.v1.persistence.MongoManager;
import org.baldeapi.v1.representation.ErrorsRepresentationV1;
import org.baldeapi.v1.resources.business.BusinessMapper;
import org.baldeapi.v1.resources.util.FilterValidator;
import org.baldeapi.v1.resources.util.FilterValidator.FilterValidation;
import org.baldeapi.v1.resources.util.ProjectionMapper;
import org.baldeapi.v1.security.User;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Path("/{resource}")
public class GenericCollectionResourceV1 {
	
	@GET
	@RolesAllowed({"GRANTED"})
	@Produces({MediaType.APPLICATION_JSON})
	public Response get( @PathParam("resource")		String resource
					   , @QueryParam("filter") 		String filters
					   , @QueryParam("sort")		String sortFields
					   , @QueryParam("limit")
						 @DefaultValue("10")		Integer limit
					   , @QueryParam("skip")		
						 @DefaultValue("0")			Integer skip
					   ) {
		
		DBObject filter = (DBObject) JSON.parse(filters);
		
		FilterValidation validation = FilterValidator.getInstance(resource).validate(filter);
		
		if (validation.hasErrors()) {
			
			ErrorsRepresentationV1 errors = validation.getErrors();
			
			return Response.status(Status.BAD_REQUEST).entity(errors).build();
			
		}
		
		DBObject sort = (DBObject) JSON.parse(sortFields);
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		DBObject projection = ProjectionMapper.getInstance(resource).project();
		
		List<DBObject> objects = dao.find(filter, projection, sort, skip, limit);
		
		if (objects == null || objects.size() == 0) {
			
			ErrorsRepresentationV1 errors = ErrorsRepresentationV1.notFound();
			
			return Response.status(Status.NOT_FOUND).entity(errors).build();
		}

		String json = JSON.serialize(objects);
		
		return Response.ok(json).build();
		
	}
	
	@Path("/{id}")
	public GenericResourceV1 withId( @PathParam("resource") String resource
								   , @PathParam("id") 		String id
								   ) {
		return new GenericResourceV1(resource, id);
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@RolesAllowed({"GRANTED"})
	public Response post( @PathParam("resource")	String	resource
						, @Context 					UriInfo uriInfo
						, @Context					SecurityContext securityContext
						, String entity
						) {
	
		User user = (User) securityContext.getUserPrincipal();
		
		DBObject object = (DBObject) JSON.parse(entity);
		
		BusinessMapper business = BusinessMapper.getInstance(resource, user);
		
		business.applyRules(object);
		
		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
		
		object = dao.insert(object);
		
		String json = JSON.serialize(object);
		
		return Response.ok(json).build();
		
	}
	
}
