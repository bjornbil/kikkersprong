package be.khleuven.bjornbillen.kikkersprong.controller.admin;


import java.util.Calendar;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMemberActivity extends Activity implements OnClickListener {
	Button addbutton;
	EditText naam, gebdatum, imgurl;
	String adminnaam;
	MemberDAO membercontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_add_member);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		addbutton = (Button) findViewById(R.id.edit_member_button);
		
		naam = (EditText) findViewById(R.id.input_naam);
		gebdatum = (EditText) findViewById(R.id.input_gebdatum);
		imgurl = (EditText) findViewById(R.id.input_imgurl);
		addbutton.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		adminnaam = b.getString("name");
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_member, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
	   setIntent.putExtra("name", adminnaam);
	   startActivity(setIntent);
	   AddMemberActivity.this.finish();
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.edit_member_button){
			if (naam.getText().toString() != null && naam.getText().toString().contains(" ") && gebdatum != null && gebdatum.getText().toString().contains("/")){
				String firstname, lastname;
				firstname = naam.getText().toString().split(" ")[0];
				lastname = naam.getText().toString().split(" ")[1];
				Calendar dob = Calendar.getInstance();
				Integer day, month, year;
				day = Integer.parseInt(gebdatum.getText().toString().split("/")[0]);
				month = Integer.parseInt(gebdatum.getText().toString().split("/")[1]);
				year = Integer.parseInt(gebdatum.getText().toString().split("/")[2]);
				dob.set(Calendar.DATE, day);
				dob.set(Calendar.MONTH, month-1);
				dob.set(Calendar.YEAR, year);
				Intent i = new Intent(getApplicationContext(), AdminActivity.class);
				String img;
				if (imgurl.getText().toString() == null || imgurl.getText().toString().equals(" ") || imgurl.getText().toString().equals("")){
					img = "nopic";
				}
				else {
					img = imgurl.getText().toString();
				}
				if (!getMemberController().existMember(firstname,lastname)){
					
					getMemberController().addMember(new Member(getMemberController().getSize(),firstname, lastname,dob,img,false,Calendar.getInstance()));
				i.putExtra("updatetext", firstname + " Succesvol toegevoegd");
				getMemberController().update();
				}
				else{
					i.putExtra("updatetext", "Dit kind staat al in de database");
				}
				i.putExtra("name", adminnaam);
				startActivity(i);
				
			}	
			else {
				Toast.makeText(getApplicationContext(), "Gelieve alles goed in te vullen", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	
}
