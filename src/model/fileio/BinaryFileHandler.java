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

public class BinaryFileHandler<T> extends FileHandler<T> {

	public BinaryFileHandler(File file) throws IOException {
		super(file);
	}

	@Override
	protected Map<Integer, T> initialReadObjects() throws IOException {
		Map<Integer, T> objectMap = new HashMap<>();
		if(file.length()==0) {
			return objectMap;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			while (true) {
				try {
					@SuppressWarnings("unchecked")
					T object = (T) ois.readObject();
					// Must implement object factories

					if (object instanceof Identifiable) {
						Integer id = ((Identifiable) object).getId();
						objectMap.put(id, object);
					} else {
						throw new IllegalArgumentException("Object must implement Identifiable");
					}
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
			for (T object : map.values()) {
				oos.writeObject(object);
			}
		}
	}

}
