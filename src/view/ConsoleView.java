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
		System.out.println("Select an Object type:\n" + "1- Student\n" + "2- Song\n" + "");
		System.out.print("Option: ");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
	public int askDataAccessType() {
		System.out.println("Select a data access type:\n" + "1- Database\n" + "2- File\n" + "3- Hibernate");
		System.out.print("Option: ");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
	public String askFilePath() {
		System.out.print("Enter the Filepath: ");
		String filePath = entrada.nextLine();
		return filePath;
	}

	@Override
	public String askDatabase() {
		System.out.print("Enter the Database name: ");
		String database = entrada.nextLine();
		return database;
	}

	@Override
	public int dataActions() {
		System.out.println("¿Qué acción desea realizar?\n" + "1-Leer todos los objetos\n"
				+ "2-Leer un objecto específico\n" + "3-Escribir un objeto\n" + "4-Modificar un objeto\n"
				+ "5-Eliminar un objeto\n" + "6-Trasladar los objetos a otro tipo de acceso a datos\n" + "7-Salir\n");
		System.out.print("Option: ");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
	public int askId() {
		System.out.print("Enter the ID of the object you want to read: ");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}

	@Override
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

	public void optionNotValid() {
		this.displayMessage("Option not valid. Try again.");
	}

}
