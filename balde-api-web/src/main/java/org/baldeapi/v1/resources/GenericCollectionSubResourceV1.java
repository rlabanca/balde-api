package org.baldeapi.v1.resources;

import java.net.URI;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class GenericCollectionSubResourceV1 {
	
	private String resource;
	private String subResource;
	private DBObject resourceObject;
	private User user;
	private String resourcePath;
	
	public GenericCollectionSubResourceV1(String resource, DBObject resourceObject, String subResource, User user) {
		super();
		this.resourceObject = resourceObject;
		this.resource = resource;
		this.subResource = subResource;
		this.user = user;
		this.resourcePath = resource + "." + subResource;
	}

	@GET
	@RolesAllowed({"GRANTED"})
	@Produces({MediaType.APPLICATION_JSON})
	public Response get() {
		
		try {
			if (resourceObject == null) {
				
				ErrorsRepresentationV1 errors = ErrorsRepresentationV1.notFound();
				
				return Response.status(Status.NOT_FOUND).entity(errors).build();
				
			}
			
			Object object = resourceObject.get(subResource);
			
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

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@RolesAllowed({"GRANTED"})
	public Response post( @Context 					UriInfo uriInfo
						, @Context					SecurityContext securityContext
						, String entity
						) {
		
//<<<<<<< HEAD
		try {
			DBObject subResourceObject = (DBObject) JSON.parse(entity);
			
			//Adicionando um id no objeto do subrecurso
			ObjectId _id = new ObjectId();
			subResourceObject.put("_id", _id);
			
			if (resourceObject == null) {
				return Response.status(Status.NOT_FOUND).build();
//=======
//		DBObject subResourceObject = (DBObject) JSON.parse(entity);
//		
//		//Adicionando um id no objeto do subrecurso
//		ObjectId _id = new ObjectId();
//		subResourceObject.put("_id", _id);
//		
//		if (resourceObject == null) {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//		
//		BusinessMapper business = BusinessMapper.getInstance(resourcePath, user);
//		
//		Object currentSubResource = resourceObject.get(subResource);
//		if (business.isList() && currentSubResource == null) {
//			currentSubResource = new BasicDBList();
//			resourceObject.put(subResource, currentSubResource);
//		}
//		
//		if (currentSubResource instanceof BasicDBList) {
//			((BasicDBList)currentSubResource).add(subResourceObject);
//		} else {
//			resourceObject.put(subResource, subResourceObject);
//		}
//		
//		GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
//		
//		//Aplica as regras do subrecurso, passando o objeto do recurso principal
//		business.applyPostRules(resourceObject);
//		
//		if (business.hasErrors()) {
//		
//			if (business.isSaveParent()) {
//				dao.insertOrUpdate(resourceObject);
//>>>>>>> 0ddd90a8d00370e69f0d314e0fc9445f87a9be85
			}
			
			resourceObject.put(subResource, subResourceObject);
			
			GenericDAO dao = new GenericDAO(MongoManager.getDatabase(), resource);
			
			//Aplica as regras do subrecurso, passando o objeto do recurso principal
			BusinessMapper business = BusinessMapper.getInstance(resourcePath, user);
			business.applyPostRules(resourceObject);
			
			if (business.hasErrors()) {
			
				if (business.isSaveParent()) {
					dao.insertOrUpdate(resourceObject);
				}
				
				ErrorsRepresentationV1 errors = business.getErrors();
				
				return Response.status(Status.BAD_REQUEST).entity(errors).build();
				
			}
			
			//Salva o objeto do recurso principal...
			dao.insertOrUpdate(resourceObject);
			
			//Construindo a responsta
			String json = JSON.serialize(subResourceObject);
			URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(_id.toString());
			return Response.created(uri).entity(json).build();
		} catch (Exception e) {
			ErrorsRepresentationV1 err = new ErrorsRepresentationV1();
			err.add(new ErrorRepresentationV1(ErrorType.LAST_MINUTE_ERROR, "Um erro ocorreu ao processar a requisição"));
			throw new WebApplicationException(e,Response.status(Status.INTERNAL_SERVER_ERROR).entity(err).build());
		}
//<<<<<<< HEAD
//=======
//		
//		if (!business.isSaved()) {
//			//Salva o objeto do recurso principal...
//			dao.insertOrUpdate(resourceObject);
//		}
//		
//		//Construindo a responsta
//		String json = JSON.serialize(subResourceObject);
//        URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(_id.toString());
//        return Response.created(uri).entity(json).build();
//		
//>>>>>>> 0ddd90a8d00370e69f0d314e0fc9445f87a9be85
	}
	
}
