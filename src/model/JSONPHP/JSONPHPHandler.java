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
	private String url;
	private String table;
	private String clazz;
	private ObjFactory<Identifiable> factory;

	public JSONPHPHandler(String table, String clazz, ObjFactory<Identifiable> factory) {
		requests = new ApiRequests();
		this.table = table;
		this.clazz = clazz;
		this.factory = factory;
	}

	@Override
	public Map<Integer, Identifiable> readObjects() {
		Map<Integer, Identifiable> map = new HashMap<>();

		try {
			String url = SERVER_PATH + GET_ + table.toUpperCase();

			String response = requests.getRequest(url);
			JSONObject answer = (JSONObject) JSONValue.parse(response.toString());

			if (answer == null) {
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else {
				String state = (String) answer.get("estado");
				if (state.equals("ok")) {
					JSONArray array = (JSONArray) answer.get(table);// GENERALIZAR raiz

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

					System.exit(-1);

				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");
			e.printStackTrace();
			System.exit(-1);
		}

		return map;
	}

	@Override
	public Identifiable readObject(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeObjects(Map<Integer, Identifiable> map, boolean overwrite) {
		
		for(Identifiable newObject : map.values()) {
			try {
				JSONObject object= factory.toJSONObject(newObject);
				JSONObject objPetition = new JSONObject();
	;

				// Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
				// Lo transformamos a string y llamamos al
				// encargado de peticiones para que lo envie al PHP

				objPetition.put("peticion", "add");
				objPetition.put("objectAdd", object);
				
				String json = objPetition.toJSONString();

				String url = SERVER_PATH + SET_PLAYER;

				//System.exit(-1);

				String response = requests.postRequest(url, json);
				
				

				JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

				if (respuesta == null) { // Si hay alg�n error de parseo (json
											// incorrecto porque hay alg�n caracter
											// raro, etc.) la respuesta ser� null
					System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
					System.exit(-1);
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

						System.exit(-1);

					}
				}
			} catch (Exception e) {
				System.out.println(
						"Excepcion desconocida. Traza de error comentada en el m�todo 'annadirJugador' de la clase JSON REMOTO");
				// e.printStackTrace();
				System.out.println("Fin ejecuci�n");
				System.exit(-1);
			}
		}

	}

	@Override
	public void writeObject(Identifiable newObject) {
		try {
			JSONObject object= factory.toJSONObject(newObject);
			JSONObject objPetition = new JSONObject();
;

			// Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
			// Lo transformamos a string y llamamos al
			// encargado de peticiones para que lo envie al PHP

			objPetition.put("peticion", "add");
			objPetition.put("objectAdd", object);
			
			String json = objPetition.toJSONString();

			String url = SERVER_PATH + SET_PLAYER;

			//System.exit(-1);

			String response = requests.postRequest(url, json);
			
			

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay alg�n error de parseo (json
										// incorrecto porque hay alg�n caracter
										// raro, etc.) la respuesta ser� null
				System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
				System.exit(-1);
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

					System.exit(-1);

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el m�todo 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecuci�n");
			System.exit(-1);
		}

	}

	@Override
	public void deleteObject(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyObject(int id, Identifiable newObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
