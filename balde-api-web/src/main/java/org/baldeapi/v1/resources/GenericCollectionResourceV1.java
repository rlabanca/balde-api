package org.baldeapi.v1.resources;

import java.net.URI;
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
import org.baldeapi.v1.resources.util.FilterValidator;
import org.baldeapi.v1.resources.util.FilterValidator.FilterValidation;
import org.baldeapi.v1.resources.util.ProjectionMapper;
import org.baldeapi.v1.security.User;
import org.bson.types.ObjectId;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Path("/{resource}")
public class GenericCollectionResourceV1 {
	
	@GET
	@RolesAllowed({"GRANTED"})
	@Produces({MediaType.APPLICATION_JSON})
	public Response get( @PathParam("resource")		String resource
					   , @QueryParam("filter") 		@DefaultValue("{}") String filters
					   , @QueryParam("sort")		@DefaultValue("{}") String sortFields
					   , @QueryParam("limit")
						 @DefaultValue("20")		Integer limit
					   , @QueryParam("skip")		
						 @DefaultValue("0")			Integer skip
					   , @Context					SecurityContext securityContext
					   , @QueryParam("build") 		@DefaultValue("") String version
					   ) {
		
		try {
			DBObject filter = (DBObject) JSON.parse(filters);
			
			FilterValidation validation = FilterValidator.getInstance(resource).validate(filter);
			
			if (validation.hasErrors()) {
				
				ErrorsRepresentationV1 errors = validation.getErrors();
				
				return Response.status(Status.BAD_REQUEST).entity(errors).build();
				
			}
			
			User user = (User) securityContext.getUserPrincipal();
			
			BusinessMapper business = BusinessMapper.getInstance(resource, user, version);
			business.applyGetRules(filter);
			
			DBObject sort = (DBObject) JSON.parse(sortFields);
			
			business.applySort(sort);
			
			GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
			
			DBObject projection = ProjectionMapper.getInstance(resource).project();
			
			List<DBObject> objects = dao.find(filter, projection, sort, skip, limit);
			
			if (objects == null || objects.size() == 0) {
				
				ErrorsRepresentationV1 errors = ErrorsRepresentationV1.notFound();
				
				return Response.status(Status.NOT_FOUND).entity(errors).build();
			}

			String json = JSON.serialize(objects);
			
			return Response.ok(json).build();
		} catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.LAST_MINUTE_ERROR, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}
		
	}
	
	@Path("/{id}")
	public GenericResourceV1 withId( @PathParam("resource") String resource
								   , @PathParam("id") 		String id
								   , @Context				SecurityContext securityContext
								   ) {
		User user = (User) securityContext.getUserPrincipal();
		return new GenericResourceV1(resource, id, user);
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
	
		try {
			User user = (User) securityContext.getUserPrincipal();
			
			DBObject object = (DBObject) JSON.parse(entity);
			
			BusinessMapper business = BusinessMapper.getInstance(resource, user);
			
			business.applyPostRules(object);
			
			if (business.hasErrors()) {
				
				ErrorsRepresentationV1 errors = business.getErrors();
				
				System.out.println("BAD_REQUEST " + errors.toString());
				
				return Response.status(Status.BAD_REQUEST).entity(errors).build();
				
			}
			
			GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
			
			object = dao.insert(object);
			
			String json = JSON.serialize(object);
			
			ObjectId _id = (ObjectId) object.get("_id");
			
			URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(_id.toString());
			
			return Response.created(uri).entity(json).build();
		}  catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.LAST_MINUTE_ERROR, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}
		

//		if (!business.isSaved()) {
//		
//			GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
//			
//			object = dao.insert(object);
//		
//		}
//		
//		String json = JSON.serialize(object);
//		
//		ObjectId _id = (ObjectId) object.get("_id");
//        
//        URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(_id.toString());
//        
//        return Response.created(uri).entity(json).build();

	}
	
}
