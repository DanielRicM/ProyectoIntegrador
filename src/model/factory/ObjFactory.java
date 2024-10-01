package model.factory;

import org.jdom2.Element;

import model.interfaces.Identifiable;

public interface ObjFactory<T extends Identifiable> {

	T create(String line);

	String toCSV(T object);

	T create(Element rootElement);

	Element toXML(T object);

}