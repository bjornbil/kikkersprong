package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.Calendar;

public class Attendance {

	private int id;
	private Member member;
	private Calendar startdate, enddate;

	public Attendance() {

	}

	public Attendance(int id, Member member, Calendar startdate, Calendar enddate) {
		setId(id);
		setMember(member);
		setStartdate(startdate);
		setEnddate(enddate);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getStartdate() {
		return startdate;
	}

	public String getStartdateString() {
		return getStartdate().get(Calendar.YEAR) + "/"
				+ getStartdate().get(Calendar.MONTH) + "/"
				+ getStartdate().get(Calendar.DATE) + " "
				+ getStartdate().get(Calendar.HOUR_OF_DAY) + ":"
				+ getStartdate().get(Calendar.MINUTE) + ":"
				+ getStartdate().get(Calendar.SECOND);
	}

	public void setStartdate(Calendar startdate) {
		this.startdate = startdate;
	}

	public Calendar getEnddate() {
		return enddate;
	}

	public String getEnddateString() {
		return getEnddate().get(Calendar.YEAR) + "/"
				+ getEnddate().get(Calendar.MONTH) + "/"
				+ getEnddate().get(Calendar.DATE) + " "
				+ getEnddate().get(Calendar.HOUR_OF_DAY) + ":"
				+ getEnddate().get(Calendar.MINUTE) + ":"
				+ getEnddate().get(Calendar.SECOND);
	}

	public void setEnddate(Calendar enddate) {
		this.enddate = enddate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public int getDuration(){
		double endhour = getEnddate().get(Calendar.HOUR_OF_DAY) + (getEnddate().get(Calendar.MINUTE)/60);
		double starthour = getStartdate().get(Calendar.HOUR_OF_DAY) + (getStartdate().get(Calendar.MINUTE)/60);
		double hoursofattendance = endhour - starthour;
		int result = (int) Math.round(hoursofattendance);
		return result;
	}
	public String toString(){
		int duration = getDuration();
		return getStartdate().get(Calendar.YEAR) + "/" + (getStartdate().get(Calendar.MONTH)+1) + "/" + getStartdate().get(Calendar.DATE) + " " + duration + "u";
	}
}
