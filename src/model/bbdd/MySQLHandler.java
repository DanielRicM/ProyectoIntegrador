package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;

import main.resources.ConfigManager;


public class MySQLHandler<T extends Identifiable> extends DDBBHandler<Identifiable> {


	public MySQLHandler(String database, ObjFactory<Identifiable> factory, String table) throws ClassNotFoundException, SQLException {
		super(factory, table);
		this.connection = getConnection(database);
		this.stm = connection.createStatement();
	}

	@Override
	protected Connection getConnection(String database) throws ClassNotFoundException, SQLException {

		String driver = ConfigManager.getProperty("mysql.driver");
		String username = ConfigManager.getProperty("mysql.username");
		String password = ConfigManager.getProperty("mysql.password");
		String url = ConfigManager.getProperty("mysql.url");


		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

}
