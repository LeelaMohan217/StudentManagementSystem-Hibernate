package todo.todo;

import java.util.Date;

public class Student {

	private long id;
	private String name;
	private String branch;
	private Date dob;
	private int age;
	private String gender;
	private String email;
	private String mobile;
	private String country;
	private String state;
	private String city;
	private int pincode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public Student(long id, String name, String branch, Date dob, int age, String gender, String email, String mobile,
			String country, String state, String city, int pincode) {
		super();
		this.id = id;
		this.name = name;
		this.branch = branch;
		this.dob = dob;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.mobile = mobile;
		this.country = country;
		this.state = state;
		this.city = city;
		this.pincode = pincode;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

}
