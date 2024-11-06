package model.interfaces;

import java.io.IOException;
import java.util.Map;

public interface DataHandler<T extends Identifiable> {

	Map<Integer, Identifiable> readObjects();

	Identifiable readObject(int id);

	void writeObjects(Map<Integer, Identifiable> map, boolean overwrite);

	void writeObject(Identifiable newObject);

	void deleteObject(int id);

	void modifyObject(int id, Identifiable newObject);
	
	void close() throws IOException;

}
