package todo.todo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "todo.student")
public class Student2 {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "todo.student_seq")
	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "college")
	private String college;

	@Column(name = "branch")
	private String branch;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "age")
	private int age;

	@Column(name = "gender")
	private String gender;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile")
	private String mobile;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	@Column(name = "pincode")
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

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public Student2(long id, String name, String college, String branch, Date dob, int age, String gender, String email,
			String mobile, Country country, State state, City city, int pincode) {
		super();
		this.id = id;
		this.name = name;
		this.college = college;
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

	public Student2() {
		super();
		// TODO Auto-generated constructor stub
	}

}
