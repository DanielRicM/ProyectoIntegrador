package model.MongoDB;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.factory.ObjFactory;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDBHandler <T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

    private MongoCollection<Document> collection;
    private ObjFactory<Identifiable> factory;
    private MongoClient mongoClient;

    public MongoDBHandler(String table, ObjFactory<Identifiable> factory){
        this.factory = factory;

        mongoClient = new MongoClient("localhost",27017);
        MongoDatabase database = mongoClient.getDatabase("adat");

        collection = database.getCollection(table);

    }

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> map = new HashMap<>();
        MongoCursor resultado = collection.find().iterator();

        while (resultado.hasNext()) {
            Document doc = (Document) resultado.next();
            JSONObject json = (JSONObject) JSONValue.parse(doc.toJson());
            Identifiable object = factory.create(json);
            map.put(object.getId(),object);
        }
        return map;
    }

    @Override
    public Identifiable readObject(int id) {
        Document searchQuery = new Document();
        searchQuery.put("id", id);
        MongoCursor<Document> resultado = collection.find(searchQuery).iterator();
        while (resultado.hasNext()) {
            Document doc = (Document) resultado.next();
            JSONObject json = (JSONObject) JSONValue.parse(doc.toJson());
            Identifiable object = factory.create(json);
            return object;
        }
        return null;
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
        List<Document> documents = new ArrayList<>();

        for(Identifiable object : map.values()){
            JSONObject json = factory.toJSONObject(object);
            Document doc =Document.parse(json.toJSONString());
            documents.add(doc);
        }

        collection.insertMany(documents);
    }

    @Override
    public void writeObject(Identifiable newObject) {
        JSONObject json = factory.toJSONObject(newObject);
        Document doc =Document.parse(json.toJSONString());
        collection.insertOne(doc);
    }

    @Override
    public void deleteObject(int id) {
        Document searchQuery = new Document();
        searchQuery.put("id",id);
        collection.deleteOne(searchQuery);
    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {
        Document searchQuery = new Document();
        searchQuery.put("id",id);

        JSONObject json = factory.toJSONObject(newObject);
        Document modified =Document.parse(json.toJSONString());

        Document updateDocument = new Document();
        updateDocument.put("$set",modified);
        collection.updateMany(searchQuery,updateDocument);

    }

    @Override
    public void close() throws IOException {
        mongoClient.close();
    }
}
