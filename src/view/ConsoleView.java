package view;

import java.util.Map;
import java.util.Scanner;

import model.interfaces.Identifiable;

public class ConsoleView implements View {

	static Scanner entrada = new Scanner(System.in);

	public ConsoleView() {
	}

	@Override
	public int askObjectType() {
		System.out.println("¿Qué tipo objeto desea utilizar?\n" + "1-Student\n" + "2-*****\n" + "3-*****");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	@Override
	public int askDataAccessType() {
		System.out.println("¿Qué tipo de acceso a datos desea utilizar?\n" + "1-BBDD\n" + "2-File\n" + "3-Hibernate");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
	public String askFilePath() {
		System.out.println("Introduce la ruta de tu archivo");
		String filePath = entrada.nextLine();
		return filePath;
	}

	@Override
	public String askDatabase() {
		System.out.println("Introduce el nombre de la base de datos");
		String database = entrada.nextLine();
		return database;
	}

	/*public  int objectType() {
		System.out.println("¿Con qué tipo de objeto desea manejar datos?\n"
				+ "1-Estudiante"
				+ "2-x");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}*/

	@Override
	public int dataActions() {
		System.out.println("¿Qué acción desea realizar?\n" + "1-Leer todos los objetos\n"
				+ "2-Leer un objecto específico\n" + "3-Escribir un objeto\n" + "4-Modificar un objeto\n"
				+ "5-Eliminar un objeto\n" + "6-Trasladar los objetos a otro tipo de acceso a datos\n" + "7-Salir\n");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
	public int askId() {
		System.out.println("Introduce el id del objeto que quiere leer");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	public void displayAllObjects(Map<Integer, ? extends Identifiable> map) {
		for (Identifiable object : map.values()) {
			System.out.println(object);
		}
	}

	@Override
	public void displayOneObject(Identifiable object) {
		System.out.println(object);
	}

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}

}
