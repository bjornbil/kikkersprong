package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditMemberActivity extends Activity implements OnClickListener {
	Button editbutton, deletebutton;
	EditText naam, gebdatum, imgurl;
	String adminnaam;
	Spinner selectmember;
	MemberDAO membercontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_edit_member);
		editbutton = (Button) findViewById(R.id.edit_member_button);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		naam = (EditText) findViewById(R.id.input_naam);
		gebdatum = (EditText) findViewById(R.id.input_gebdatum);
		imgurl = (EditText) findViewById(R.id.input_imgurl);
		selectmember = (Spinner) findViewById(R.id.spinner1);
		deletebutton = (Button) findViewById(R.id.button1);
		deletebutton.setOnClickListener(this);
		editbutton.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		adminnaam = b.getString("name");
		String[] spinnerdata = new String[getMemberController().getSize()];
		List<Member> members = getMemberController().getAllMembers();
		for (int i = 0; i < getMemberController().getSize(); i++){
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
	                	Member m = getMemberController().getMember(index);
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

	public MemberDAO getMemberController(){
		return membercontroller;
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
				if (getMemberController().existMember(firstname,lastname)){
					int id = selectmember.getSelectedItemPosition();
			    getMemberController().updateMember(new Member(id,firstname, lastname,dob,img,false,Calendar.getInstance()));
				i.putExtra("updatetext", firstname + " Succesvol gewijzigd");
				getMemberController().update();
				}
				else{
					i.putExtra("updatetext", "Dit kind werd niet gevonden");
				}
				i.putExtra("name", adminnaam);
				startActivity(i);
				EditMemberActivity.this.finish();
			}	
			else {
				Toast.makeText(getApplicationContext(), "Gelieve alles goed in te vullen", Toast.LENGTH_LONG).show();
			}
		}
		else if (v.getId() == R.id.button1){
			final int id = selectmember.getSelectedItemPosition();
			final String kind = getMemberController().getMember(id).getFirstname();
			 new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Kind verwijderen")
		        .setMessage("Ben je zeker dat je " + kind + " wil verwijderen?")
		        .setPositiveButton("Ja", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	getMemberController().deleteMember(id);
		        	getMemberController().update();
		        	Intent i = new Intent(getApplicationContext(),AdminActivity.class);
		        	i.putExtra("name", adminnaam);
					startActivity(i);
					EditMemberActivity.this.finish();
		        }

		    })
		    .setNegativeButton("Annuleren", null)
		    .show();
		
		}
		
	}

	
}
