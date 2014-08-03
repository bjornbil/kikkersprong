package be.khleuven.bjornbillen.kikkersprong.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;

import com.hmkcode.android.sqlite.MySQLiteHelper;

public class PresencyDAO extends MySQLiteHelper {

	private static final String TABLE_ATTENDANCES = "attendances";

	// Table presencies Column names
	private static final String ATTENDANCE_ID = "id";
	private static final String ATTENDANCE_MEMBERID = "memberid";
	private static final String ATTENDANCE_START = "startdate";
	private static final String ATTENDANCE_END = "enddate";

	private static final String[] ATTENDANCE_COLUMNS = { ATTENDANCE_ID,
			ATTENDANCE_MEMBERID, ATTENDANCE_START, ATTENDANCE_END };

	public PresencyDAO(Context context) {
		super(context);

	}

	public void addPresency(Attendance attendance) {
		if (attendance == null) {
		} // exception handling
		ContentValues values = new ContentValues();
		values.put(ATTENDANCE_ID, attendance.getId());
		values.put(ATTENDANCE_MEMBERID, attendance.getMember().getId());
		values.put(ATTENDANCE_START, attendance.getStartdateString());
		values.put(ATTENDANCE_END, attendance.getEnddateString());
		super.addObject(TABLE_ATTENDANCES, values);
	}

	public Attendance getPresency(int id) {
		return (Attendance) super.getObject(id, TABLE_ATTENDANCES,
				ATTENDANCE_COLUMNS);
	}

	public List<Attendance> getAllAttendances() {
		List<Object> presencies = super.getAllObjects(TABLE_ATTENDANCES);
		List<Attendance> pres = new ArrayList<Attendance>();
		for (Object o : presencies) {
			if (o instanceof Attendance) {
				pres.add((Attendance) o);
			}
		}
		return pres;
	}

	// Updating
	public void updateAttendance(Attendance att) {

		ContentValues values = new ContentValues();
		values.put(ATTENDANCE_ID, att.getId());
		values.put(ATTENDANCE_MEMBERID, att.getMember().getId());
		values.put(ATTENDANCE_START, att.getStartdateString());
		values.put(ATTENDANCE_END, att.getEnddateString());
		super.updateObject(TABLE_ATTENDANCES, ATTENDANCE_ID, values,
				att.getId());
	}

	// Deleting
	public void deleteAttendance(int id) {
		super.deleteObject(TABLE_ATTENDANCES, ATTENDANCE_ID, id);

	}

}
