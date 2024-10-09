package model.bbdd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import model.entities.Student;
import model.factory.ObjFactory;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class DDBBHandler<T extends Identifiable> implements DataHandler<T>, AutoCloseable {

	private Connection connection;
	private String table;
	private ObjFactory<T> factory;
	

	public DDBBHandler(String database,ObjFactory<T> factory) throws ClassNotFoundException, SQLException {
		this.connection = getConnection(database);
		this.table="students";
		this.factory = factory;
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
		Map<Integer,T> map = new HashMap<>();
		
		try {
			String query = "Select * from "+table;
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			
			ResultSetMetaData metaData = rs.getMetaData();
			int nfields = metaData.getColumnCount();
			String[]fields = new String[nfields];
			
			for (int i=0;i<nfields;i++) {
				fields[i]=metaData.getColumnName(i+1);
			}
			
			while(rs.next()) {
				String line=rs.getString(fields[0])+";"+rs.getString(fields[1])+";"+rs.getString(fields[2])+";"+rs.getString(fields[3]);
				T object= factory.create(line);
				map.put(((Identifiable)object).getId(), object);
			}	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return  map;
	}
	
	
	@Override
	public T readObject(int id) {
		
		try {
			String query="Select * from "+table+" where id="+String.valueOf(id);
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int nfields = metaData.getColumnCount();
			String[]fields = new String[nfields];
			
			for (int i=0;i<nfields;i++) {
				fields[i]=metaData.getColumnName(i+1);
			}
		
			String line=rs.getString(fields[0])+";"+rs.getString(fields[1])+";"+rs.getString(fields[2])+";"+rs.getString(fields[3]);
			T object= factory.create(line);
			
			
			
			return object;
			
			
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		
		return null;
	}

	@Override
	public void writeObjects(Map<Integer, T> map, boolean overwrite) {
		
		//Hacer metodo en el factory
		
		for(T student:map.values()) {
			Student student2=(Student)student;
			String query ="Insert into "+table+" values ("+
			String.valueOf(student2.getId())+ ", '"
					+student2.getName()+ ", '"
			+String.valueOf(student2.getAge())
			+ ", '"+student2.getCourse()+") ";
		}
		

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
