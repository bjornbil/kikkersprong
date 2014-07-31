package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.Calendar;


public class Presency {

	private int id;
	private Member member;
	private Calendar startdate, enddate;
	
	
	public Presency(){
		
	}
	
	public Presency(Calendar startdate, Calendar enddate){
		
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

	public String getStartdateString(){
		return getStartdate().get(Calendar.DATE)+"/"+getStartdate().get(Calendar.MONTH)+"/"+getStartdate().get(Calendar.YEAR) + " " +
				getStartdate().get(Calendar.HOUR)+":"+getStartdate().get(Calendar.MINUTE)+":"+getStartdate().get(Calendar.SECOND);		
	}
	
	public void setStartdate(Calendar startdate) {
		this.startdate = startdate;
	}

	public Calendar getEnddate() {
		return enddate;
	}
	
	public String getEnddateString(){
		return getEnddate().get(Calendar.DATE)+"/"+getEnddate().get(Calendar.MONTH)+"/"+getEnddate().get(Calendar.YEAR) + " " +
				getEnddate().get(Calendar.HOUR)+":"+getEnddate().get(Calendar.MINUTE)+":"+getEnddate().get(Calendar.SECOND);		
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

}
