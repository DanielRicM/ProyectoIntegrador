package controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import view.View;

import model.entities.Student;
import model.fileio.BinaryFileHandler;
import model.fileio.FileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.factory.StudentFactory;

public class Controller {

	private View view;
	private FileHandler<Student> myaccess;
	private FileHandler<Student> myaccess2;
	private String filePath;
	private String filePath2;

	
	private InputHandler inputHandler;

	public Controller(View view) {
		this.view = view;
		this.inputHandler = new InputHandler();
	}

	public void run() {
		getFilePath();
		String ext = getExtension(filePath);
		try {
			switch (ext) {
			case "txt":// Text
				myaccess = new TextFileHandler<>(new File(filePath), new StudentFactory());
				myaccess.initialize();
				handleDataActions();
				break;
			case "dat":// Binary
			case "bin":
				myaccess = new BinaryFileHandler<>(new File(filePath));
				myaccess.initialize();
				handleDataActions();
				break;
			case "xml":// XML
				myaccess = new XMLFileHandler<>(new File(filePath));
				myaccess.initialize();
				handleDataActions();
				break;
			default:
				System.out.println("Opción no válida");
				break;
			}
		} catch (IOException ex) {
			System.out.println("Error al manejar el archivo: " + ex.getMessage());
		}

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
		String secondFilePath=view.askFilePath();
		menuSecondFile(secondFilePath);
		Map<Integer,Student> map = myaccess.readObjects();
		myaccess2.writeObjects(map);
		try {
			myaccess2.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void menuSecondFile(String filePath) {
		String ext = getExtension(filePath);
		try {
			switch (ext) {
			case "txt":// Text
				myaccess2 = new TextFileHandler<>(new File(filePath), new StudentFactory());
				break;
			case "dat":// Binary
			case "bin":
				myaccess2 = new BinaryFileHandler<>(new File(filePath));
				break;
			case "xml":// XML
				myaccess2 = new XMLFileHandler<>(new File(filePath));
				break;
			default:
				System.out.println("Opción no válida");
				break;
			}
		} catch (IOException ex) {
			System.out.println("Error al manejar el archivo: " + ex.getMessage());
		}
	}
	

	

	
	

	

}
