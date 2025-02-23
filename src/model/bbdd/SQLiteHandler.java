package model.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import resources.ConfigManager;
import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class SQLiteHandler<T extends Identifiable> extends DDBBHandler<Identifiable> {

	public SQLiteHandler(ObjFactory<Identifiable> factory, String table) throws SQLException {
		super(factory, table);
		this.connection = getConnection();
		this.stm = connection.createStatement();
	}

	@Override
	protected Connection getConnection() throws SQLException {
		String url = ConfigManager.getProperty("sqlite.url");
		return DriverManager.getConnection(url);
	}

}
