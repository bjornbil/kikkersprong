package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.Date;

public class Presency {

	private int id;
	private Member member;
	private Date startdate, enddate;
	
	
	public Presency(){
		
	}
	
	public Presency(Date startdate, Date enddate){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
