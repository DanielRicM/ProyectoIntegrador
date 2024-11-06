package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class MySQLHandler<T extends Identifiable> extends DDBBHandler<Identifiable> {

	public MySQLHandler(String database, ObjFactory<Identifiable> factory, String table) throws ClassNotFoundException, SQLException {
		super(factory, table);
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
