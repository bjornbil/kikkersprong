package com.hmkcode.android.sqlite;

import static com.hmkcode.android.sqlite.MySQLiteHelper.kikkersprongdb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Presency;
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
		
	// Singleton access
	private static MySQLiteHelper sInstance;

	public static SQLiteDatabase kikkersprongdb;

	public static MySQLiteHelper getInstance(Context context) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (sInstance == null) {
	      sInstance = new MySQLiteHelper(context.getApplicationContext());
	    }
	    return sInstance;
	  }

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		kikkersprongdb = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create table
		String CREATE_MEMBER_TABLE = "CREATE TABLE members ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
				+ "birthday TEXT, " + "imgurl TEXT" + " )";

		String CREATE_PRESENCY_TABLE = "CREATE TABLE presency ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "memberid INTEGER , " + "start TEXT, "
				+ "end TEXT,"
				+ "FOREIGN KEY (memberid) REFERENCES members (id));";
				
		
		String CREATE_BILL_TABLE = "CREATE TABLE bills ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "memberid INTEGER," + "paydate TEXT, "
				+ "ispaid TEXT,"
				+ " FOREIGN KEY (memberid) REFERENCES members (id));";
				// create table
		db.execSQL(CREATE_PRESENCY_TABLE);
		db.execSQL(CREATE_MEMBER_TABLE);
		db.execSQL(CREATE_BILL_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS members");
		db.execSQL("DROP TABLE IF EXISTS presencies");
		db.execSQL("DROP TABLE IF EXISTS bills");

		// create fresh table
		this.onCreate(db);
	}
	
	public void addObject(String table, ContentValues values){
		if (values != null)
		kikkersprongdb.insert(table, null, values);
	}
	private Member pullStringMember(Cursor cursor){
		Member member = new Member();
		member.setId(Integer.parseInt(cursor.getString(0)));
		member.setFirstname(cursor.getString(1));
		member.setLastname(cursor.getString(2));
		String datevalues[] = cursor.getString(3).split("/");
		Calendar birthday = Calendar.getInstance();
		birthday.set(Integer.parseInt(datevalues[2]),
				Integer.parseInt(datevalues[1]),
				Integer.parseInt(datevalues[0]));
		member.setBirthday(birthday);
		member.setImageurl(cursor.getString(4));
		return member;
	}
	
	private Presency pullStringPresency(Cursor cursor){
		Presency presency = new Presency();
		presency.setId(Integer.parseInt(cursor.getString(0)));
		presency.setMember((Member) getObject(Integer.parseInt(cursor.getString(1)),"members",MEMBER_COLUMNS));
		String calendarvalues[] = cursor.getString(3).split(" ");
		String datevalues = calendarvalues[0];
		String timevalues = calendarvalues[1];
		String date[] = datevalues.split("/");
		String time[] = timevalues.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, Integer.parseInt(date[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(date[1]));
		cal.set(Calendar.YEAR, Integer.parseInt(date[2]));
		cal.set(Calendar.HOUR, Integer.parseInt(time[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(time[2]));
		presency.setStartdate(cal);
		calendarvalues = cursor.getString(4).split(" ");
		cal.set(Calendar.DATE, Integer.parseInt(date[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(date[1]));
		cal.set(Calendar.YEAR, Integer.parseInt(date[2]));
		cal.set(Calendar.HOUR, Integer.parseInt(time[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(time[2]));
		presency.setEnddate(cal);
		return presency;
	}
	public Object getObject(int id, String table, String[] columns){
		Cursor cursor = kikkersprongdb.query(table, // a. table
				columns, // b. column names
				" id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		if (cursor != null)
			cursor.moveToFirst();
		Object returnobject = null;
		if (table.equals("members")){
			returnobject = pullStringMember(cursor);
		}
		else if (table.equals("presencies")){
			returnobject = pullStringPresency(cursor);
		}
		kikkersprongdb.close();
		return returnobject;
	}
	

	

	public List<Object> getAllObjects(String table) {
		List<Object> objects = new ArrayList<Object>();

		// 1. build the query
		String query = "SELECT  * FROM " + table;

		// 2. get reference to writable DB

		Cursor cursor = kikkersprongdb.rawQuery(query, null);

		Object object = null;
		if (cursor.moveToFirst()) {
			if (table.equals("members")){
			do {
				object = pullStringMember(cursor);
				objects.add(object);
			} while (cursor.moveToNext());
			}
			else if (table.equals("presencies")){
				do {
					object = pullStringPresency(cursor);
					objects.add(object);
				} while (cursor.moveToNext());
				}
			}
		kikkersprongdb.close();

		return objects;
	}

	// Updating 
	public void updateObject(String table, String memberid, ContentValues values, int memberId) {

		kikkersprongdb.update(table, // table
				values, // column/value
				memberid + " = ?", // selections
				new String[] { String.valueOf(memberId) }); // selection
																	// args

		kikkersprongdb.close();

	}

	// Deleting 
	public void deleteObject(String table, String memberid, int id) {

		kikkersprongdb.delete(table, memberid + " = ?",
				new String[] { String.valueOf(id) });

		kikkersprongdb.close();

	}
}
