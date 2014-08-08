package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.util.ArrayList;
import java.util.List;


import be.khleuven.bjornbillen.kikkersprong.controller.CostumMemberListAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class ViewMembersActivity extends Activity {
	CostumMemberListAdapter listadapter;
	MemberDAO membercontroller;
	List<Member> members;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_members);
		membercontroller = new MemberDAO(getApplicationContext());
		listView = (ListView) findViewById(R.id.listViewMembers);
		members = membercontroller.getAllMembers();
		List<String> valuelist = new ArrayList<String>();
		for (Member m : members){
			valuelist.add(m.toString());
		}
		listadapter = new CostumMemberListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_members, menu);
		return true;
	}

}
