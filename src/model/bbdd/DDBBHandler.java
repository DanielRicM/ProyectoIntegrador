package model.bbdd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class DDBBHandler<T extends Identifiable> implements DataHandler<T>, AutoCloseable {

	private Connection connection;

	public DDBBHandler(String database) throws ClassNotFoundException, SQLException {
		this.connection = getConnection(database);
	}

	private Connection getConnection(String database) throws ClassNotFoundException, SQLException {
		String driver = "com.mysql.cj.jdbc.Driver";
		String hostname = "localhost";
		String port = "3306";
		String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
		String username = "root";
		String password = "root";
		Class.forName(driver);
		System.out.println(url);
		return DriverManager.getConnection(url, username, password);

	}

	@Override
	public Map<Integer, T> readObjects(){
		return null;
	}

	@Override
	public T readObject(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeObjects(Map<Integer, T> map, boolean overwrite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeObject(T newObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteObject(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyObject(int id, T newObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
