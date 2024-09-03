package todo.todo;

import javax.persistence.*;

@Entity
@Table(name = "todo.state")
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "todo.state_seq")
	@Column(name = "state_id")
	private long state_id;

	@Column(name = "state_name")
	private String state_name;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;


	
	public long getState_id() {
		return state_id;
	}

	public void setState_id(long state_id) {
		this.state_id = state_id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State(long state_id, String state_name, Country country) {
		super();
		this.state_id = state_id;
		this.state_name = state_name;
		this.country = country;
	}

	public State() {
		super();
	}

}
