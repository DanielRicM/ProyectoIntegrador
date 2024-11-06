package model.factory;

import org.jdom2.Element;

import model.interfaces.Identifiable;

public interface ObjFactory<T extends Identifiable> {

	Identifiable create(String line);

	String toCSV(Identifiable object);

	Identifiable create(Element rootElement);

	Element toXML(Identifiable object);
	
	String toQuery(Identifiable Object);
	
	String toUpdateQuery(Identifiable student);

}