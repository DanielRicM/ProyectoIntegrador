package model.factory;

import org.jdom2.Element;
import org.json.simple.JSONObject;

import model.interfaces.Identifiable;

public interface ObjFactory<T extends Identifiable> {

    Identifiable create(String line);

    String toCSV(Identifiable object);

    Identifiable create(Element rootElement);

    Element toXML(Identifiable object);

    String toQuery(Identifiable object);

    String toUpdateQuery(Identifiable object);

    Identifiable create(JSONObject row);

    JSONObject toJSONObject(Identifiable object);

    String createTableQuery();

}