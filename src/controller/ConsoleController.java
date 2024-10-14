package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import view.ConsoleView;
import model.bbdd.DDBBHandler;
import model.bbdd.MySQLHandler;
import model.bbdd.SQLiteHandler;
import model.entities.Student;
import model.fileio.BinaryFileHandler;
import model.fileio.FileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.interfaces.DataHandler;
import model.factory.StudentFactory;

public class ConsoleController {

	private ConsoleView view;
	private DataHandler<Student> myaccess;
	private InputHandler inputHandler;

	public ConsoleController(ConsoleView view) {
		this.view = view;
		this.inputHandler = new InputHandler();
	}

	private void selectDataAccess() {
		int option = view.askDataAccessType();
		switch (option) {
		case 1:
			myaccess = createDDBBHandler(view.askDatabase());
			break;
		case 2:
			myaccess = createFileHandler(view.askFilePath());
			break;
		}
	}

	public void run() {
		// primero decision de usar file o DB,
		selectDataAccess();

		// Una vez decidido, entramos en el flujo principal.
		handleDataActions();
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
		int option = view.askDataAccessType();
		switch (option) {
		case 1:
			String secondDatabase = view.askDatabase();
			DDBBHandler<Student> myaccessDDBB = createDDBBHandler(secondDatabase);
			Map<Integer, Student> mapDDBB = myaccess.readObjects();
			myaccessDDBB.writeObjects(mapDDBB, false);
			break;
		case 2:
			try {
				String secondFilePath = view.askFilePath();
				FileHandler<Student> myaccessFile = createFileHandler(secondFilePath); // Pasar a DataHandler
				Map<Integer, Student> map = myaccess.readObjects();
				myaccessFile.writeObjects(map, false); // With DDBB, always false (do not overwrite)
				myaccessFile.close();
				break;
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private FileHandler<Student> createFileHandler(String filePath) {
		FileHandler<Student> access;
		try {
			switch (getExtension(filePath)) {
			case "txt":// Text
				access = new TextFileHandler<>(new File(filePath), new StudentFactory());
				return access;
			case "dat":// Binary
			case "bin":
				access = new BinaryFileHandler<>(new File(filePath));
				return access;
			case "xml":// XML
				access = new XMLFileHandler<>(new File(filePath), new StudentFactory());
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

	private DDBBHandler<Student> createDDBBHandler(String database) {
		DDBBHandler<Student> access;
		String DataBaseType = getExtension(database);
		try {
			if (DataBaseType.equals(database)) {
				access = new MySQLHandler<>(database, new StudentFactory());
				return access;
			}
			
			switch (DataBaseType) {
			case "db":// Text
				access = new SQLiteHandler<>(database, new StudentFactory());
				return access;
			default:
				System.out.println("Opción no válida");
				break;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
