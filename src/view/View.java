package view;

import java.util.Scanner;

import model.entities.Student;
import model.interfaces.DataHandler;

public class View {
	
	static Scanner entrada=new Scanner(System.in);

	public View() {
	}

	public int typeDataAccess() {
		System.out.println("¿Qué tipo de acceso a datos desea utilizar?\n"
				+ "1-File\n"
				+ "2-BBDD");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	public  int objectType() {
		System.out.println("¿Con qué tipo de objeto desea manejar datos?\n"
				+ "1-Estudiante"
				+ "2-x");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	public  int fileType() {
		System.out.println("¿Qué tipo de archivos desea manejar inicialmente?\n"
				+ "1-Archivos de texto"
				+ "2-Archivos binarios"
				+ "3-Archivos XML");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	public int dataActions() {
		
		System.out.println("¿Qué acción desea realizar?\n"
				+ "1-Leer todos los objetos"
				+ "2-Leer un objecto específico"
				+ "3-Escribir varios objetos"//en el mismo o en otro?
				+ "4-Escribir un solo objeto"//en el mismo o en otro?
				+ "5-Modificar un objeto"
				+ "6-Eliminar un objeto"
				+ "7-Salir");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	public int askIdToRead() {
		System.out.println("Introduce el id del objeto que quiere leer");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	public int askIdToModify() {
		System.out.println("Introduce el id del objeto que desea modificar");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	public int askIdToRemove() {
		System.out.println("Introduce el id del objeto que desea eliminar");
		int opcion = Integer.parseInt(entrada.nextLine());
		return opcion;
	}
	
	public String askStudentToWrite() {
		System.out.println("Introduce el id del objeto que quiere escribir");
		int id = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el nombre del objeto que quiere escribir");
		String name = entrada.nextLine();
		
		System.out.println("Introduce el nombre del objeto que quiere escribir");
		int age = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el nombre del objeto que quiere escribir");
		String course = entrada.nextLine();
		
		
		return id+";"+name+";"+age+";"+course;
	}
	
	public String askNewStudent() {
		System.out.println("Introduce el id del nuevo objeto");
		int id = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el nombre del del nuevo objeto");
		String name = entrada.nextLine();
		
		System.out.println("Introduce el nombre del del nuevo objeto");
		int age = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el nombre del del nuevo objeto");
		String course = entrada.nextLine();
		
		
		return id+";"+name+";"+age+";"+course;
	}
}
