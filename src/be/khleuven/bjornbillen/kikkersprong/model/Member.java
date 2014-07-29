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
    private List<Presency> presencies;
 

	public Member(){}
 
    public Member(String firstname, String lastname, Date birthday, String imageurl) {
        presencies = new ArrayList<Presency>();
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
	
	public String getBirthdayString(){
		return getBirthday().get(Calendar.DATE)+"/"+getBirthday().get(Calendar.MONTH)+"/"+getBirthday().get(Calendar.YEAR);
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	
	public List<Presency> getPresencies() {
		return presencies;
	}

	public void setPresencies(List<Presency> presencies) {
		this.presencies = presencies;
	}
	
	public String toString(){
		return firstname + " " + lastname + " /" + birthday;
	}
	
    
     
}
