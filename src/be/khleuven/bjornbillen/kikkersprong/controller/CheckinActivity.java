package be.khleuven.bjornbillen.kikkersprong.controller;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckinActivity extends Activity {
	TextView checkstatus;
	MemberDAO membercontroller;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		checkstatus = (TextView) findViewById(R.id.checkstatus);
		membercontroller = new MemberDAO(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		Member m = membercontroller.getMember(id);
		if (m.isPresent() == true){
		checkstatus.setText("Ingecheckt");
		checkstatus.setTextColor(R.color.slogancolor);
		}
		else{
		checkstatus.setText("Uitgecheckt");
		checkstatus.setTextColor(R.color.bgcolor);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkin, menu);
		checkstatus = (TextView) findViewById(R.id.checkstatus);
		membercontroller = new MemberDAO(getApplicationContext());
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
