package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditMemberActivity extends Activity implements OnClickListener {
	Button editbutton;
	MemberDAO membercontroller;
	EditText naam, gebdatum, imgurl;
	String adminnaam;
	Spinner selectmember;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_member);
		membercontroller = new MemberDAO(getApplicationContext());
		editbutton = (Button) findViewById(R.id.edit_member_button);
		
		naam = (EditText) findViewById(R.id.input_naam);
		gebdatum = (EditText) findViewById(R.id.input_gebdatum);
		imgurl = (EditText) findViewById(R.id.input_imgurl);
		selectmember = (Spinner) findViewById(R.id.spinner1);
		editbutton.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		adminnaam = b.getString("name");
		String[] spinnerdata = new String[membercontroller.getSize()];
		List<Member> members = membercontroller.getAllMembers();
		for (int i = 0; i < membercontroller.getSize(); i++){
			spinnerdata[i] = members.get(i).getFirstname() + " " + members.get(i).getLastname();
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter spinneradapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, spinnerdata);
		selectmember.setAdapter(spinneradapter);
		selectmember.setSelection(0);
		selectmember.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {

	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1,
	                        int index, long arg3) {
	                	Member m = membercontroller.getMember(index);
	                	naam.setText(m.getFirstname() + " " + m.getLastname());
	                	Calendar bday = m.getBirthday();
	                	gebdatum.setText(bday.get(Calendar.DATE)+"/"+bday.get(Calendar.MONTH)+"/"+bday.get(Calendar.YEAR));
	                	imgurl.setText(m.getImageurl());
	                }

	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) {
	                    // TODO Auto-generated method stub

	                }
	                //add some code here
	            }
	        );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_member, menu);
		return true;
	}

	public void onBackPressed() {
		   Log.d("CDA", "onBackPressed Called");
		   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
		   setIntent.putExtra("id", membercontroller.getCurrentMemberID());
		   setIntent.putExtra("name", adminnaam);
		   startActivity(setIntent);
		   EditMemberActivity.this.finish();
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
				dob.set(Calendar.MONTH, month);
				dob.set(Calendar.YEAR, year);
				Intent i = new Intent(getApplicationContext(), AdminActivity.class);
				String img;
				if (imgurl.getText().toString() == null || imgurl.getText().toString().equals(" ") || imgurl.getText().toString().equals("")){
					img = "nopic";
				}
				else {
					img = imgurl.getText().toString();
				}
				if (membercontroller.existMember(firstname,lastname)){
			    int id = selectmember.getSelectedItemPosition();
				membercontroller.updateMember(new Member(id,firstname, lastname,dob,img,false,Calendar.getInstance()));
				i.putExtra("updatetext", firstname + " Succesvol gewijzigd");
				}
				else{
					i.putExtra("updatetext", "Dit kind werd niet gevonden");
				}
				i.putExtra("id", membercontroller.getCurrentMemberID());
				i.putExtra("name", adminnaam);
				startActivity(i);
				
			}	
			else {
				Toast.makeText(getApplicationContext(), "Gelieve alles goed in te vullen", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	
}
