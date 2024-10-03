package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccessDB {

	Connection connection;
	
	public AccessDB(String database) {
				String driver = "com.mysql.cj.jdbc.Driver";
				String hostname = "localhost";
				String port = "3306";
				String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
				String username = "root";
				String password = "root";
				
				try {
					Class.forName(driver);
					System.out.println(url);
					connection = DriverManager.getConnection(url, username, password);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}

	}
}
