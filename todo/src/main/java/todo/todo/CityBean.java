package todo.todo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Named
@SessionScoped
public class CityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private City city;
	private List<City> cities;
	private List<State> states;
	private String selectedStateId;
	private List<Country> countries;
	private String selectedCountryCode;
	private String selectedCountryName;
	private String searchCityName;

	// Getters and Setters for new fields
	public String getSearchCityName() {
		return searchCityName;
	}

	public void setSearchCityName(String searchCityName) {
		this.searchCityName = searchCityName;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public String getSelectedCountryCode() {
		return selectedCountryCode;
	}

	public void setSelectedCountryCode(String selectedCountryCode) {
		this.selectedCountryCode = selectedCountryCode;
	}

	public String getSelectedCountryName() {
		return selectedCountryName;
	}

	public void setSelectedCountryName(String selectedCountryName) {
		this.selectedCountryName = selectedCountryName;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public String getSelectedStateId() {
		return selectedStateId;
	}

	public void setSelectedStateId(String selectedStateId) {
		this.selectedStateId = selectedStateId;
	}

	@PostConstruct
	public void init() {
		cities = getAllCities();
		city = new City();
		countries = getAllCountries();
	}

	// Retrieve all cities
	public List<City> getAllCities() {
		Session session = null;
		List<City> cities = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query<City> query = session.createQuery("select c from City c join fetch c.state s join fetch s.country",
					City.class);
			cities = query.list();

			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return cities;
	}

	public List<Country> getAllCountries() {
		Session session = null;
		List<Country> countries = new ArrayList<>();

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query<Country> query = session.createQuery("from Country", Country.class);
			countries = query.list();

			session.getTransaction().commit();
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return countries;
	}

	public void loadStatesForCountry(AjaxBehaviorEvent event) {
		if (selectedCountryCode != null) {
			Session session = null;

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();

				Query<State> query = session.createQuery("from State s where s.country.country_code = :code",
						State.class);
				query.setParameter("code", selectedCountryCode);
				states = query.list();

				session.getTransaction().commit();

				// Set the selected country name based on the code
				if (countries != null) {
					for (Country country : countries) {
						if (country.getCountry_code().equals(selectedCountryCode)) {
							selectedCountryName = country.getCountry_name();
							break;
						}
					}
				}
			} catch (Exception e) {
				if (session != null) {
					session.getTransaction().rollback();
				}
				e.printStackTrace();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
	}

	public void saveCity() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Fetch the selected state from the list
			for (State state : states) {
				if (String.valueOf(state.getState_id()).equals(selectedStateId)) {
					city.setState(state);
					break;
				}
			}

			session.save(city);
			transaction.commit();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City saved successfully!"));

			// Reset fields and refresh city list
			selectedCountryCode = null;
			selectedCountryName = null;
			city = new City();
			states = null;
			cities = getAllCities();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error saving city."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// Search cities by name
	public void searchCitiesByName() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String queryString = "from City where lower(city_name) like :name";
			Query<City> query = session.createQuery(queryString, City.class);
			query.setParameter("name", "%" + searchCityName.toLowerCase() + "%");
			cities = query.list();

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// Reset search and refresh the city list
	public void resetSearch() {
		searchCityName = "";
		cities = getAllCities();
	}

	public void deleteCity(long city_id) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			City cityToDelete = session.get(City.class, city_id);
			if (cityToDelete != null) {
				session.delete(cityToDelete);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City deleted successfully!"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City not found."));
			}

			transaction.commit();
			cities = getAllCities(); // Refresh city list
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error deleting city."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void refreshCities() {
		cities = getAllCities();
	}

	public void loadCityForEdit(City city) {
		this.city = city; // Load the selected city details into the form
		this.selectedCountryCode = city.getState().getCountry().getCountry_code();
		this.selectedCountryName = city.getState().getCountry().getCountry_name();
		this.selectedStateId = String.valueOf(city.getState().getState_id());
		loadStatesForCountry(null); // Load the states for the selected country
	}

	public void updateCity() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Fetch the selected state from the list
			for (State state : states) {
				if (String.valueOf(state.getState_id()).equals(selectedStateId)) {
					city.setState(state);
					break;
				}
			}

			session.update(city); // Update the city details
			transaction.commit();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("City updated successfully!"));

			// Reset fields and refresh city list
			selectedCountryCode = null;
			selectedCountryName = null;
			city = new City();
			states = null;
			cities = getAllCities();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error updating city."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
