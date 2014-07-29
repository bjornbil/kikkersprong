package be.khleuven.bjornbillen.kikkersprong.controller;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InfoActivity extends Activity {

	Button startbutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		startbutton = (Button) findViewById(R.id.startbutton);
		launch();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	
	public void launch(){
		
		startbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),MainActivity.class);	
				startActivity(i);
			}
		});
	}

}
