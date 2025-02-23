package model.jsonphp;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import resources.ConfigManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliaries.ApiRequests;
import model.factory.ObjFactory;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class JSONPHPHandler<T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

    private final ApiRequests requests;
    private final String table;
    private final ObjFactory<Identifiable> factory;
    private final String serverPath;

    public JSONPHPHandler(String table, ObjFactory<Identifiable> factory) {
        requests = new ApiRequests();
        this.table = table;
        serverPath = ConfigManager.getProperty("jsonphp.serverpath");
        this.factory = factory;
    }

    @Override
    public Map<Integer, Identifiable> readObjects() {
        Map<Integer, Identifiable> map = new HashMap<>();
        try {
            String url = serverPath + table + ".php";
            String response = requests.getRequest(url);
            JSONObject answer = (JSONObject) JSONValue.parse(response);
            if (answer == null) {
                System.out.println("Error: No response from the server.");
                return map;
            }
            String state = (String) answer.get("estado");
            if (!state.equals("ok")) {
                System.out.println("Error: request is not valid.");
                return map;
            }
            JSONArray array = (JSONArray) answer.get(table);
            if (array.isEmpty()) {
                return map;
            }
            for (Object o : array) {
                JSONObject row = (JSONObject) o;
                Identifiable newObject = factory.create(row);
                map.put(newObject.getId(), newObject);
            }
        } catch (Exception e) {
            System.out.println("Error making the request.");
        }
        return map;

    }

    @Override
    public Identifiable readObject(int id) {
        try {
            String url = serverPath + table + ".php?id=" + id;
            String response = requests.getRequest(url);
            JSONObject answer = (JSONObject) JSONValue.parse(response);
            if (answer == null) {
                System.out.println("Error: No response from the server.");
                return null;
            }
            String state = (String) answer.get("estado");
            if (!state.equals("ok")) {
                System.out.println("Error: request is not valid.");
                return null;
            }
            JSONArray array = (JSONArray) answer.get(table);
            if (array.isEmpty()) {
                return null;
            }
            JSONObject row = (JSONObject) array.getFirst();
            return factory.create(row);
        } catch (Exception e) {
            System.out.println("Error making the request.");
            return null;
        }
    }

    @Override
    public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
        try {
            JSONArray list = new JSONArray();
            for (Identifiable newObject : map.values()) {
                JSONObject object = factory.toJSONObject(newObject);
                list.add(object);
            }
            JSONObject objPetition = new JSONObject();
            objPetition.put("peticion", "add");
            objPetition.put("objectAdd", list);
            String json = objPetition.toJSONString();
            String url = serverPath + table + ".php";
            requests.postRequest(url, json);
        } catch (IOException e) {
            System.out.println("Error making the request.");
        }
    }

    @Override
    public void writeObject(Identifiable newObject) {
        try {
            JSONArray list = new JSONArray();
            JSONObject object = factory.toJSONObject(newObject);
            JSONObject objPetition = new JSONObject();
            list.add(object);
            objPetition.put("peticion", "add");
            objPetition.put("objectAdd", list);
            String json = objPetition.toJSONString();
            String url = serverPath + table + ".php";
            requests.postRequest(url, json);
        } catch (Exception e) {
            System.out.println("Error making the request.");
        }
    }

    @Override
    public void deleteObject(int id) {
        try {
            String url = serverPath + table + ".php?id=" + id;
            requests.deleteRequest(url);
        } catch (Exception e) {
            System.out.println("Error making the request.");
        }
    }

    @Override
    public void modifyObject(int id, Identifiable newObject) {
        try {
            JSONArray list = new JSONArray();
            JSONObject object = factory.toJSONObject(newObject);
            JSONObject objPetition = new JSONObject();
            list.add(object);
            objPetition.put("peticion", "add");
            objPetition.put("objectAdd", list);
            String json = objPetition.toJSONString();
            String url = serverPath + table + ".php?id=" + id;
            requests.putRequest(url, json);
        } catch (Exception e) {
            System.out.println("Error Making the resquest.");
        }
    }

    @Override
    public void close() {
        //Does nothing
    }
}
