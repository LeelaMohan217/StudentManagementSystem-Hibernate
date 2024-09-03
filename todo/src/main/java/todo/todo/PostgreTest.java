package todo.todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;



public class PostgreTest {
	public static void main(String[] args) {
		PostgreTest obj = new PostgreTest();
		System.out.println(obj.getConnection());
	}

	public Connection getConnection() {

		Connection connection = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "admin123");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

		return connection;
	}
}