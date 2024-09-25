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
	private String filePath;
	private InputHandler inputHandler;

	public Controller(View view) {
		this.view = view;
		this.inputHandler = new InputHandler();
	}

	public void run() {
		getFilePath();
		String ext = getExtension();
		try {
			switch (ext) {
			case "txt":// Text
				myaccess = new TextFileHandler<>(new File(filePath), new StudentFactory());
				handleDataActions();
				break;
			case "dat":// Binary
			case "bin":
				myaccess = new BinaryFileHandler<>(new File(filePath));
				handleDataActions();
				break;
			case "xml":// XML
				myaccess = new XMLFileHandler<>(new File(filePath));
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

	public String getExtension() {
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
				myaccess.deleteObject(view.askId());
				break;
			case 6:
				// Traslado
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

	private void writeOneObject() {
		Student Student = new InputHandler().getStudentDetails(null);
		myaccess.writeObject(Student);
	}

	private void viewOneObject() {
		Student object = myaccess.readObject(view.askId());
		view.displayOneObject(object);
	}

	private void viewAllObjects() {
		Map<Integer, Student> map = myaccess.readObjects();
		view.displayAllObjects(map);
	}

	private void modifySingleObject() {
		int id = view.askId();
		
		try {
			Student existingStudent = myaccess.readObject(id);
			Student updatedStudent = inputHandler.getStudentDetails(existingStudent);
			myaccess.modifyObject(id, updatedStudent);
			view.displayMessage("Objeto modificado con éxito.");
		} catch (IllegalArgumentException e) {
			view.displayMessage("Objeto no encontrado.");
		}
	}

}
