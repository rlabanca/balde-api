package org.baldeapi.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button post = (Button) findViewById(R.id.post);
        post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ResponseListener listener = new ResponseListener() {
					@Override
					public void onEvent(boolean array, Object object, int statusCode,
							boolean success) {
						Log.d("EVENT", object.toString());
					}
				};
				
				JSONObject object = new JSONObject();
				try {
					object.put("version", "2.2");
				} catch (JSONException e) {
					Log.e("JSON", e.getMessage(), e);
				}
				
				BaldeAPI.post("http://192.168.25.9:8082", "android", object)
						.onError(listener)
						.onSuccess(listener)
						.apiKey("abc123")
						.execute();
				
			}
		});
        
        Button get = (Button) findViewById(R.id.get);
        get.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
