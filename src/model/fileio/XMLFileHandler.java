package model.fileio;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class XMLFileHandler<T> extends FileHandler<T> {

	public XMLFileHandler(File file) throws IOException {
		super(file);
	}

	@Override
	protected Map<Integer, T> initialReadObjects() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		// TODO Auto-generated method stub
	}

}
