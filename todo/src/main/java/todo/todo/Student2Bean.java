package todo.todo;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
public class Student2Bean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Student2 student = new Student2();
	private Student2 selectedStudent;
	private List<Student2> students;
	private ArrayList<String> allBranches;
	private List<Country> countries;
	private List<State> states;
	private Long selectedCountryId;
	private String countryName;
	private String stateName;
	private String cityName;
	private Long selectedStateId;
	private List<City> cities;
	private Long selectedCityId;
	private String searchTerm;
	private Student2 studentToSave = new Student2();

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Student2 getStudentToSave() {
		return studentToSave;
	}

	public void setStudentToSave(Student2 studentToSave) {
		this.studentToSave = studentToSave;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Long getSelectedCityId() {
		return selectedCityId;
	}

	public void setSelectedCityId(Long selectedCityId) {
		this.selectedCityId = selectedCityId;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public Long getSelectedCountryId() {
		return selectedCountryId;
	}

	public void setSelectedCountryId(Long selectedCountryId) {
		this.selectedCountryId = selectedCountryId;
	}

	public Long getSelectedStateId() {
		return selectedStateId;
	}

	public void setSelectedStateId(Long selectedStateId) {
		this.selectedStateId = selectedStateId;
	}

	public Student2 getStudent() {
		return student;
	}

	public void setStudent(Student2 student) {
		this.student = student;
	}

	public Student2 getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student2 selectedStudent) {
		this.selectedStudent = selectedStudent;
		if (selectedStudent != null) {
			this.selectedCountryId = selectedStudent.getCountry().getCountry_id();
			this.selectedStateId = selectedStudent.getState().getState_id();
			this.selectedCityId = selectedStudent.getCity().getCity_id();

			this.states = loadStatesByCountry(selectedCountryId);
			this.cities = loadCitiesByState(selectedStateId);
		} else {
			this.selectedCountryId = null;
			this.selectedStateId = null;
			this.selectedCityId = null;
			this.states = new ArrayList<>();
			this.cities = new ArrayList<>();
		}
	}

	public List<Student2> getStudents() {
		return students;
	}

	public void setStudents(List<Student2> students) {
		this.students = students;
	}

	public ArrayList<String> getAllBranches() {
		return allBranches;
	}

	public void setAllBranches(ArrayList<String> allBranches) {
		this.allBranches = allBranches;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@PostConstruct
	public void init() {
		students = getAll();
		allBranches = new ArrayList<>();
		allBranches.add("CSE");
		allBranches.add("ECE");
		allBranches.add("MECHANICAL");
		allBranches.add("CIVIL");
		allBranches.add("CHEMICAL");
		allBranches.add("EEE");
		allBranches.add("IT");
		countries = loadCountries();

	}

	public void calculateAge(AjaxBehaviorEvent event) {
		if (studentToSave.getDob() != null) {
			// Convert Date to LocalDate
			LocalDate dob = studentToSave.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate today = LocalDate.now();
			// Calculate age
			Period period = Period.between(dob, today);
			studentToSave.setAge(period.getYears());
		} else {
			studentToSave.setAge(0); // Set age to 0 if DOB is not set
		}
	}

	public void updateCalculateAge(AjaxBehaviorEvent event) {
		if (selectedStudent.getDob() != null) {
			// Convert Date to LocalDate
			LocalDate dob = selectedStudent.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate today = LocalDate.now();
			// Calculate age
			Period period = Period.between(dob, today);
			selectedStudent.setAge(period.getYears());
		} else {
			selectedStudent.setAge(0); // Set age to 0 if DOB is not set
		}
	}

	public List<Student2> getAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			List<Student2> students = session.createQuery("from Student2", Student2.class).list();
			session.getTransaction().commit();
			return students;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void deleteStudent(Student2 studentToDelete) {
		if (studentToDelete == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Student not selected for deletion"));
			return;
		}

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			Student2 student = session.get(Student2.class, studentToDelete.getId());
			if (student != null) {
				session.delete(student);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student deleted successfully"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Student not found"));
			}

			transaction.commit();
			students = getAll();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"An error occurred while deleting the student"));
		}
	}

	public void searchStudentByName() {
		if (searchTerm != null && !searchTerm.trim().isEmpty()) {
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				session.beginTransaction();

				String searchName = searchTerm.toLowerCase();
				Query<Student2> query = session.createQuery("FROM Student2 WHERE LOWER(name) LIKE :searchName",
						Student2.class);
				query.setParameter("searchName", "%" + searchName + "%");
				students = query.list();
				session.getTransaction().commit();

				if (students.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Info", "No students found with the name: " + searchTerm));
				}
				searchTerm = null;

			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "An error occurred while searching for students"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Search term cannot be empty"));
		}
	}

	public void resetSearch() {
		students = getAll();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Search reset and all students displayed"));
	}

	public void onCountryChange(AjaxBehaviorEvent event) {

		if (selectedCountryId != null) {
			states = loadStatesByCountry(selectedCountryId);
			if (states == null || states.isEmpty()) {
				System.out.println("No states found for country ID: " + selectedCountryId);
			}
		} else {
			states = null;
		}
	}

	private List<Country> loadCountries() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Country> query = session.createQuery("FROM Country", Country.class);
			return query.list();
		}
	}

	private List<State> loadStatesByCountry(Long country_id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<State> query = session.createQuery("FROM State WHERE country_id = :countryId", State.class);
			query.setParameter("countryId", country_id);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<City> loadCitiesByState(Long stateId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<City> query = session.createQuery("FROM City WHERE state_id = :stateId", City.class);
			query.setParameter("stateId", stateId);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void onStateChange(AjaxBehaviorEvent event) {

		if (selectedStateId != null) {
			cities = loadCitiesByState(selectedStateId);
			if (cities == null || cities.isEmpty()) {
				System.out.println("No cities found for state ID: " + selectedStateId);
			}
		} else {
			cities = null;
		}
	}

	public void saveStudent() {
		validateStudentData();

		if (studentToSave.getName() == null || studentToSave.getName().trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Name cannot be null or empty"));
			return; // Exit the method if validation fails
		}

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			// Fetch and set the Country, State, and City objects based on their IDs
			if (selectedCountryId != null) {
				Country country = session.get(Country.class, selectedCountryId);
				if (country != null) {
					studentToSave.setCountry(country);
				}
			}

			if (selectedStateId != null) {
				State state = session.get(State.class, selectedStateId);
				if (state != null) {
					studentToSave.setState(state);
				}
			}

			if (selectedCityId != null) {
				City city = session.get(City.class, selectedCityId);
				if (city != null) {
					studentToSave.setCity(city);
				}
			}

			session.save(studentToSave);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student added successfully"));

			transaction.commit();
			students = getAll(); // Refresh the student list
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"An error occurred while saving the student"));
		}
	}

	public void validateStudentData() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (studentToSave.getName() == null || studentToSave.getName().trim().isEmpty()) {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Name cannot be null or empty"));
			facesContext.validationFailed(); // Mark validation as failed
		}
		// Add more validation checks as needed
	}

	public void updateStudent() {
		if (selectedStudent == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No student selected for updating"));
			return;
		}

		if (selectedStudent.getName() == null || selectedStudent.getName().trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Name cannot be null or empty"));
			return;
		}

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();

			// Fetch and set the Country, State, and City objects based on their IDs
			if (selectedCountryId != null) {
				Country country = session.get(Country.class, selectedCountryId);
				if (country != null) {
					selectedStudent.setCountry(country);
				}
			}

			if (selectedStateId != null) {
				State state = session.get(State.class, selectedStateId);
				if (state != null) {
					selectedStudent.setState(state);
				}
			}

			if (selectedCityId != null) {
				City city = session.get(City.class, selectedCityId);
				if (city != null) {
					selectedStudent.setCity(city);
				}
			}

			// Update the student record in the database
			session.update(selectedStudent);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student updated successfully"));

			transaction.commit();
			students = getAll(); // Refresh the student list
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"An error occurred while updating the student"));
		}
	}

	public void refreshStudents() {
		students = getAll();
	}
	

}