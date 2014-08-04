package be.khleuven.bjornbillen.kikkersprong.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.ImageView;

public class Member {
	private Integer id;
	private String firstname;
	private String lastname;
	private Calendar birthday;
	private String imageurl;
	private Calendar lastcheckin; 
	private boolean isPresent = false;

	public Member() {
	}

	public Member(int id, String firstname, String lastname, Calendar birthday,
			String imageurl, boolean present, Calendar lastcheckin) {
		setId(id);
		setFirstname(firstname);
		setLastname(lastname);
		setBirthday(birthday);
		setImageurl(imageurl);
		setPresent(present);
		setLastcheckin(lastcheckin);
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getBirthdayString() {
		return getBirthday().get(Calendar.YEAR) + "/"
				+ getBirthday().get(Calendar.MONTH) + "/"
				+ getBirthday().get(Calendar.DATE);
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String toString() {
		return firstname + " " + lastname + " /" + birthday;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public Calendar getLastcheckin() {
		return lastcheckin;
	}

	public void setLastcheckin(Calendar lastcheckin) {
		this.lastcheckin = lastcheckin;
	}
	
	public String getLastCheckinString(){
		return getLastcheckin().get(Calendar.YEAR) + "/" + getLastcheckin().get(Calendar.MONTH) + "/" + getLastcheckin().get(Calendar.DATE) + " " + getLastcheckin().get(Calendar.HOUR) + ":" + getLastcheckin().get(Calendar.MINUTE) + ":" + getLastcheckin().get(Calendar.SECOND);

	}

}
