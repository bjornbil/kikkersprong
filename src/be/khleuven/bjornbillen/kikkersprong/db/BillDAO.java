package be.khleuven.bjornbillen.kikkersprong.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;

import com.hmkcode.android.sqlite.MySQLiteHelper;

public class BillDAO extends MySQLiteHelper {
	private static final String TABLE_BILLS = "presencies";

	// Table presencies Column names
	private static final String BILL_ID = "id";
	private static final String BILL_MEMBERID = "memberid";
	private static final String BILL_PAYDATE = "paydate";
	private static final String BILL_PAID = "ispaid";

	private static final String[] BILL_COLUMNS = { BILL_ID, BILL_MEMBERID,
			BILL_PAYDATE, BILL_PAID };

	public BillDAO(Context context) {
		super(context);

	}

	public void addBill(Bill bill) {
		if (bill == null) {
		} // exception handling
		ContentValues values = new ContentValues();
		values.put(BILL_ID, bill.getId());
		values.put(BILL_MEMBERID, bill.getMember().getId());
		values.put(BILL_PAYDATE, bill.getPaybeforeString());
		values.put(BILL_PAID, bill.isPaid());
		super.addObject(TABLE_BILLS, values);
	}

	public Bill getBill(int id) {
		return (Bill) super.getObject(id, TABLE_BILLS, BILL_COLUMNS);
	}

	public List<Bill> getAllBills() {
		List<Object> billobjects = super.getAllObjects(TABLE_BILLS);
		List<Bill> bills = new ArrayList<Bill>();
		for (Object o : billobjects) {
			if (o instanceof Attendance) {
				bills.add((Bill) o);
			}
		}
		return bills;
	}

	// Updating
	public void updateBills(Bill bill) {

		ContentValues values = new ContentValues();
		values.put(BILL_ID, bill.getId());
		values.put(BILL_MEMBERID, bill.getMember().getId());
		values.put(BILL_PAYDATE, bill.getPaybeforeString());
		values.put(BILL_PAID, bill.isPaid());

		super.updateObject(TABLE_BILLS, BILL_ID, values, bill.getId());
	}

	// Deleting
	public void deleteBill(int id) {
		super.deleteObject(TABLE_BILLS, BILL_ID, id);

	}
}