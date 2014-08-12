package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.Calendar;

public class Bill {
	private Integer id;
	private Member member;
	private Calendar paybefore;
	private boolean paid;
	private double amount;

	public Bill() {

	}

	public Bill(Integer id, double amount, Member member, Calendar paydate, boolean paid) {
		setId(id);
		setMember(member);
		setPaybefore(paydate);
		setPaid(paid);
		setAmount(amount);
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Calendar getPaybefore() {
		return paybefore;
	}

	public String getPaybeforeString() {
		return getPaybefore().get(Calendar.YEAR) + "/"
				+ (getPaybefore().get(Calendar.MONTH)+1) + "/"
				+ getPaybefore().get(Calendar.DATE);
	}

	public void setPaybefore(Calendar paybefore) {
		this.paybefore = paybefore;
	}
	
	public String toString(){
		return getPaybefore().get(Calendar.YEAR) + "/"
				+ (getPaybefore().get(Calendar.MONTH)+1) + "/"
				+ getPaybefore().get(Calendar.DATE) + " " + getAmount() + " " + isPaid() + " " + getMember().getFirstname() + " " + getMember().getLastname();
	}

}
