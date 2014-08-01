package be.khleuven.bjornbillen.kikkersprong.controller;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MemberActivity extends Activity {
	MemberDAO membercontroller;
	TextView txtnaam, geboortedatum;
	LinearLayout lin;
	ImageView foto;
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		membercontroller = new MemberDAO(getApplicationContext());
		txtnaam = (TextView) findViewById(R.id.naam);
		geboortedatum = (TextView) findViewById(R.id.gebdatum);
		foto = (ImageView) findViewById(R.id.imageView2);
		if (android.os.Build.VERSION.SDK_INT >= 17){
			lin = (LinearLayout) findViewById(R.id.clockspace2);
			TextClock textclock;
			textclock = new TextClock(getApplicationContext());
			textclock.setTimeZone("GMT+0200");
			textclock.setFormat12Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
		}
		
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		String naam = bundle.getString("name");
		//Member m = membercontroller.getMember(id);
		//String dbnaam = m.getFirstname() + " " + m.getLastname();
		//if (naam.equals(dbnaam)){
			txtnaam.setText(naam);
		//	geboortedatum.setText(m.getBirthdayString());
		//	foto.setImageResource(R.drawable.ic_nopic);
		//}
		//else {
		//	Intent i = new Intent(getApplicationContext(),MainActivity.class);
		//	i.putExtra("ERROR", "Er is geen persoon gevonden");
		//}
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);

		return true;
	}

}
