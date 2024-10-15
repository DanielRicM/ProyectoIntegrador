package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class MySQLHandler<T extends Identifiable> extends DDBBHandler<T> {

	public MySQLHandler(String database, ObjFactory<T> factory) throws ClassNotFoundException, SQLException {
		super(database, factory);
		this.connection = getConnection(database);
		this.stm = connection.createStatement();
	}

	@Override
	protected Connection getConnection(String database) throws ClassNotFoundException, SQLException {
		String driver = "com.mysql.cj.jdbc.Driver";
		String hostname = "localhost";
		String port = "3306";
		String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
		String username = "root";
		String password = "root";
		
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

}
