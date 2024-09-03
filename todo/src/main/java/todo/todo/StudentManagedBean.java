package todo.todo;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named
@SessionScoped
public class StudentManagedBean implements Serializable {

	private ArrayList<String> allBranches;
	private Map<String, String> allCountries = new HashMap<>();
	private Map<String, Map<String, String>> data = new HashMap<>();
	private Map<String, String> states = new HashMap<>();
	private Map<String, String> cities = new HashMap<>();

	private String selectedBranch;
	private String selectedCountry;
	private String selectedState;
	private String selectedCity;
	private Long id;

	private ArrayList<Student> students;
	private Student student = new Student();
	private Student selectedStudent;
	private ArrayList<Student> searchedStudents = new ArrayList<Student>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<String> getAllBranches() {
		return allBranches;
	}

	public void setAllBranches(ArrayList<String> allBranches) {
		this.allBranches = allBranches;
	}

	public Map<String, String> getAllCountries() {
		return allCountries;
	}

	public void setAllCountries(Map<String, String> allCountries) {
		this.allCountries = allCountries;
	}

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}

	public Map<String, String> getStates() {
		return states;
	}

	public void setStates(Map<String, String> states) {
		this.states = states;
	}

	public Map<String, String> getCities() {
		return cities;
	}

	public void setCities(Map<String, String> cities) {
		this.cities = cities;
	}

	public String getSelectedBranch() {
		return selectedBranch;
	}

	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}

	public String getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(String selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public String getSelectedState() {
		return selectedState;
	}

	public void setSelectedState(String selectedState) {
		this.selectedState = selectedState;
	}

	public String getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(String selectedCity) {
		this.selectedCity = selectedCity;
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public ArrayList<Student> getSearchedStudents() {
		return searchedStudents;
	}

	public void setSearchedStudents(ArrayList<Student> searchedStudents) {
		this.searchedStudents = searchedStudents;
	}

	@PostConstruct
	public void init() {
		students = getAllStudents();
		populateData();

		allBranches = new ArrayList<String>();
		allBranches.add("CSE");
		allBranches.add("EEE");
		allBranches.add("MECH");
		allBranches.add("CIVIL");

	}

	private void populateData() {
		// Populate countries
		allCountries.put("USA", "USA");
		allCountries.put("IND", "IND");
		allCountries.put("UK", "UK");

		// Populate states and cities for each country
		Map<String, String> map1 = new HashMap<>();

		// USA states
		map1.put("California", "California");
		map1.put("Texas", "Texas");
		map1.put("New York", "New York");
		data.put("USA", map1);

		// USA cities
		map1 = new HashMap<>();
		map1.put("Los Angeles", "Los Angeles");
		map1.put("San Francisco", "San Francisco");
		map1.put("San Diego", "San Diego");
		data.put("California", map1);

		map1 = new HashMap<>();
		map1.put("Houston", "Houston");
		map1.put("Dallas", "Dallas");
		map1.put("Austin", "Austin");
		data.put("Texas", map1);

		map1 = new HashMap<>();
		map1.put("New York City", "New York City");
		map1.put("Buffalo", "Buffalo");
		map1.put("Rochester", "Rochester");
		data.put("New York", map1);

		// India states
		map1 = new HashMap<>();
		map1.put("Maharashtra", "Maharashtra");
		map1.put("Karnataka", "Karnataka");
		map1.put("Tamil Nadu", "Tamil Nadu");
		map1.put("Andhra Pradesh", "Andhra Pradesh");
		data.put("IND", map1);

		// India cities
		map1 = new HashMap<>();
		map1.put("Mumbai", "Mumbai");
		map1.put("Pune", "Pune");
		map1.put("Nagpur", "Nagpur");
		data.put("Maharashtra", map1);

		map1 = new HashMap<>();
		map1.put("Bangalore", "Bangalore");
		map1.put("Mysore", "Mysore");
		map1.put("Mangalore", "Mangalore");
		data.put("Karnataka", map1);

		map1 = new HashMap<>();
		map1.put("Chennai", "Chennai");
		map1.put("Coimbatore", "Coimbatore");
		map1.put("Madurai", "Madurai");
		data.put("Tamil Nadu", map1);

		map1 = new HashMap<>();
		map1.put("Vishakapatnam", "Vishakapatnam");
		map1.put("Srikakulam", "Srikakulam");
		map1.put("East Godavari", "East Godavari");
		data.put("Andhra Pradesh", map1);

		// UK states
		map1 = new HashMap<>();
		map1.put("England", "England");
		map1.put("Scotland", "Scotland");
		map1.put("Wales", "Wales");
		data.put("UK", map1);

		// UK cities
		map1 = new HashMap<>();
		map1.put("London", "London");
		map1.put("Manchester", "Manchester");
		map1.put("Liverpool", "Liverpool");
		data.put("England", map1);

		map1 = new HashMap<>();
		map1.put("Edinburgh", "Edinburgh");
		map1.put("Glasgow", "Glasgow");
		map1.put("Aberdeen", "Aberdeen");
		data.put("Scotland", map1);

		map1 = new HashMap<>();
		map1.put("Cardiff", "Cardiff");
		map1.put("Swansea", "Swansea");
		map1.put("Newport", "Newport");
		data.put("Wales", map1);
	}

	public void onCountryChange() {
		if (selectedCountry != null && !selectedCountry.isEmpty()) {

			states = data.get(selectedCountry);
			cities = new HashMap<>();
			selectedState = null;
			selectedCity = null;
		} else {
			states = new HashMap<>();
			cities = new HashMap<>();
			selectedState = null;
			selectedCity = null;
		}

		PrimeFaces.current().ajax().update("studentForm:state");
		PrimeFaces.current().ajax().update("studentForm:city");
	}

	public void onStateChange() {
		if (selectedState != null && !selectedState.isEmpty()) {
			cities = data.get(selectedState);
			selectedCity = null;
		} else {
			cities = new HashMap<>();
			selectedCity = null;
		}

		PrimeFaces.current().ajax().update("studentForm:city");
	}

	private boolean isValidMobileNumber(String mobile) {
		String regex = "^[9876]\\d{9}$";
		return Pattern.matches(regex, mobile);
	}

	public void validateMobileNumber(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		String mobile = (String) value;
		if (!isValidMobileNumber(mobile)) {
			throw new ValidatorException(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Invalid mobile number format."));
		}
	}
	 public void ageCalculator() {
	        if (student.getDob() != null) {
	            Date birthDate = student.getDob();
	            Instant instant = birthDate.toInstant();
	            ZoneId zoneId = ZoneId.systemDefault();
	            LocalDate localDate = instant.atZone(zoneId).toLocalDate();
	            LocalDate today = LocalDate.now();
	            int age = Period.between(localDate, today).getYears();
	            student.setAge(age);
	            System.out.println("Calculated age: " + age);
	        }
	    }
	    
	    public void ageEdit() {
	        if (selectedStudent.getDob() != null) {
	            Date birthDate = selectedStudent.getDob();
	            Instant instant = birthDate.toInstant();
	            ZoneId zoneId = ZoneId.systemDefault();
	            LocalDate localDate = instant.atZone(zoneId).toLocalDate();
	            LocalDate today = LocalDate.now();
	            int age = Period.between(localDate, today).getYears();
	            selectedStudent.setAge(age);
	            System.out.println("Edited age: " + age);
	        }
	    }
	

	public ArrayList<Student> getAllStudents() {
		ArrayList<Student> listOfStudents = new ArrayList<>();
		try (Connection connection = new PostgreTest().getConnection();
				PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM public.student");
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				Student obj = new Student();
				obj.setId(rs.getLong("id"));
				obj.setName(rs.getString("name"));
				obj.setBranch(rs.getString("branch"));
				obj.setDob(rs.getDate("dob"));
				obj.setAge(rs.getInt("age"));
				obj.setGender(rs.getString("gender"));
				obj.setEmail(rs.getString("email"));
				obj.setMobile(rs.getString("mobile"));
				obj.setCountry(rs.getString("country"));
				obj.setState(rs.getString("state"));
				obj.setCity(rs.getString("city"));
				obj.setPincode(rs.getInt("pincode"));

				listOfStudents.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfStudents;
	}

	public void saveStudent() {
		try (Connection connection = new PostgreTest().getConnection()) {
			if (!isValidMobileNumber(student.getMobile())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Validation Error", "Invalid mobile number format."));
				return;
			}

			// Assign selectedCountry, selectedState, selectedCity to student
			student.setCountry(selectedCountry);
			student.setState(selectedState);
			student.setCity(selectedCity);

			Long mobile = Long.parseLong(student.getMobile());

			String sql = "INSERT INTO public.student (name, branch, dob, age, gender, email, mobile, country, state, city, pincode) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, student.getName());
				pstmt.setString(2, student.getBranch());
				pstmt.setDate(3, new java.sql.Date(student.getDob().getTime()));
				pstmt.setInt(4, student.getAge());
				pstmt.setString(5, student.getGender());
				pstmt.setString(6, student.getEmail());
				pstmt.setLong(7, mobile);
				pstmt.setString(8, student.getCountry());
				pstmt.setString(9, student.getState());
				pstmt.setString(10, student.getCity());
				pstmt.setInt(11, student.getPincode());

				pstmt.executeUpdate();
				students = getAllStudents();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student data saved successfully"));
				PrimeFaces.current().executeScript("PF('studentDialog').hide()");
				PrimeFaces.current().ajax().update("studentTable");
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Failed to save student data: " + e.getMessage()));
			e.printStackTrace();
		}
	}

	public void search(String id) {
		searchedStudents.clear();
		try {
			int ids = Integer.valueOf(id);

			Connection connection = new PostgreTest().getConnection();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM student WHERE id = " + ids);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Student obj = new Student();
				obj.setId(rs.getLong("id"));
				obj.setName(rs.getString("name"));
				obj.setBranch(rs.getString("branch"));
				obj.setDob(rs.getDate("dob"));
				obj.setAge(rs.getInt("age"));
				obj.setGender(rs.getString("gender"));
				obj.setEmail(rs.getString("email"));
				obj.setMobile(rs.getString("mobile"));
				obj.setCountry(rs.getString("country"));
				obj.setState(rs.getString("state"));
				obj.setCity(rs.getString("city"));
				obj.setPincode(rs.getInt("pincode"));
				searchedStudents.add(obj);
			}

			students = searchedStudents;
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetSearch() {
		students = getAllStudents();
	}

	public void deleteStudent(Long id) {
		try (Connection connection = new PostgreTest().getConnection();
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM public.student WHERE id = ?")) {
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
			students = getAllStudents(); // Update the student list

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student data deleted successfully"));
			PrimeFaces.current().ajax().update("studentTable");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Failed to delete student data: " + e.getMessage()));
			e.printStackTrace();
		}
	}

	public void editStudent(Student st) {
		selectedStudent = st;
	}

	public void updateStudent() {
		try (Connection connection = new PostgreTest().getConnection()) {
			// Assign selectedCountry, selectedState, selectedCity to selectedStudent
			selectedStudent.setCountry(selectedCountry);
			selectedStudent.setState(selectedState);
			selectedStudent.setCity(selectedCity);

			String sql = "UPDATE public.student SET name=?, branch=?, dob=?, age=?, gender=?, email=?, mobile=?, country=?, state=?, city=?, pincode=? WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, selectedStudent.getName());
				pstmt.setString(2, selectedStudent.getBranch());
				pstmt.setDate(3, new java.sql.Date(selectedStudent.getDob().getTime()));
				pstmt.setInt(4, selectedStudent.getAge());
				pstmt.setString(5, selectedStudent.getGender());
				pstmt.setString(6, selectedStudent.getEmail());
				pstmt.setLong(7, Long.parseLong(selectedStudent.getMobile()));
				pstmt.setString(8, selectedStudent.getCountry());
				pstmt.setString(9, selectedStudent.getState());
				pstmt.setString(10, selectedStudent.getCity());
				pstmt.setInt(11, selectedStudent.getPincode());
				pstmt.setLong(12, selectedStudent.getId());

				pstmt.executeUpdate();
				students = getAllStudents(); // Update the student list

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student data updated successfully"));
				PrimeFaces.current().executeScript("PF('studentDialog').hide()");
				PrimeFaces.current().ajax().update("studentTable");
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"Failed to update student data: " + e.getMessage()));
			e.printStackTrace();
		}
	}


}
