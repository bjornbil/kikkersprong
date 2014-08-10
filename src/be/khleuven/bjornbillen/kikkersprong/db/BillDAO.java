package be.khleuven.bjornbillen.kikkersprong.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;

import com.hmkcode.android.sqlite.MySQLiteHelper;

public class BillDAO  {
	private static final String TABLE_BILLS = "bills";

	// Table presencies Column names
	private static final String BILL_ID = "id";
	private static final String BILL_AMOUNT = "amount";
	private static final String BILL_MEMBERID = "memberid";
	private static final String BILL_PAYDATE = "paydate";
	private static final String BILL_PAID = "ispaid";
	private MySQLiteHelper db;
	private double priceperhour = 10.0;

	private static final String[] BILL_COLUMNS = { BILL_ID, BILL_AMOUNT, BILL_MEMBERID,
			BILL_PAYDATE, BILL_PAID };

	public BillDAO(Context context) {
		db = new MySQLiteHelper(context);
	}
	
	public int getSize(){
		return getAllBills().size();
	}
	
	public void setPricePerHour(double price){
		if (price > 0)
		this.priceperhour = price;
	}
	
	public double getPricePerHour(){
		return priceperhour;
	}

	public void addBill(Bill bill) {
		if (bill == null) {
		} // exception handling
		ContentValues values = new ContentValues();
		values.put(BILL_ID, bill.getId());
		values.put(BILL_AMOUNT, bill.getAmount());
		values.put(BILL_MEMBERID, bill.getMember().getId());
		values.put(BILL_PAYDATE, bill.getPaybeforeString());
		values.put(BILL_PAID, bill.isPaid());
		db.addObject(TABLE_BILLS, values);
		
	}
	
	public boolean hasBill(Bill b){
		boolean result = false;
		for (Bill bill : getAllBills()){
			if (b.getId() == bill.getId() && b.getMember().getId() == bill.getMember().getId()){
				result = true;
			}
		}
		return result;
	}

	public Bill getBill(int id) {
		return (Bill) db.getObject(id, TABLE_BILLS, BILL_COLUMNS);
	}
	
	
	public List<Bill> getBills(int memberid){
		List<Bill> memberbills = new ArrayList<Bill>();
		for (Bill b : getAllBills()){
			if (b.getMember().getId() == memberid){
				memberbills.add(b);
			}
		}
		return memberbills;
	}
	public List<Bill> getAllBills() {
		List<Object> billobjects = db.getAllObjects(TABLE_BILLS);
		List<Bill> bills = new ArrayList<Bill>();
		for (Object o : billobjects) {
			if (o instanceof Bill) {
				bills.add((Bill) o);
			}
		}
		return bills;
	}

	// Updating
	public void updateBill(Bill bill) {

		ContentValues values = new ContentValues();
		values.put(BILL_ID, bill.getId());
		values.put(BILL_AMOUNT, bill.getAmount());
		values.put(BILL_MEMBERID, bill.getMember().getId());
		values.put(BILL_PAYDATE, bill.getPaybeforeString());
		values.put(BILL_PAID, bill.isPaid());

		db.updateObject(TABLE_BILLS, BILL_ID, values, bill.getId());
		
	}

	// Deleting
	public void deleteBill(int id) {
		db.deleteObject(TABLE_BILLS, BILL_ID, id);

	}
}