package model.fileio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public abstract class FileHandler<T> implements DataHandler<T>, AutoCloseable {

	protected final File file;
	protected Map<Integer, T> map;

	public FileHandler(File file) throws IOException {
		this.file = file;
		this.map = new HashMap<>(); // This ensures the map is never null
		this.map.putAll(initialReadObjects());
	}

	protected abstract Map<Integer, T> initialReadObjects() throws IOException;

	protected abstract void finalWriteObjects() throws IOException;

	@Override
	public Map<Integer, T> readObjects() {
		return map;
	}

	@Override
	public T readObject(int id) {
		if (map.containsKey(id)) {
			return map.get(id);
		} else {
			throw new IllegalArgumentException("Object with ID " + id + " not found.");
		}
	}

	@Override
	public void writeObjects(Map<Integer, T> map) {
		this.map.putAll(map); // May add boolean for overwrite
	}

	@Override
	public void writeObject(T object) {
		if (object instanceof Identifiable) {
			Integer id = ((Identifiable) object).getId();
			map.put(id, object);
		} else {
			throw new IllegalArgumentException("Object must implement Identifiable");
		}
	}

	@Override
	public void deleteObject(int id) {
		map.remove(id);
	}

	@Override
	public void modifyObject(int id, T newObject) {
		if (map.containsKey(id)) {
			map.put(id, newObject);
		} else {
			throw new IllegalArgumentException("Object with ID " + id + " not found.");
		}
	}

	public void close() throws IOException {
		finalWriteObjects();
	}

	// Considerar implementar una forma de confirmar los cambios:
	// private boolean changesConfirmed = false;
	// ...close(){
	// if(changesConfirmed) finalwriteObjects();
	// }

}
