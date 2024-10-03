package model.interfaces;

import java.io.IOException;
import java.util.Map;

public interface DataHandler<T extends Identifiable> {

	Map<Integer, T> readObjects();

	T readObject(int id);

	void writeObjects(Map<Integer, T> map);

	void writeObject(T newObject);

	void deleteObject(int id);

	void modifyObject(int id, T newObject);
	
	void close() throws IOException;

}
