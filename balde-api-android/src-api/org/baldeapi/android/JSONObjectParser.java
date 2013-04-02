package org.baldeapi.android;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectParser implements Parser<JSONObject> {

	@Override
	public JSONObject parse(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			return null;
		}
	}

}
