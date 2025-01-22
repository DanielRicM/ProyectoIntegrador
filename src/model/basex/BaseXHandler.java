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

import java.util.HashMap;
import java.util.List;

import java.io.*;
import java.util.Map;

public class BaseXHandler<T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

    Context context;
    String clazz;
    ObjFactory<Identifiable> factory;
    File file;

    public BaseXHandler(String clazz, ObjFactory<Identifiable> factory){
        try{
            context = new Context();
            this.clazz = clazz;
            this.factory = factory;
            openDB();
        } catch (BaseXException e) {
            try{
                createDB();
            } catch (BaseXException ex) {
                //TODO
            }
        }
    }

    private void createDB() throws BaseXException {
        file = new File(clazz+".xml");
        new CreateDB(clazz, clazz+".xml").execute(context);
    }

    private void openDB() throws BaseXException {
        new Open(clazz).execute(context);
    }

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> objectMap = new HashMap<>();
        try{
            String query = "//"+clazz;
            String result = new XQuery(query).execute(context);
            InputStream fichero = new ByteArrayInputStream(result.getBytes());
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(fichero);
            Element rootElement = document.getRootElement();

            List<Element> nodeList = rootElement.getChildren();

            for(Element objectElement : nodeList){
                Identifiable object = factory.create(objectElement);
                int id = object.getId();
                objectMap.put(id, object);
            }
        } catch(Exception e) {
            //TODO
        }
        return objectMap;
    }

    @Override
    public Identifiable readObject(int id) {
        //TODO
        return null;
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
        Element rootElement = new Element("objects");
        Document document = new Document(rootElement);
        
    }

    @Override
    public void writeObject(Identifiable newObject) {

    }

    @Override
    public void deleteObject(int id) {

    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {

    }

    @Override
    public void close() throws IOException {
        context.close();
    }
}
