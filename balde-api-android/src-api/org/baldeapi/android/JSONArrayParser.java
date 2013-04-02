package org.baldeapi.android;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONArrayParser implements Parser<JSONArray> {

	@Override
	public JSONArray parse(String json) {
		try {
			return new JSONArray(json);
		} catch (JSONException e) {
			return null;
		}
	}

}
