package model.fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;
import model.interfaces.TextSerializable;

public class TextFileHandler<T> extends FileHandler<T> {

	private ObjFactory<T> factory;

	public TextFileHandler(File file, ObjFactory<T> factory) throws IOException {
		super(file);
		this.factory = factory;
	}

	@Override
	protected Map<Integer, T> initialReadObjects() throws IOException {
		Map<Integer, T> objectMap = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				T object = factory.create(line);
				if (object instanceof Identifiable) {
					objectMap.put(((Identifiable) object).getId(), object);
				}
			}
		} catch (IOException e) {
			throw new IOException("Error reading object from file", e);
		}
		return objectMap;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (T object : map.values()) {
				if (object instanceof TextSerializable) {
					writer.write(((TextSerializable) object).toText());
					writer.newLine();
				} else {
					throw new IllegalArgumentException("Object does not implement TextSerializable");
				}
			}
		}
	}

}