package dbms.account;

import java.sql.Date;

public class AccountDto {
	private String id;
	private String password;
	private String email;
	private String name;
	private String birthday;
	private String gender;
	private String education;
	private String hobby;
	private Date joindate;
	
	//생성자
	public AccountDto() {}

	public AccountDto(String id, String password, String email, String name, String birthday, String gender,
			String education, String hobby, Date joindate) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
		this.education = education;
		this.hobby = hobby;
		this.joindate = joindate;
	}
	
	//Getter and Setter
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
}
