package be.khleuven.bjornbillen.kikkersprong.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hmkcode.android.sqlite.MySQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

//TODO : exception handling

public class MemberDAO extends MySQLiteHelper {
	// TABLE name
	private static final String TABLE_MEMBERS = "members";
	// Table member Column names
	private static final String MEMBER_ID = "id";
	private static final String MEMBER_NAME = "name";
	private static final String MEMBER_DOB = "birthday";
	private static final String MEMBER_IMAGEURL = "imgurl";
	
	private static final String[] MEMBER_COLUMNS = { MEMBER_ID, MEMBER_NAME,
		MEMBER_DOB, MEMBER_IMAGEURL };

	public MemberDAO(Context context) {
		super(context);
	}

	public void addMember(Member member) {
		if (member == null){} // exception handling
		ContentValues values = new ContentValues();
		values.put(MEMBER_ID, member.getId());
		values.put(MEMBER_NAME, member.getFirstname() + " " + member.getLastname());
		values.put(MEMBER_DOB, member.getBirthdayString());
		values.put(MEMBER_IMAGEURL, member.getImageurl());
		super.addObject(TABLE_MEMBERS, values);
	}

	public Member getMember(int id) {
		return (Member) super.getObject(id, TABLE_MEMBERS, MEMBER_COLUMNS);
	}

	public List<Member> getAllMembers() {
		List<Object> members = super.getAllObjects(TABLE_MEMBERS);
		List<Member> resmembers = new ArrayList<Member>();
		for (Object o : members){
			if (o instanceof Member){
				resmembers.add((Member) o);
			}
		}
		return resmembers;
	}

	// Updating 
	public void updateMember(Member member) {

		ContentValues values = new ContentValues();
		values.put(MEMBER_ID, member.getId());
		values.put(MEMBER_NAME, member.getFirstname() + " " + member.getLastname());
		values.put(MEMBER_DOB, member.getBirthdayString());
		values.put(MEMBER_IMAGEURL, member.getImageurl());

		super.updateObject(TABLE_MEMBERS, MEMBER_ID, values, member.getId());
	}

	// Deleting 
	public void deleteMember(int id) {
		super.deleteObject(TABLE_MEMBERS, MEMBER_ID, id);

	}
	
}
