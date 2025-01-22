package model.basex;

import model.factory.ObjFactory;
import model.fileio.XMLFileHandler;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.List;

import java.io.*;
import java.util.Map;

public class BaseXHandler<T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

    private Context context;
    private String clazz;
    private ObjFactory<Identifiable> factory;

    public BaseXHandler(String clazz, ObjFactory<Identifiable> factory) {
        try {
            context = new Context();
            this.clazz = clazz.toLowerCase();
            this.factory = factory;
            openDB();
        } catch (BaseXException e) {
            try {
                createDB();
            } catch (BaseXException ex) {
                //TODO
                System.out.println(ex);
            }
        }
    }

    private void createDB() throws BaseXException {
        String path = "./files/"+clazz+".xml";
        new CreateDB(clazz, path).execute(context);
    }

    private void openDB() throws BaseXException {
        new Open(clazz).execute(context);
    }

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> objectMap = new HashMap<>();
        try {
            String query = "/objects";
            String result = new XQuery(query).execute(context);
            InputStream fichero = new ByteArrayInputStream(result.getBytes());
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(fichero);
            Element rootElement = document.getRootElement();

            List<Element> nodeList = rootElement.getChildren();

            for (Element objectElement : nodeList) {
                Identifiable object = factory.create(objectElement);
                int id = object.getId();
                objectMap.put(id, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectMap;
    }

    @Override
    public Identifiable readObject(int id) {
        try {
            String query = "//" + clazz + "[@id=" + id + "]";
            String result = new XQuery(query).execute(context);
            InputStream fichero = new ByteArrayInputStream(result.getBytes());
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(fichero);
            Element rootElement = document.getRootElement();
            return factory.create(rootElement);
        } catch (Exception e) {
            System.out.println("Error Basex readOne"+e);
            return null;
        }
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
        try {
            XMLOutputter xmlOut = new XMLOutputter();
            for (Identifiable object : map.values()) {
                Element objectElement = factory.toXML(object);
                String formatted = xmlOut.outputString(objectElement);
                new XQuery("insert node " + formatted + " into /objects").execute(context);
            }
        } catch (BaseXException e) {
            System.out.println("Error Basex writeAll"+e);
        }
    }

    @Override
    public void writeObject(Identifiable newObject) {
        try {
            XMLOutputter xmlOut = new XMLOutputter();
            Element objectElement = factory.toXML(newObject);
            String formatted = xmlOut.outputString(objectElement);
            new XQuery("insert node " + formatted + " into /objects").execute(context);
        } catch (Exception e) {
            System.out.println("Error Basex writeOne"+e);
        }
    }

    @Override
    public void deleteObject(int id) {
        try {
            new XQuery("for $node in /objects/" + clazz + "[@id='" + id + "'] return delete node $node").execute(context);
        } catch (BaseXException e) {
            System.out.println("Error Basex delete"+e);
        }

    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {
        try {
            XMLOutputter xmlOut = new XMLOutputter();
            Element objectElement = factory.toXML(newObject);
            String formatted = xmlOut.outputString(objectElement);
            new XQuery("replace node //" + clazz + "[@id=" + id + "] with " + formatted).execute(context);
        } catch (BaseXException e) {
            System.out.println("Error Basex modify"+e);
        }


    }

    @Override
    public void close() throws IOException {
        context.close();
    }
}
