package controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import view.ConsoleView;

import model.entities.Student;
import model.fileio.BinaryFileHandler;
import model.fileio.FileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.interfaces.DataHandler;
import model.factory.StudentFactory;

public class Controller {

	private ConsoleView view;
	private DataHandler<Student> myaccess;
	private String filePath;
	private InputHandler inputHandler;

	public Controller(ConsoleView view) {
		this.view = view;
		this.inputHandler = new InputHandler();
	}

	public void run() {
		getFilePath();
		myaccess = createFileHandler(filePath);
		handleDataActions();
	}

	public void getFilePath() {
		this.filePath = view.askFilePath();
	}

	public String getExtension(String filePath) {
		String[] parts = filePath.split("\\.");
		return parts[parts.length - 1];
	}

	public void handleDataActions() {
		boolean running = true;
		while (running) {
			int dataActionOption = view.dataActions();
			switch (dataActionOption) {
			case 1:
				viewAllObjects();
				break;
			case 2:
				viewOneObject();
				break;
			case 3:
				writeOneObject();
				break;
			case 4:
				modifySingleObject();
				break;
			case 5:
				deleteOneObject();
				break;
			case 6:
				writeAllObjects();
				break;
			case 7:
				try {
					myaccess.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				System.out.println("Has salido del menú");
				running = false;
				break;
			default:
				System.out.println("Opción no válida");
				break;
			}
		}

	}

	private void viewAllObjects() {
		Map<Integer, Student> map = myaccess.readObjects();
		view.displayAllObjects(map);
	}

	private void viewOneObject() {
		Student object = myaccess.readObject(view.askId());
		view.displayOneObject(object);
	}

	private void writeOneObject() {
		Student object = new InputHandler().getStudentDetails(null);
		myaccess.writeObject(object);
	}

	private void modifySingleObject() {
		int id = view.askId();

		try {
			Student existingObject = myaccess.readObject(id);
			Student updatedObject = inputHandler.getStudentDetails(existingObject);
			myaccess.modifyObject(id, updatedObject);
			view.displayMessage("Objeto modificado con éxito.");
		} catch (IllegalArgumentException e) {
			view.displayMessage("Objeto no encontrado.");
		}
	}

	private void deleteOneObject() {
		myaccess.deleteObject(view.askId());
	}

	private void writeAllObjects() {
		try {
			String secondFilePath = view.askFilePath();
			FileHandler<Student> myaccess2 = createFileHandler(secondFilePath);
			Map<Integer, Student> map = myaccess.readObjects();
			myaccess2.writeObjects(map);
			myaccess2.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private FileHandler<Student> createFileHandler(String filePath) {
		FileHandler<Student> access;
		try {
			switch (getExtension(filePath)) {
			case "txt":// Text
				access = new TextFileHandler<>(new File(filePath), new StudentFactory());
				access.initialize();
				return access;
			case "dat":// Binary
			case "bin":
				access = new BinaryFileHandler<>(new File(filePath));
				access.initialize();
				return access;
			case "xml":// XML
				access = new XMLFileHandler<>(new File(filePath), new StudentFactory());
				access.initialize();
				return access;
			default:
				System.out.println("Opción no válida");
				break;
			}
		} catch (IOException ex) {
			System.out.println("Error al manejar el archivo: " + ex.getMessage());
		}
		return null;
	}

}
