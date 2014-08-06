package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BillsActivity extends Activity {
	ListView listView;
	TextView membername, betaald;
	BillDAO billcontroller;
	MemberDAO membercontroller;
	CustomBillListAdapter listadapter;
	List<Bill> bills;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);
		billcontroller = new BillDAO(getApplicationContext());
		membercontroller = new MemberDAO(getApplicationContext());
		int id = membercontroller.getCurrentMemberID();
		membername = (TextView) findViewById(R.id.billname);
		betaald = (TextView) findViewById(R.id.betaald);
		listView = (ListView) findViewById(R.id.listViewBills);
		
		bills = billcontroller.getBills(id);
		membername.setText(membercontroller.getMember(id).getFirstname() + " " + membercontroller.getMember(id).getLastname());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

							// Show Alert
				String datum = listView.getAdapter().getItem(position).toString().split(" ")[0];
				String prijs = listView.getAdapter().getItem(position).toString().split(" ")[1];
								
				Toast.makeText(getApplicationContext(),
						"Datum : " + datum + " (" + prijs + ")",
						Toast.LENGTH_LONG).show();

			}
		});
		List<String> valuelist = new ArrayList<String>();
		for (Bill b : bills){
			valuelist.add(b.toString());
		}
		listadapter = new CustomBillListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bills, menu);
		return true;
	}
}
