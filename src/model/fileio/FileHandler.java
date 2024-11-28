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

	public FileHandler(File file) throws IOException {
		this.file = file;
		this.map = new HashMap<>(); // This ensures the map is never null
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
			throw new IllegalArgumentException("Object with ID " + id + " not found.");
		}
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
	public void modifyObject(int id, Identifiable newObject) {
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
