package model.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.factory.ObjFactory;
import model.interfaces.Identifiable;

public class XMLFileHandler<T extends Identifiable> extends FileHandler<T> {

	private ObjFactory<T> factory;

	public XMLFileHandler(File file, ObjFactory<T> factory) throws IOException {
		super(file);
		this.factory = factory;
		this.map.putAll(initialReadObjects());
	}

	@Override
	protected Map<Integer, T> initialReadObjects() throws IOException {
		Map<Integer, T> studentMap = new HashMap<>();
		if (file.length() == 0) {
			return studentMap;
		}

		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element rootElement = document.getRootElement();
			List<Element> studentList = rootElement.getChildren();
			for (Element studentElement : studentList) {
				T object = factory.create(studentElement);
				int id = object.getId();
				studentMap.put(id, object);
			}

		} catch (Exception e) {
			throw new IOException("Error parsing XML file", e);
		}
		return studentMap;
	}

	@Override
	protected void finalWriteObjects() throws IOException {
		Element rootElement = new Element("objects");
		Document document = new Document(rootElement);

		for (T object : map.values()) {
			Element objectElement = factory.toXML(object);
			rootElement.addContent(objectElement);
		}

		try (FileOutputStream fos = new FileOutputStream(file)) {
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
			xmlOutputter.output(document, fos);
		}
	}
}
