package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.InputStream;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MemberActivity extends Activity implements OnClickListener {
	
	TextView txtnaam, geboortedatum, checktext, title;
	LinearLayout lin;
	ImageView foto;
	ImageButton checkin, attendances, bills;
	Bitmap fotobitmap;
	MemberDAO membercontroller;
	int id;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_member);
		txtnaam = (TextView) findViewById(R.id.name);
		geboortedatum = (TextView) findViewById(R.id.gebdatum);
		foto = (ImageView) findViewById(R.id.qrexample);
		title = (TextView) findViewById(R.id.main_title);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
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
		String naam = null;
		int id = bundle.getInt("id");
		getMemberController().setCurrentMemberID(id);
		Member m = getMemberController().getMember(id);
		String dbnaam = m.getFirstname() + " " + m.getLastname();
		if (bundle.getString("name") != null){
		naam = bundle.getString("name");
		}
		else {
			naam = dbnaam;
		}
		boolean magdoor;
		if (naam.equals(dbnaam)) {
			txtnaam.setText(naam);
			geboortedatum.setText(m.getBirthdayString());
			if (m.getImageurl() != null && m.getImageurl().contains("http")) {
				new DownloadImageTask(foto).execute(m.getImageurl());
			} else {
				foto.setImageResource(R.drawable.ic_nopic);
			}
			magdoor = true;
		}
		else {
			magdoor = false;
		}
		updateCheckin();
		if (!magdoor){
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(i);
			MemberActivity.this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");
		Intent setIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		setIntent.putExtra("db","nietladen");
		startActivity(setIntent);
		MemberActivity.this.finish();
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
				
	}
	
	

	public void updateCheckin() {

		int id = getMemberController().getCurrentMemberID();
		if (getMemberController().getMember(id).isPresent()) {
			checkin.setImageResource(R.drawable.ic_checkout);
			checktext.setText("Uitchecken");
		} else {
			checkin.setImageResource(R.drawable.ic_checkin);
			checktext.setText("Inchecken");
		}
		checkin.bringToFront();

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
			int id = getMemberController().getCurrentMemberID();
			
			Member m = getMemberController().getMember(id);
			if (m.isPresent()) {
				m.setPresent(false);
			} else {
				m.setPresent(true);
			}
			getMemberController().updateMember(m);

			Intent i = new Intent(getApplicationContext(),
					CheckinActivity.class);
			i.putExtra("id", m.getId());
			this.startActivity(i);
			MemberActivity.this.finish();
			break;
		case R.id.attendancebutton:
			Intent i2 = new Intent(getApplicationContext(),
					AttendanceActivity.class);
			i2.putExtra("id", getMemberController().getCurrentMemberID());
			this.startActivity(i2);
			MemberActivity.this.finish();
			break;
		case R.id.billsbutton:
			Intent i3 = new Intent(getApplicationContext(), BillsActivity.class);
			i3.putExtra("id", getMemberController().getCurrentMemberID());
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
