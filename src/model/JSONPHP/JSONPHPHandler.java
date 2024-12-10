package model.JSONPHP;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliaries.ApiRequests;
import model.factory.ObjFactory;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;

public class JSONPHPHandler<T extends Identifiable> implements DataHandler<Identifiable>, Closeable {

	private ApiRequests requests;
	private String table;
	private ObjFactory<Identifiable> factory;
	private static final String SERVER_PATH = "http://localhost/Irene/ProjectJSONServer/";

	public JSONPHPHandler(String table, ObjFactory<Identifiable> factory) {
		requests = new ApiRequests();
		this.table = table;
		this.factory = factory;
	}

	@Override
	public Map<Integer, Identifiable> readObjects() {
		Map<Integer, Identifiable> map = new HashMap<>();

		try {
			String url = SERVER_PATH + table + ".php";

			String response = requests.getRequest(url);
			JSONObject answer = (JSONObject) JSONValue.parse(response.toString());

			if (answer == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
			} else {
				String state = (String) answer.get("estado");
				if (state.equals("ok")) {
					JSONArray array = (JSONArray) answer.get(table);

					if (array.size() > 0) {
						for (int i = 0; i < array.size(); i++) {
							JSONObject row = (JSONObject) array.get(i);
							Identifiable newObject = factory.create(row);
							map.put(newObject.getId(), newObject);
						}

						System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
						System.out.println();

					} else { // El array de jugadores est� vac�o
						System.out.println("Acceso JSON Remoto - No hay datos que tratar");
						System.out.println();
					}

				} else { // Hemos recibido el json pero en el estado se nos
					// indica que ha habido alg�n error

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) answer.get("error"));
					System.out.println("Consulta: " + (String) answer.get("query"));

				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");
		}

		return map;
	}

	@Override
	public Identifiable readObject(int id) {
		Identifiable newObject;
		try {
			String url = SERVER_PATH + table + ".php?id=" + String.valueOf(id);

			String response = requests.getRequest(url);
			JSONObject answer = (JSONObject) JSONValue.parse(response.toString());

			if (answer == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
			} else {
				String state = (String) answer.get("estado");
				if (state.equals("ok")) {
					JSONArray array = (JSONArray) answer.get(table);// GENERALIZAR raiz

					if (array.size() > 0) {
						JSONObject row = (JSONObject) array.get(0);
						newObject = factory.create(row);
						return newObject;

					} else {
						System.out.println("Acceso JSON Remoto - No hay datos que tratar");
						System.out.println();
					}

				} else {

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) answer.get("error"));
					System.out.println("Consulta: " + (String) answer.get("query"));

				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {

		JSONArray list = new JSONArray();

		for (Identifiable newObject : map.values()) {
			JSONObject object = factory.toJSONObject(newObject);
			list.add(object);
		}

		JSONObject objPetition = new JSONObject();
		objPetition.put("peticion", "add");
		objPetition.put("objectAdd", list);

		String json = objPetition.toJSONString();
		String url = SERVER_PATH + table + ".php";

		String response;
		try {
			response = requests.postRequest(url, json);
			System.out.println(response);
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
			if (respuesta == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
			} else {
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {
					System.out.println("Almacenado estudiante enviado por JSON Remoto");
				} else {

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

				}
			}
		} catch (IOException e) {
			System.out.println(e);
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
			String url = SERVER_PATH + table + ".php";
			System.out.println(json);
			String response = requests.postRequest(url, json);
			System.out.println(response);
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido alg�n error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Override
	public void deleteObject(int id) {
		Identifiable newObject;
		try {
			String url = SERVER_PATH + table + ".php?id=" + String.valueOf(id);

			String response = requests.deleteRequest(url);
			JSONObject answer = (JSONObject) JSONValue.parse(response.toString());

			if (answer == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
			} else {
				String state = (String) answer.get("estado");
				if (state.equals("ok")) {
					System.out.println("El json recibido es correcto. Objeto eliminado");
				} else {
					System.out.println("Acceso JSON Remoto - No hay datos que tratar");
					System.out.println();
				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");
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
			String url = SERVER_PATH + table + ".php?id="+String.valueOf(id);
			String response = requests.putRequest(url, json);
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
			} else {
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else {

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
