package model.fileio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public abstract class FileHandler<T extends Identifiable> implements DataHandler<Identifiable>, AutoCloseable {

	protected final File file;
	protected Map<Integer, Identifiable> map;

	protected FileHandler(File file) {
		this.file = file;
		this.map = new HashMap<>();
	}
	
	protected abstract Map<Integer, Identifiable> initialReadObjects() throws IOException;

	protected abstract void finalWriteObjects() throws IOException;

	@Override
	public Map<Integer, Identifiable> readObjects() {
		return map;
	}

	@Override
	public Identifiable readObject(int id) {
		if (map.containsKey(id)) {
			return map.get(id);
		} else {
			return null;		}
	}

	@Override
	public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
		if(overwrite) {
			this.map = new HashMap<>();
		}
		this.map.putAll(map); // May add boolean for overwrite
	}
	
	@Override
	public void writeObject(Identifiable object) {
			Integer id = ((Identifiable) object).getId();
			map.put(id, object);
	}

	@Override
	public void deleteObject(int id) {
		map.remove(id);
	}

	@Override
	public void modifyObject(int id, Identifiable newObject) {
		if (map.containsKey(id)) {
			map.put(id, newObject);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void close() throws IOException {
		finalWriteObjects();
	}
}
