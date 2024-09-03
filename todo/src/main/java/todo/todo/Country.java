package todo.todo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todo.country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "todo.country_seq")
	@Column(name = "country_id")
	private long country_id;

	@Column(name = "country_code")
	private String country_code;

	@Column(name = "country_name")
	private String country_name;

	public long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(long country_id) {
		this.country_id = country_id;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public Country(long country_id, String country_code, String country_name) {
		super();
		this.country_id = country_id;
		this.country_code = country_code;
		this.country_name = country_name;
	}

	public Country() {
		super();
		// TODO Auto-generated constructor stub
	}

}
