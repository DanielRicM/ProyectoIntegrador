package model.fileio;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.interfaces.Identifiable;

public class BinaryFileHandler<T extends Identifiable> extends FileHandler<Identifiable> {

	public BinaryFileHandler(File file) throws IOException {
		super(file);
		this.map.putAll(initialReadObjects());
	}

	@Override
	protected Map<Integer, Identifiable> initialReadObjects() throws IOException {
		Map<Integer, Identifiable> objectMap = new HashMap<>();
		if (file.length() == 0) {
			return objectMap;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			while (true) {
				try {
					Identifiable object = (Identifiable) ois.readObject();
					Integer id = object.getId();
					objectMap.put(id, object);
				} catch (EOFException e) {
					break; // End of file reached
				}
			}
		} catch (ClassNotFoundException e) {
			throw new IOException("Error reading object from file", e);
		}
		return objectMap;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			for (Identifiable object : map.values()) {
				oos.writeObject(object);
			}
		}
	}

}
