package model.fileio;

import java.io.File;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import model.interfaces.Identifiable;

public class JSONFileHandler<T extends Identifiable> extends FileHandler<Identifiable> {

	public JSONFileHandler(File file) throws IOException {
		super(file);
		this.map.putAll(initialReadObjects());
	}

	@Override
	protected Map<Integer, Identifiable> initialReadObjects() throws IOException {
		Map<Integer, Identifiable> objectMap = new HashMap<>();
		if (file.length() == 0) {
			return objectMap;
		}
		
		return objectMap;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		
	}

}

