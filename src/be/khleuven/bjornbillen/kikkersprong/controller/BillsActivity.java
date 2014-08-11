package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
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
	CostumBillListAdapter listadapter;
	List<Bill> bills;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);
		billcontroller = new BillDAO(getApplicationContext());
		membercontroller = new MemberDAO(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		membercontroller.setCurrentMemberID(id);
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
				double prijsdouble = Double.parseDouble(prijs);
				
				if (prijsdouble % 2 == 0){
					int prijsrond = (int) Math.round(prijsdouble);
					prijs = "€" + prijsrond ;
				}
				else {
					prijs = "€" + prijs + "0";
				}
				Toast.makeText(getApplicationContext(),
						"Datum : " + datum + " (" + prijs + ")",
						Toast.LENGTH_LONG).show();

			}
		});
		List<String> valuelist = new ArrayList<String>();
		for (Bill b : bills){
			valuelist.add(b.toString());
		}
		listadapter = new CostumBillListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),MemberActivity.class);
	   setIntent.putExtra("id", membercontroller.getCurrentMemberID());
	   Member m = membercontroller.getMember(membercontroller.getCurrentMemberID());
	   setIntent.putExtra("name", m.getFirstname() + " " + m.getLastname());
	   startActivity(setIntent);
	   BillsActivity.this.finish();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bills, menu);
		return true;
	}
}
