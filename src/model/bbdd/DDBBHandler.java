package model.bbdd;

import java.util.Map;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;


public  class DDBBHandler<T extends Identifiable> implements DataHandler{
	private AccessDB database;
	
	public DDBBHandler(String database) {
		this.database=new AccessDB(database);
	}

	@Override
	public Map readObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Identifiable readObject(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeObjects(Map map) {
		// TODO Auto-generated method stub
		
	}//??

	@Override
	public void writeObject(Identifiable newObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObject(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyObject(int id, Identifiable newObject) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	

	 
	
}
