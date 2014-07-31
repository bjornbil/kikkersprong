package be.khleuven.bjornbillen.kikkersprong.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Presency;

import com.hmkcode.android.sqlite.MySQLiteHelper;

public class PresencyDAO extends MySQLiteHelper {

	private static final String TABLE_PRESENCIES = "presencies";

	// Table presencies Column names
	private static final String PRESENCY_ID = "id";
	private static final String PRESENCY_MEMBERID = "memberid";
	private static final String PRESENCY_START = "startdate";
	private static final String PRESENCY_END = "enddate";

	private static final String[] PRESENCY_COLUMNS = { PRESENCY_ID, PRESENCY_MEMBERID,
		PRESENCY_START, PRESENCY_END};
	
	public PresencyDAO(Context context) {
		super(context);

	}
	
	public void addPresency(Presency presency) {
		if (presency == null){} // exception handling
		ContentValues values = new ContentValues();
		values.put(PRESENCY_ID, presency.getId());
		values.put(PRESENCY_MEMBERID, presency.getMember().getId());
		values.put(PRESENCY_START, presency.getStartdateString());
		values.put(PRESENCY_END, presency.getEnddateString());
		super.addObject(TABLE_PRESENCIES, values);
	}

	public Presency getPresency(int id) {
		return (Presency) super.getObject(id, TABLE_PRESENCIES, PRESENCY_COLUMNS);
	}

	public List<Presency> getAllPresencies() {
		List<Object> presencies = super.getAllObjects(TABLE_PRESENCIES);
		List<Presency> pres = new ArrayList<Presency>();
		for (Object o : presencies){
			if (o instanceof Presency){
				pres.add((Presency) o);
			}
		}
		return pres;
	}

	// Updating 
	public void updatePresency(Presency presency) {

		ContentValues values = new ContentValues();
		values.put(PRESENCY_ID, presency.getId());
		values.put(PRESENCY_MEMBERID, presency.getMember().getId());
		values.put(PRESENCY_START, presency.getStartdateString());
		values.put(PRESENCY_END, presency.getEnddateString());
		
		super.updateObject(TABLE_PRESENCIES, PRESENCY_ID, values, presency.getId());
	}

	// Deleting 
	public void deleteMember(int id) {
		super.deleteObject(TABLE_PRESENCIES, PRESENCY_ID, id);

	}

}
