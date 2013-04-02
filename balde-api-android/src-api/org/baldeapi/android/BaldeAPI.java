package org.baldeapi.android;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class BaldeAPI {

	private String base;
	private String bucket;
	private boolean array;
	private HttpRequestBase method;
	private String apiKey;
	private JSONObject filter;
	private JSONObject sort;
	private Integer skip;
	private Integer limit;
	private ResponseListener success;
	private ResponseListener error;
	private String id;
	private JSONObject object;
	
	public static BaldeAPI get(String base, String bucket) {
		
		BaldeAPI api = new BaldeAPI();
		api.base = base;
		api.bucket = bucket;
		api.array = true;
		
		api.method = new HttpGet();
		
		return api;
		
	}
	
	public static BaldeAPI get(String base, String bucket, String id) {
		
		BaldeAPI api = new BaldeAPI();
		api.base = base;
		api.bucket = bucket;
		api.id = id;
		
		api.method = new HttpGet();
		
		return api;
		
	}
	
	public static BaldeAPI post(String base, String bucket, JSONObject object) {
		
		BaldeAPI api = new BaldeAPI();
		api.base = base;
		api.bucket = bucket;
		api.object = object;
		
		HttpPost post = new HttpPost();
		try {
			post.setEntity(new StringEntity(object.toString()));
		} catch (UnsupportedEncodingException e) {
			Log.e("POST", e.getMessage(), e);
		}
		
		api.method = post;
		
		return api;
		
	}
	
	public static BaldeAPI put(String base, String bucket, String id, JSONObject object) {
		
		BaldeAPI api = new BaldeAPI();
		api.base = base;
		api.bucket = bucket;
		api.object = object;
		api.id = id;
		
		HttpPut put = new HttpPut();
		try {
			put.setEntity(new StringEntity(object.toString()));
		} catch (UnsupportedEncodingException e) {
			Log.e("POST", e.getMessage(), e);
		}
		
		api.method = put;
		
		return api;
		
	}
	
	public static BaldeAPI delete(String base, String bucket, String id) {
		
		BaldeAPI api = new BaldeAPI();
		api.base = base;
		api.bucket = bucket;
		api.id = id;
		
		api.method = new HttpDelete();
		
		return api;
		
	}
	
	private class BaldeAPITask<T> extends AsyncTask<Void, Void, T> {

		private int statusCode;
		private boolean success;
		private BaldeAPI api;
		private Parser<T> parser;
		
		public BaldeAPITask(BaldeAPI api, Parser<T> parser) {
			this.api = api;
			this.parser = parser;
		}
		
		@Override
		protected T doInBackground(Void... params) {
			
			try {
				
				HttpClient client = new DefaultHttpClient();
					
				api.method.setHeader("Accept", "application/json");
				api.method.setHeader("Content-Type", "application/json");
				api.method.setHeader("X-API-Key", api.apiKey);
				
				api.method.setURI(new URI(api.buildUri()));
				
				HttpResponse response = client.execute(method);
				statusCode = response.getStatusLine().getStatusCode();
				
				switch (statusCode) {
					case 200:
					case 201:
						T object = parser.parse(EntityUtils.toString(response.getEntity()));
						success = true;
						return object;
					default:
						break;
				}
				
			} catch (Exception e) {
				Log.e("API", e.getMessage(), e);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(T result) {
			
			if (success) {
				
				api.success.onEvent(array, result, statusCode, success);
				
			} else {
				
				api.error.onEvent(array, result, statusCode, success);
				
			}
			
		}
		
	}
	
	public BaldeAPI apiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}
	
	public String buildUri() {
		StringBuilder builder = new StringBuilder();
		builder.append(base);
		builder.append("/");
		builder.append(bucket);
		
		if (id != null) {
			builder.append("/");
			builder.append(id);
		}
		
		if (filter != null || sort != null || skip != null || limit != null) {
			
			builder.append("?");
			
			if (filter != null) {
				builder.append("filter");
				builder.append("=");
				builder.append(filter.toString());
			}
			
			if (sort != null) {
				builder.append("sort");
				builder.append("=");
				builder.append(sort.toString());
			}
			
			if (skip != null) {
				builder.append("skip");
				builder.append("=");
				builder.append(skip);
			}
			
			if (limit != null) {
				builder.append("limit");
				builder.append("=");
				builder.append(limit);
			}
		}
		
		return builder.toString();
	}

	public BaldeAPI filter(JSONObject filter) {
		this.filter = filter;
		return this;
	}
	
	public BaldeAPI sort(JSONObject sort) {
		this.sort = sort;
		return this;
	}
	
	public BaldeAPI skip(Integer skip) {
		this.skip = skip;
		return this;
	}
	
	public BaldeAPI limit(Integer limit) {
		this.limit = limit;
		return this;
	}
	
	public BaldeAPI onSuccess(ResponseListener success) {
		this.success = success;
		return this;
	}
	
	public BaldeAPI onError(ResponseListener error) {
		this.error = error;
		return this;
	}
	
	public void execute() {
		BaldeAPITask<?> task;
		if (array) {
			task = new BaldeAPITask<JSONArray>(this, new JSONArrayParser()); 
		} else {
			task = new BaldeAPITask<JSONObject>(this, new JSONObjectParser());
		}
		task.execute();
	}
	
}
