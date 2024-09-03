package todo.todo;

import java.io.Serializable;
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
public class StateBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private State state;
	private List<State> states;
	private String selectedCountryCode;
	private String selectedCountryName;
	private String searchStateName;

	public String getSearchStateName() {
		return searchStateName;
	}

	public void setSearchStateName(String searchStateName) {
		this.searchStateName = searchStateName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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

	@PostConstruct
	public void init() {
		states = getAll();
		state = new State();
	}

	// Retrieve Data
	public List<State> getAll() {
		Session session = null;
		List<State> states = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query<State> query = session.createQuery("select s from State s join fetch s.country", State.class);
			states = query.list();

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

		return states;
	}

	public void onCountryCodeChange(AjaxBehaviorEvent event) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			Query<Country> query = session.createQuery("from Country where country_code = :country_code",
					Country.class);
			query.setParameter("country_code", selectedCountryCode);
			Country country = query.uniqueResult();

			if (country != null) {
				selectedCountryName = country.getCountry_name();
				state.setCountry(country);
			} else {
				selectedCountryName = "";
			}

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
	}

	public void saveState() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			session.save(state);

			transaction.commit();

			states = getAll();

			state = new State();
			selectedCountryCode = null;
			selectedCountryName = null;

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "State added successfully!"));

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save state!"));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void searchStatesByName() {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String queryString = "from State where lower(state_name) like :name";
			Query<State> query = session.createQuery(queryString, State.class);
			query.setParameter("name", "%" + searchStateName.toLowerCase() + "%");
			states = query.list();

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
		searchStateName = "";
		states = getAll();
	}

	public void deleteState(long state_id) {
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			// Fetch the state by state_id
			State stateToDelete = session.get(State.class, state_id);
			if (stateToDelete != null) {
				// Delete associated cities first
				String deleteCitiesQuery = "delete from City where state.state_id = :state_id";
				Query<?> query = session.createQuery(deleteCitiesQuery);
				query.setParameter("state_id", state_id);
				query.executeUpdate();

				// Now delete the state
				session.delete(stateToDelete);

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("State and its cities deleted successfully!"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("State not found."));
			}

			transaction.commit();

			// Refresh state list
			states = getAll();

			// Access CityBean from FacesContext and refresh cities
			CityBean cityBean = (CityBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("cityBean");
			if (cityBean != null) {
				cityBean.refreshCities();
			}

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error deleting state."));
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void refreshStates() {
		states = getAll();
	}
	public void prepareEditState(long stateId) {
	    Session session = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();

	        state = session.get(State.class, stateId);
	        if (state != null) {
	            selectedCountryCode = state.getCountry().getCountry_code();
	            selectedCountryName = state.getCountry().getCountry_name();
	        }

	        session.getTransaction().commit();

	    } catch (Exception e) {
	        if (session != null) {
	            session.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load state details!"));
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	}
	public void updateState() {
	    Session session = null;
	    Transaction transaction = null;

	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        transaction = session.beginTransaction();

	        session.update(state);

	        transaction.commit();

	        // Refresh state list
	        states = getAll();

	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "State updated successfully!"));

	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update state!"));
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	}
	
}
