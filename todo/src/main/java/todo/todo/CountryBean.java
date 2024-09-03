package todo.todo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Named
@SessionScoped
public class CountryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Country country;
	private List<Country> countries;
	private String searchName;
	private Country selectedCountry;

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	@PostConstruct
	public void init() {
		countries = getAll();
		country = new Country();
	}

	public List<Country> getAll() {
		Session session = null;
		List<Country> countries = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			countries = session.createQuery("from Country", Country.class).list();

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

	public void saveCountry() {
		Session session = null;
		Transaction transaction = null;

		try {

			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			session.save(country);

			transaction.commit();

			countries = getAll();

			country = new Country();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Country added successfully!"));
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

	public void searchCountryByName() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Query to search country by name, ignoring case
			String queryString = "from Country where lower(country_name) like :name";
			Query<Country> query = session.createQuery(queryString, Country.class);
			query.setParameter("name", "%" + searchName.toLowerCase() + "%");
			countries = query.getResultList();

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

	public void resetSearch() {
		searchName = ""; // Clear the search field
		countries = getAll(); // Reset to show all countries
	}

	public void deleteCountry(long country_id) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Fetch the country to delete using the country_id
			Country countryToDelete = session.get(Country.class, country_id);

			if (countryToDelete != null) {
				session.delete(countryToDelete);
				transaction.commit();

				// Refresh the list of countries after deletion
				countries = getAll();

				// Access StateBean from FacesContext and refresh states
				StateBean stateBean = (StateBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.get("stateBean");
				if (stateBean != null) {
					stateBean.refreshStates();
				}

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Country deleted successfully!"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Country not found."));
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete country."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void refreshCountries() {
		countries = getAll();
	}

	public void loadCountryForEdit(long countryId) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			selectedCountry = session.get(Country.class, countryId);
			if (selectedCountry == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Country not found."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	// Method to update the country details
	public void updateCountry() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			session.update(selectedCountry);

			transaction.commit();

			// Refresh the list of countries after update
			countries = getAll();
			selectedCountry = null; // Reset selected country

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Country updated successfully!"));
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update country."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
