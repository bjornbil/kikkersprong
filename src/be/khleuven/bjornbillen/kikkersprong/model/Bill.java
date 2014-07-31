package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.Calendar;

public class Bill {
	private Integer id;
	private Member member;
	private Calendar paybefore;
	private boolean paid;
	
	public Bill(){
		
	}
	
	public Bill(Integer id, Member member, Calendar paydate){
		setId(id);
		setMember(member);
		setPaybefore(paydate);
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Calendar getPaybefore() {
		return paybefore;
	}
	
	public String getPaybeforeString(){
		return getPaybefore().get(Calendar.DATE)+"/"+getPaybefore().get(Calendar.MONTH)+"/"+getPaybefore().get(Calendar.YEAR);
	}

	public void setPaybefore(Calendar paybefore) {
		this.paybefore = paybefore;
	}
	
	
}
