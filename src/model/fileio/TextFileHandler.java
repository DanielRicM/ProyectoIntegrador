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

public class TextFileHandler<T extends Identifiable> extends FileHandler<Identifiable> {

	private ObjFactory<Identifiable> factory;

	public TextFileHandler(File file, ObjFactory<Identifiable> factory) throws IOException {
		super(file);
		this.factory = factory;
		this.map.putAll(initialReadObjects());
	}

	@Override
	protected Map<Integer, Identifiable> initialReadObjects() throws IOException {
		Map<Integer, Identifiable> objectMap = new HashMap<>();
		if (file.length() == 0) {
			return objectMap;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Identifiable object = factory.create(line);
				objectMap.put(((Identifiable) object).getId(), object);
			}
		} catch (IOException e) {
			throw new IOException("Error reading object from file", e);
		}
		return objectMap;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Identifiable object : map.values()) {
				writer.write(factory.toCSV(object));
				writer.newLine();
			}
		}
	}

}