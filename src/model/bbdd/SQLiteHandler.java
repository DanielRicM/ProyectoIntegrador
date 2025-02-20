package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import main.resources.ConfigManager;
import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class SQLiteHandler<T extends Identifiable> extends DDBBHandler<Identifiable> {

	public SQLiteHandler(String database, ObjFactory<Identifiable> factory, String table) throws ClassNotFoundException, SQLException {
		super(factory, table);
		this.connection = getConnection(database);
		this.stm = connection.createStatement();
	}

	@Override
	protected Connection getConnection(String database) throws ClassNotFoundException, SQLException {
		String url = ConfigManager.getProperty("sqlite.url");
		return DriverManager.getConnection(url);
	}

}
