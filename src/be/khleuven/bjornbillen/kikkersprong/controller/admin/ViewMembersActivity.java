package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import be.khleuven.bjornbillen.kikkersprong.controller.CostumMemberListAdapter;
import be.khleuven.bjornbillen.kikkersprong.controller.MainActivity;
import be.khleuven.bjornbillen.kikkersprong.controller.MemberActivity;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class ViewMembersActivity extends Activity {
	CostumMemberListAdapter listadapter;
	
	List<Member> members;
	ListView listView;
	String adminnaam;
	TextView admintextview;
	MemberDAO membercontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_members);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		XMLDatabase xml = new XMLDatabase(getApplicationContext());
		try {
			xml.loadFromXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView = (ListView) findViewById(R.id.listViewMembers);
		members = getMemberController().getAllMembers();
		
		List<String> valuelist = new ArrayList<String>();
		for (Member m : members){
			valuelist.add(m.toString());
		}
		listadapter = new CostumMemberListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		Bundle b = getIntent().getExtras();
		adminnaam = b.getString("name");
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
	   setIntent.putExtra("name", adminnaam);
	   startActivity(setIntent);
	   ViewMembersActivity.this.finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_members, menu);
		return true;
	}

}
