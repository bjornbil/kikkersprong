package com.hmkcode.android.sqlite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.model.Member;
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
	private static final String TABLE_MEMBERS = "members";

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FIRSTNAME = "firstname";
	private static final String KEY_LASTNAME = "lastname";
	private static final String KEY_DOB = "birthday";
	private static final String KEY_IMAGEURL = "imgurl";

	private static SQLiteDatabase kikkersprongdb;

	private static final String[] COLUMNS = { KEY_ID, KEY_FIRSTNAME,
			KEY_LASTNAME, KEY_DOB, KEY_IMAGEURL };

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
				+ "memberid INTEGER FOREIGN KEY, " + "start TEXT, "
				+ "end TEXT" + " )";

		// create table
		db.execSQL(CREATE_PRESENCY_TABLE);
		db.execSQL(CREATE_MEMBER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS members");

		// create fresh table
		this.onCreate(db);
	}

	public void addMember(Member member) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, member.getId());
		values.put(KEY_FIRSTNAME, member.getFirstname());
		values.put(KEY_LASTNAME, member.getLastname());
		values.put(KEY_DOB, member.getBirthdayString());
		values.put(KEY_IMAGEURL, member.getImageurl());
		kikkersprongdb.insert(TABLE_MEMBERS, null, values);
		kikkersprongdb.close();
	}

	public Member getMember(int id) {
		Cursor cursor = kikkersprongdb.query(TABLE_MEMBERS, // a. table
				COLUMNS, // b. column names
				" id = ?", // c. selections
				new String[] { String.valueOf(id) }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		if (cursor != null)
			cursor.moveToFirst();
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

	public List<Member> getAllMembers() {
		List<Member> members = new ArrayList<Member>();

		// 1. build the query
		String query = "SELECT  * FROM " + TABLE_MEMBERS;

		// 2. get reference to writable DB

		Cursor cursor = kikkersprongdb.rawQuery(query, null);

		Member member = null;
		if (cursor.moveToFirst()) {
			do {
				member = new Member();
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

				members.add(member);
			} while (cursor.moveToNext());
		}

		return members;
	}

	// Updating single book
	public int updateMember(Member member) {

		ContentValues values = new ContentValues();
		values.put(KEY_ID, member.getId());
		values.put(KEY_FIRSTNAME, member.getFirstname());
		values.put(KEY_LASTNAME, member.getLastname());
		values.put(KEY_DOB, member.getBirthdayString());
		values.put(KEY_IMAGEURL, member.getImageurl());

		int i = kikkersprongdb.update(TABLE_MEMBERS, // table
				values, // column/value
				KEY_ID + " = ?", // selections
				new String[] { String.valueOf(member.getId()) }); // selection
																	// args

		kikkersprongdb.close();

		return i;

	}

	// Deleting single book
	public void deleteMember(int id) {

		// 2. delete
		kikkersprongdb.delete(TABLE_MEMBERS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });

		// 3. close
		kikkersprongdb.close();

	}
}
