   package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MemberActivity extends Activity implements OnClickListener {
	MemberDAO membercontroller;
	TextView txtnaam, geboortedatum,checktext,title;
	LinearLayout lin;
	ImageView foto;
	ImageButton checkin, attendances, bills;
	Bitmap fotobitmap;
	int id;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		membercontroller = new MemberDAO(getApplicationContext());
		txtnaam = (TextView) findViewById(R.id.name);
		geboortedatum = (TextView) findViewById(R.id.gebdatum);
		foto = (ImageView) findViewById(R.id.qrexample);
		title = (TextView) findViewById(R.id.main_title);
		
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			lin = (LinearLayout) findViewById(R.id.clockspace2);
			TextClock textclock;
			textclock = new TextClock(getApplicationContext());
			textclock.setTimeZone("GMT+0200");
			textclock.setFormat12Hour("MMM dd, yyyy k:mm:ss");
			textclock.setFormat24Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
			
		}
		checkin = (ImageButton) findViewById(R.id.checkinbutton);
		bills = (ImageButton) findViewById(R.id.billsbutton);
		attendances = (ImageButton) findViewById(R.id.attendancebutton);
		checkin.setOnClickListener(this);
		bills.setOnClickListener(this);
		attendances.setOnClickListener(this);
		checktext = (TextView) findViewById(R.id.checktext);
		Bundle bundle = getIntent().getExtras();
		
		id = bundle.getInt("id");
		Log.d("debug","id = " + id);
		membercontroller.setCurrentMemberID(id);
		Log.d("debug","membercontroller id = " + membercontroller.getCurrentMemberID());
				String naam = bundle.getString("name");

		Member m = membercontroller.getMember(id);
		String dbnaam = m.getFirstname() + " " + m.getLastname();
		if (naam.equals(dbnaam)){
		txtnaam.setText(naam);
		geboortedatum.setText(m.getBirthdayString());
		if (m.getImageurl() != null && m.getImageurl().contains("http")){
		new DownloadImageTask(foto).execute(m.getImageurl());
		}
		else {
			foto.setImageResource(R.drawable.ic_nopic);
		}
		updateCheckin();
		}
		
	}
	
	
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),MainActivity.class);
	   startActivity(setIntent);
	   MemberActivity.this.finish();
	}
	
	public void updateCheckin(){
		
		Log.d("id",id + "");
		if(membercontroller.getMember(id).isPresent()){
			checkin.setImageResource(R.drawable.ic_checkout);
			checktext.setText("Uitchecken");
		}
		else{
			checkin.setImageResource(R.drawable.ic_checkin);
			checktext.setText("Inchecken");
			}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);

		return true;
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkinbutton:
			Log.d("debug","id = " + id);
			Member m = membercontroller.getMember(id);
			if (m.isPresent()){
				m.setPresent(false);
			}
			else {
				m.setPresent(true);
			}
			membercontroller.updateMember(m);
			
			Intent i = new Intent(getApplicationContext(),
					CheckinActivity.class);
			i.putExtra("id", id);
			this.startActivity(i);
			MemberActivity.this.finish();
			break;
		case R.id.attendancebutton:
			Intent i2 = new Intent(getApplicationContext(),
					AttendanceActivity.class);
			i2.putExtra("id", id);
			this.startActivity(i2);
			MemberActivity.this.finish();
			break;
		case R.id.billsbutton:
			Intent i3 = new Intent(getApplicationContext(), BillsActivity.class);
			i3.putExtra("id", membercontroller.getCurrentMemberID());
			this.startActivity(i3);
			MemberActivity.this.finish();
			break;
		}

	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}
}

