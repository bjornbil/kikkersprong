package be.khleuven.bjornbillen.kikkersprong.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;

import com.hmkcode.android.sqlite.MySQLiteHelper;

public class AttendanceDAO {

	private static final String TABLE_ATTENDANCES = "attendances";

	// Table presencies Column names
	private static final String ATTENDANCE_ID = "id";
	private static final String ATTENDANCE_MEMBERID = "memberid";
	private static final String ATTENDANCE_START = "start";
	private static final String ATTENDANCE_END = "end";
	private MySQLiteHelper db;
	private BillDAO billcontroller;
	private static AttendanceDAO attendancecontroller;
	private Context context;
	
	private static final String[] ATTENDANCE_COLUMNS = { ATTENDANCE_ID,
			ATTENDANCE_MEMBERID, ATTENDANCE_START, ATTENDANCE_END };

	public AttendanceDAO(Context context) {
		db = new MySQLiteHelper(context);
		this.context = context;
		billcontroller = BillDAO.getInstance(context);
		billcontroller.setPricePerHour(3.5);
	}
	
	public static AttendanceDAO getInstance(Context context){
		if (attendancecontroller == null){
			attendancecontroller = new AttendanceDAO(context);
		}
	
		return attendancecontroller;
	}
	
	public int getSize(){
		return getAllAttendances().size();
	}

	public boolean hasAttendance(Attendance a){
		boolean result = false;
		for (Attendance att : getAllAttendances()){
			if (a.getId() == att.getId() && a.getMember().getId() == att.getMember().getId()){
				result = true;
			}
		}
		return result;
	}
	public void addAttendance(Attendance attendance) {
		if (attendance == null) {
		} // exception handling
		ContentValues values = new ContentValues();
		values.put(ATTENDANCE_ID, attendance.getId());
		values.put(ATTENDANCE_MEMBERID, attendance.getMember().getId());
		values.put(ATTENDANCE_START, attendance.getStartdateString());
		values.put(ATTENDANCE_END, attendance.getEnddateString());
		db.addObject(TABLE_ATTENDANCES, values);
		int month = -1;
		int day = -1;
		int totaaluren = 0;
		if (attendance.getEnddate().get(Calendar.DATE) == 30 && attendance.getEnddate().get(Calendar.MONTH) % 2 == 0){
			month = attendance.getEnddate().get(Calendar.MONTH);
			day = attendance.getEnddate().get(Calendar.DATE);
		}
		else if (attendance.getEnddate().get(Calendar.DATE) == 31){
			month = attendance.getEnddate().get(Calendar.MONTH);
			day = attendance.getEnddate().get(Calendar.DATE);
		}
		else if ((attendance.getEnddate().get(Calendar.DATE) == 28 || attendance.getEnddate().get(Calendar.DATE) == 29) && attendance.getEnddate().get(Calendar.MONTH) == Calendar.FEBRUARY){
			month = attendance.getEnddate().get(Calendar.MONTH);
			day= attendance.getEnddate().get(Calendar.DATE);
		} 
		Calendar vorigemaand = Calendar.getInstance();
		if (month != -1){
			for (Attendance a : getAttendances(attendance.getMember().getId())){
				if (a.getStartdate().get(Calendar.MONTH) == month){
					totaaluren += a.getDuration();
				}
			}
			double prijs = totaaluren * billcontroller.getPricePerHour();
			if (prijs > 0){
			billcontroller.addBill(new Bill(billcontroller.getSize(),prijs,attendance.getMember(),attendance.getEnddate(),false));
			}
			vorigemaand.set(Calendar.MONTH, month);
			if (day == 30){
				vorigemaand.set(Calendar.DATE, 31);
			}
			else if (day == 31 && Calendar.MARCH != month){
				vorigemaand.set(Calendar.DATE, 30);
			}
			else if (Calendar.MARCH == month){
				vorigemaand.set(Calendar.DATE,28);
			}
			vorigemaand.add(Calendar.MONTH, -1);
		}
		else{
			while (vorigemaand.get(Calendar.MONTH) != attendance.getStartdate().get(Calendar.MONTH)){
				vorigemaand.add(Calendar.MONTH, -1);
			}
		vorigemaand.add(Calendar.MONTH, -1);
		boolean rekeningbestaat = false;
		for (Bill b : billcontroller.getBills(attendance.getMember().getId())){
			if (b.getPaybefore().get(Calendar.MONTH) == vorigemaand.get(Calendar.MONTH)){
				rekeningbestaat = true;
			}
		}
		// maak vorige maand aan in geval prijs > 0 en dus aanwezigheden > 0 in die maand
		// indien nog geen rekening bestaande op deze maand
		if (!rekeningbestaat){
			for (Attendance a : getAttendances(attendance.getMember().getId())){
				
				if (a.getStartdate().get(Calendar.MONTH) == vorigemaand.get(Calendar.MONTH)){
					totaaluren += a.getDuration();
				}
			}
			double prijs = totaaluren * billcontroller.getPricePerHour();
						
			if (prijs > 0){
				billcontroller.addBill(new Bill(billcontroller.getSize(),prijs,attendance.getMember(),vorigemaand,false));
			}
			
		}
		}
		
		
	}

	public Attendance getAttendance(int id) {
		return (Attendance) db.getObject(id, TABLE_ATTENDANCES,
				ATTENDANCE_COLUMNS);
	}
	
	public List<Attendance> getAttendances(int memberid){
		List<Attendance> attendances = new ArrayList<Attendance>();
		for (Attendance a : getAllAttendances()){
			if (a.getMember().getId() == memberid){
				attendances.add(a);
			}
		}
		return attendances;
	}

	public List<Attendance> getAllAttendances() {
		List<Object> presencies = db.getAllObjects(TABLE_ATTENDANCES);
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
		db.updateObject(TABLE_ATTENDANCES, ATTENDANCE_ID, values,
				att.getId());
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

	// Deleting
	public void deleteAttendance(int id) {
		db.deleteObject(TABLE_ATTENDANCES, ATTENDANCE_ID, id);

	}

}
