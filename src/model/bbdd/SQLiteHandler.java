package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class SQLiteHandler<T extends Identifiable> extends DDBBHandler<T> {

	public SQLiteHandler(String database, ObjFactory<T> factory) throws ClassNotFoundException, SQLException {
		super(database, factory);
		this.connection = getConnection(database);
		this.stm = connection.createStatement();
	}

	@Override
	protected Connection getConnection(String database) throws ClassNotFoundException, SQLException {
		//Class.forName("org.sqlite.JDBC");
		String url = "jdbc:sqlite:" + database;
		return DriverManager.getConnection(url);
	}

}
