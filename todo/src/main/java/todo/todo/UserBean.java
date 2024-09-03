package todo.todo;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.management.Query;

import org.hibernate.Session;

@Named
@SessionScoped
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private List<User> userss;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserss() {
		return userss;
	}

	public void setUserss(List<User> userss) {
		this.userss = userss;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@PostConstruct
	public void init() {

		userss = getAllUsers();

	}

	public List<User> getAllUsers() {
		Session session = null;
		List<User> users = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			users = session.createQuery("from User", User.class).list();

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

		return users;
	}

}
