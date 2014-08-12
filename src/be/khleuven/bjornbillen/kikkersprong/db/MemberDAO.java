package be.khleuven.bjornbillen.kikkersprong.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hmkcode.android.sqlite.MySQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

//TODO : exception handling

public class MemberDAO  {
	// TABLE name
	private static final String TABLE_MEMBERS = "members";
	// Table member Column names
	private static final String MEMBER_ID = "id";
	private static final String MEMBER_NAME = "name";
	private static final String MEMBER_DOB = "birthday";
	private static final String MEMBER_IMAGEURL = "imgurl";
	private static final String MEMBER_PRESENT = "present";
	private static final String MEMBER_LASTCHECK = "checkin";
	private int currentid;
	private MySQLiteHelper db;
	private static MemberDAO membercontroller;
	private Context context;

	private static final String[] MEMBER_COLUMNS = { MEMBER_ID, MEMBER_NAME,
			MEMBER_DOB, MEMBER_IMAGEURL, MEMBER_PRESENT, MEMBER_LASTCHECK };

	public MemberDAO(Context context) {
		db = new MySQLiteHelper(context);
		this.context = context;
	}
	
	public static MemberDAO getInstance(Context context){
		if (membercontroller == null){
			membercontroller = new MemberDAO(context);
		}
		return membercontroller;
	}
	
	
	public int getSize(){
		return getAllMembers().size();
	}

	public void addMember(Member member) {
		if (member == null) {
		} // exception handling
		ContentValues values = new ContentValues();
		values.put(MEMBER_ID, member.getId());
		values.put(MEMBER_NAME,
				member.getFirstname() + " " + member.getLastname());
		values.put(MEMBER_DOB, member.getBirthdayString());
		values.put(MEMBER_IMAGEURL, member.getImageurl());
		int present = 0;
		if(member.isPresent()){
			present = 1;
		}
		else {
			present = 0;
		}
		values.put(MEMBER_PRESENT, present);
		values.put(MEMBER_LASTCHECK, member.getLastCheckinString());
		db.addObject(TABLE_MEMBERS, values);
	}
	
	public int getCurrentMemberID(){
		return currentid;
	}
	
	public void setCurrentMemberID(int id){
		this.currentid = id;
	}

	public Member getMember(int id) {
		return (Member) db.getObject(id, TABLE_MEMBERS, MEMBER_COLUMNS);
		
	}

	public void update(){
		XMLDatabase xml = new XMLDatabase(context);
		try {
			xml.writetoXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Member> getAllMembers() {
		List<Object> members = db.getAllObjects(TABLE_MEMBERS);
		List<Member> resmembers = new ArrayList<Member>();
		for (Object o : members) {
			if (o instanceof Member) {
				resmembers.add((Member) o);
			}
		}
		return resmembers;
	}
	
	public boolean existMember(String firstname, String lastname){
		boolean exists = false;
		for (Member member : getAllMembers()){
			if (member.getFirstname().equals(firstname) && member.getLastname().equals(lastname)){
				exists = true;
			}
		}
		return exists;
	}

	// Updating
	public void updateMember(Member member) {

		ContentValues values = new ContentValues();
		values.put(MEMBER_ID, member.getId());
		values.put(MEMBER_NAME,
				member.getFirstname() + " " + member.getLastname());
		values.put(MEMBER_DOB, member.getBirthdayString());
		values.put(MEMBER_IMAGEURL, member.getImageurl());
		int present = 0;
		if(member.isPresent()){
			present = 1;
		}
		else {
			present = 0;
		}
		values.put(MEMBER_PRESENT, present);
		values.put(MEMBER_LASTCHECK, member.getLastCheckinString());

		db.updateObject(TABLE_MEMBERS, MEMBER_ID, values, member.getId());
		
	}

	// Deleting
	public void deleteMember(int id) {
		db.deleteObject(TABLE_MEMBERS, MEMBER_ID, id);
	}

}
