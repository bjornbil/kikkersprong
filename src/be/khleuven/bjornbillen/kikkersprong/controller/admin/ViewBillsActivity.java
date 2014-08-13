package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.controller.listadapter.CostumBillsAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;

import com.example.kikkersprong.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewBillsActivity extends Activity {
	
	ListView listView;
	RelativeLayout layout;
	CostumBillsAdapter listadapter;
	List<Bill> bills;
	TextView adminnaam;
	BillDAO billcontroller;
	MemberDAO membercontroller;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_bills);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		billcontroller = BillDAO.getInstance(getApplicationContext());
		XMLDatabase xml = new XMLDatabase(getApplicationContext());
		try {
			xml.loadFromXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView = (ListView) findViewById(R.id.listViewBills);
		layout = (RelativeLayout) findViewById(R.id.rekeningenlayout);
		layout.setBackgroundResource(R.color.infcolor);
		adminnaam = (TextView) findViewById(R.id.billname);
		bills = getBillController().getAllBills();
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
			    
		        if (prijs.split(".").length > 1 && Integer.parseInt(prijs.split(".")[1]) == 0){
		        	prijs = prijs.split(".")[0];
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
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	
	public BillDAO getBillController(){
		return billcontroller;
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
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
