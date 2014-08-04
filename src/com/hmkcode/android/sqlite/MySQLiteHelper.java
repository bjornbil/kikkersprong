package com.hmkcode.android.sqlite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "KIKKERSPRONGDB";

	private static final String[] MEMBER_COLUMNS = { "id", "firstname",
			"lastname", "birthday", "imgurl" };


	private SQLiteDatabase db;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create table
		
		String CREATE_MEMBER_TABLE = "CREATE TABLE members ( "
				+ "id INTEGER PRIMARY KEY, " + "name TEXT, "
				+ "birthday TEXT, " + "imgurl TEXT," + "present INTEGER,"+ "checkin TEXT" +" )";

		String CREATE_ATTENDANCE_TABLE = "CREATE TABLE attendances ( "
				+ "id INTEGER PRIMARY KEY, "
				+ "memberid INTEGER , " + "start TEXT, " + "end TEXT,"
				+ "FOREIGN KEY (memberid) REFERENCES members (id));";

		String CREATE_BILL_TABLE = "CREATE TABLE bills ( "
				+ "id INTEGER PRIMARY KEY, "
				+ "memberid INTEGER," + "paydate TEXT, " + "ispaid TEXT,"
				+ " FOREIGN KEY (memberid) REFERENCES members (id));";
		
		
		
		db.execSQL(CREATE_MEMBER_TABLE);
		db.execSQL(CREATE_ATTENDANCE_TABLE);
		db.execSQL(CREATE_BILL_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS members");
		db.execSQL("DROP TABLE IF EXISTS attendances");
		db.execSQL("DROP TABLE IF EXISTS bills");

		// create fresh table
		this.onCreate(db);
	}

	public void addObject(String table, ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (values != null)
			db.insert(table, null, values);
	}
	
	private Bill pullStringBill(Cursor cursor){
		// TODO : bill string pull
		return null;
	}
	private Member pullStringMember(Cursor cursor) {
		Member member = new Member();
		member.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
		member.setFirstname(cursor.getString(cursor.getColumnIndex("name")).split(" ")[0]);
		member.setLastname(cursor.getString(cursor.getColumnIndex("name")).split(" ")[1]);
		String datevalues[] = cursor.getString(cursor.getColumnIndex("birthday")).split("/");
		Calendar birthday = Calendar.getInstance();
		birthday.set(Calendar.YEAR,Integer.parseInt(datevalues[0]));
		birthday.set(Calendar.MONTH, Integer.parseInt(datevalues[1]));
		birthday.set(Calendar.DATE, Integer.parseInt(datevalues[2]));
		member.setBirthday(birthday);
		member.setImageurl(cursor.getString(cursor.getColumnIndex("imgurl")));
		if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("present"))) == 1){
		member.setPresent(true);
		}
		else {
			member.setPresent(false);
		}
		Calendar lastcheck = Calendar.getInstance();
		String checkvalues[] = cursor.getString(cursor.getColumnIndex("checkin")).split(" ");
		String checkdate[] = checkvalues[0].split("/");
		String checktime[] = checkvalues[1].split(":");
		lastcheck.set(Calendar.YEAR, Integer.parseInt(checkdate[0]));
		lastcheck.set(Calendar.MONTH, Integer.parseInt(checkdate[1]));
		lastcheck.set(Calendar.DATE, Integer.parseInt(checkdate[2]));
		lastcheck.set(Calendar.HOUR_OF_DAY, Integer.parseInt(checktime[0]));
		lastcheck.set(Calendar.MINUTE, Integer.parseInt(checktime[1]));
		lastcheck.set(Calendar.SECOND, Integer.parseInt(checktime[2]));
		member.setLastcheckin(lastcheck);
		return member;
	}

	private Attendance pullStringAttendance(Cursor cursor) {
		Attendance presency = new Attendance();
		presency.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
		presency.setMember((Member) getObject(
				Integer.parseInt(cursor.getString(cursor.getColumnIndex("memberid"))), "members",
				MEMBER_COLUMNS));
		String calendarvalues[] = cursor.getString(cursor.getColumnIndex("start")).split(" ");
		String datevalues = calendarvalues[0];
		String timevalues = calendarvalues[1];
		String date[] = datevalues.split("/");
		String time[] = timevalues.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(date[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(date[1]));
		cal.set(Calendar.DATE, Integer.parseInt(date[2]));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(time[2]));
		presency.setStartdate(cal);
		calendarvalues = cursor.getString(cursor.getColumnIndex("end")).split(" ");
		cal = Calendar.getInstance();
		datevalues = calendarvalues[0];
		timevalues = calendarvalues[1];
		String date2[] = datevalues.split("/");
		String time2[] = timevalues.split(":");
		cal.set(Calendar.YEAR, Integer.parseInt(date2[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(date2[1]));
		cal.set(Calendar.DATE, Integer.parseInt(date2[2]));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time2[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(time2[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(time2[2]));
		presency.setEnddate(cal);
		return presency;
	}

	public Object getObject(int id, String table, String[] columns) {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM members WHERE id =" + id;
		Cursor cursor = db.rawQuery(selectQuery, null);
		/*Cursor cursor = db.query(table, // a. table
				columns, // b. column names
				"id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit */
		
		Object returnobject = null;
		if (cursor != null && cursor.moveToFirst()){
			
		
		if (table.equals("members")) {
			returnobject = pullStringMember(cursor);
		} else if (table.equals("attendances")) {
			returnobject = pullStringAttendance(cursor);
		}
		else if (table.equals("bills")){
			returnobject = pullStringBill(cursor);
		}
		}
		db.close();
		return returnobject;
	}

	public List<Object> getAllObjects(String table) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Object> objects = new ArrayList<Object>();

		// 1. build the query
		String query = "SELECT * FROM " + table;

		// 2. get reference to writable DB

		Cursor cursor = db.rawQuery(query, null);

		Object object = null;
		if (cursor.moveToFirst()) {
			if (table.equals("members")) {
				do {
					object = pullStringMember(cursor);
					objects.add(object);
				} while (cursor.moveToNext());
			} else if (table.equals("attendances")) {
				do {
					object = pullStringAttendance(cursor);
					objects.add(object);
				} while (cursor.moveToNext());
			}
			else if (table.equals("attendances")){
				do{
					object = pullStringBill(cursor);
					objects.add(object);
				} while (cursor.moveToNext());
			}
		}
		db.close();

		return objects;
	}

	// Updating
	public void updateObject(String table, String memberid,
			ContentValues values, int memberId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(table, // table
				values, // column/value
				memberid + " = ?", // selections
				new String[] { String.valueOf(memberId) }); // selection
															// args

		db.close();

	}

	// Deleting
	public void deleteObject(String table, String memberid, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table, memberid + " = ?",
				new String[] { String.valueOf(id) });

		db.close();

	}
}
