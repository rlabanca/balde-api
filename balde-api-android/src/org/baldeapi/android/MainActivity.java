package org.baldeapi.android;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String HOST = "http://192.168.25.9:8082";
	private static final String BUCKET = "clipboard";
	private static final String API_KEY = "abc123";
	
	private TextView text;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        
        
        ClipboardManager.
        
        text = (TextView) findViewById(R.id.text);
        
        Button post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				post("button clicked");
			}
		});
        
        Button get = (Button) findViewById(R.id.get);
        get.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				ResponseListener listener = new ResponseListener() {
					@Override
					public void onEvent(boolean array, Object object, int statusCode,
							boolean success) {
						if (success) {
							try {
								JSONArray jsonArray = (JSONArray) object;
								JSONObject jsonObject = (JSONObject) jsonArray.get(0);
								text.setText(jsonObject.getString("version"));
							} catch (Exception e) {
								Log.e("GET", e.getMessage(), e);
							}
						}
					}
				};
				
				try {
				
					JSONObject sort = new JSONObject();
					sort.put("timestamp", -1);
					
					BaldeAPI.get(HOST, BUCKET)
							.onError(listener)
							.onSuccess(listener)
							.apiKey(API_KEY)
							.sort(sort)
							.limit(1)
							.execute();
				
				} catch (Exception e) {
					
				}
			}
		});
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	private void post(String text) {
		ResponseListener listener = new ResponseListener() {
			@Override
			public void onEvent(boolean array, Object object, int statusCode,
					boolean success) {
				Log.d("EVENT", object.toString());
			}
		};
		
		JSONObject object = new JSONObject();
		try {
			object.put("text", text);
			object.put("timestamp", System.currentTimeMillis());
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage(), e);
		}
		
		BaldeAPI.post(HOST, BUCKET, object)
				.onError(listener)
				.onSuccess(listener)
				.apiKey(API_KEY)
				.execute();
	}

    
}
