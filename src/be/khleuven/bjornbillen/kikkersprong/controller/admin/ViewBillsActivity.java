package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.util.ArrayList;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.controller.CostumBillListAdapter;
import be.khleuven.bjornbillen.kikkersprong.controller.CostumBillsAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;

import com.example.kikkersprong.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewBillsActivity extends Activity {
	BillDAO billcontroller;
	MemberDAO membercontroller;
	ListView listView;
	RelativeLayout layout;
	CostumBillsAdapter listadapter;
	List<Bill> bills;
	TextView adminnaam;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);
		billcontroller = new BillDAO(getApplicationContext());
		membercontroller = new MemberDAO(getApplicationContext());
		listView = (ListView) findViewById(R.id.listViewBills);
		layout = (RelativeLayout) findViewById(R.id.rekeningenlayout);
		layout.setBackgroundResource(R.color.infcolor);
		adminnaam = (TextView) findViewById(R.id.billname);
		bills = billcontroller.getAllBills();
		List<String> valuelist = new ArrayList<String>();
		for (Bill b : bills){
			valuelist.add(b.toString());
		}
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
		listadapter = new CostumBillsAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		Bundle b = getIntent().getExtras();
		adminnaam.setText(b.getString("name"));
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
	   setIntent.putExtra("id",membercontroller.getCurrentMemberID());
	   setIntent.putExtra("name", adminnaam.getText().toString());
	   startActivity(setIntent);
	   ViewBillsActivity.this.finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);

		return true;
	}
	
}
