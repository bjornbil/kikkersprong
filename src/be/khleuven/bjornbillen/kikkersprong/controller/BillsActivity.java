package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.ArrayList;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.controller.listadapter.CostumBillListAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import com.example.kikkersprong.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BillsActivity extends Activity {
	ListView listView;
	TextView membername, betaald;
	CostumBillListAdapter listadapter;
	List<Bill> bills;
	BillDAO billcontroller;
	MemberDAO membercontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		billcontroller = BillDAO.getInstance(getApplicationContext());
		membername = (TextView) findViewById(R.id.billname);
		betaald = (TextView) findViewById(R.id.betaald);
		listView = (ListView) findViewById(R.id.listViewBills);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		getMemberController().setCurrentMemberID(id);
		bills = getBillController().getBills(id);
		membername.setText(getMemberController().getMember(id).getFirstname() + " " + getMemberController().getMember(id).getLastname());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

							// Show Alert
				String datum = listView.getAdapter().getItem(position).toString().split(" ")[0];
				String price = listView.getAdapter().getItem(position).toString().split(" ")[1];
			    
		        if (price.split(".").length > 1 && Integer.parseInt(price.split(".")[1]) == 0){
		        	price = price.split(".")[0];
		        }
		        
				Toast.makeText(getApplicationContext(),
						"Datum : " + datum + " (" + price + ")",
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
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public BillDAO getBillController(){
		return billcontroller;
	}
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),MemberActivity.class);
	   setIntent.putExtra("id", getMemberController().getCurrentMemberID());
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
