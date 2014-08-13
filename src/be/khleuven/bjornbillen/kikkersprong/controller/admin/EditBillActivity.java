package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillsToPDF;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
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

public class EditBillActivity extends Activity implements OnClickListener {
	Button editbutton, deletebutton, exportbutton, setbetaaldbutton;
	EditText amount;
	String adminnaam;
	Spinner selectbill;
	BillDAO billcontroller;
	MemberDAO membercontroller;
	AttendanceDAO attendancecontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_edit_bill);
		editbutton = (Button) findViewById(R.id.button1);
		billcontroller = BillDAO.getInstance(getApplicationContext());
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		attendancecontroller = AttendanceDAO.getInstance(getApplicationContext());
		amount = (EditText) findViewById(R.id.amounttext);
		selectbill = (Spinner) findViewById(R.id.spinner1);
		deletebutton = (Button) findViewById(R.id.deletebutton);
		setbetaaldbutton = (Button) findViewById(R.id.setbetaald);
		exportbutton = (Button) findViewById(R.id.exportbutton);
		setbetaaldbutton.setOnClickListener(this);
		exportbutton.setOnClickListener(this);
		
		deletebutton.setOnClickListener(this);
		editbutton.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		adminnaam = b.getString("name");
		String[] spinnerdata = new String[getBillController().getSize()];
		List<Bill> bills = getBillController().getAllBills();
		for (int i = 0; i < getBillController().getSize(); i++){
			spinnerdata[i] = bills.get(i).getPaybeforeString() + " " + bills.get(i).getMember().getFirstname() + " " + bills.get(i).getMember().getLastname();
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter spinneradapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, spinnerdata);
		selectbill.setAdapter(spinneradapter);
		
		selectbill.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {
	            	
	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1,
	                        int index, long arg3) {
	                	if(index > 0){
	                		amount.setText(""+getBillController().getBill(index).getAmount());
	                	}
	                	    	
	                }

	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) {
	                    // TODO Auto-generated method stub

	                }
	                //add some code here
	            }
	        );
		selectbill.setSelection(0);
	}

	public BillDAO getBillController(){
		return billcontroller;
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public AttendanceDAO getAttendanceController(){
		return attendancecontroller;
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
		   EditBillActivity.this.finish();
		}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.deletebutton){
			final int id = selectbill.getSelectedItemPosition();
			final String bill = getBillController().getBill(id).getMember().getFirstname();
			 new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Rekening verwijderen")
		        .setMessage("Ben je zeker dat je deze rekening van " + bill + " wil verwijderen?")
		        .setPositiveButton("Ja", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	getBillController().deleteBill(id);
		        	getMemberController().update();
		        	Intent i = new Intent(getApplicationContext(),AdminActivity.class);
		        	i.putExtra("name", adminnaam);
		        	i.putExtra("updatetext","Rekening succesvol verwijderd");
					startActivity(i);
					EditBillActivity.this.finish();
		        }

		    })
		    .setNegativeButton("Annuleren", null)
		    .show();
		
		}
		else if (v.getId() == R.id.exportbutton){
			int id = selectbill.getSelectedItemPosition();
			Bill b = getBillController().getBill(id);
			Member m = b.getMember();
			List<Attendance> attendances = getAttendanceController().getAttendances(id);
			BillsToPDF billsTopdf = new BillsToPDF();
			String result = billsTopdf.createPDF(attendances, m, b, 3.5);
				Toast.makeText(getApplicationContext(),result + " opgeslagen!", Toast.LENGTH_LONG).show();
			
		}
		else if (v.getId() == R.id.setbetaald){
			int id = selectbill.getSelectedItemPosition();
			Intent i = new Intent(getApplicationContext(),AdminActivity.class);
			Bill b = getBillController().getBill(id);
			if (!b.isPaid()){
			b.setPaid(true);
			getBillController().updateBill(b);
			getMemberController().update();
			i.putExtra("updatetext","Rekening is nu betaald");
			i.putExtra("name", adminnaam);
			startActivity(i);
			EditBillActivity.this.finish();
			}
			else {
				Toast.makeText(getApplicationContext(), "Rekening is al betaald", Toast.LENGTH_LONG).show();
			}
        	
			
		}
		else if (v.getId() == R.id.button1){
			int id = selectbill.getSelectedItemPosition();
			Bill b = getBillController().getBill(id);
			b.setAmount(Double.parseDouble(amount.getText().toString()));
			getBillController().updateBill(b);
			getMemberController().update();
			Intent i = new Intent(getApplicationContext(),AdminActivity.class);
        	i.putExtra("name", adminnaam);
        	i.putExtra("updatetext","Rekening is gewijzigd");
			startActivity(i);
			EditBillActivity.this.finish();
		}
		
		
	}

	
}
