package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import view.ConsoleView;
import model.Hibernate.HibernateHandler;
import model.bbdd.DDBBHandler;
import model.bbdd.MySQLHandler;
import model.bbdd.SQLiteHandler;
import model.fileio.BinaryFileHandler;
import model.fileio.FileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.interfaces.DataHandler;
import model.interfaces.Identifiable;
import model.factory.ObjFactory;
import model.factory.StudentFactory;

public class ConsoleController {

	private ConsoleView view;
	private DataHandler<Identifiable> myaccess;
	private ObjFactory<Identifiable> factory;
	private InputHandler inputHandler;
	private String table;
	private String clazz;

	public ConsoleController(ConsoleView view) {
		this.view = view;
	}

	private void selectObjectType() {
		int option = view.askObjectType();
		switch (option) {
		case 1:
			inputHandler = new StudentInputHandler();
			factory = new StudentFactory();
			table = "students";
			clazz = "Student";
			break;
		}
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
		case 3:
			myaccess = createHibernateHandler();
			break;
		}
	}

	public void run() {
		
		selectObjectType();
		
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
		Map<Integer, Identifiable> map = myaccess.readObjects();
		view.displayAllObjects(map);
	}

	private void viewOneObject() {
		Identifiable object = (Identifiable) myaccess.readObject(view.askId());
		view.displayOneObject(object);
	}

	private void writeOneObject() {
		Identifiable object = inputHandler.getDetails(null);
		myaccess.writeObject(object);
	}

	private void modifySingleObject() {
		int id = view.askId();

		try {
			Identifiable existingObject = (Identifiable) myaccess.readObject(id);
			Identifiable updatedObject = inputHandler.getDetails(existingObject);
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
			DDBBHandler<Identifiable> myaccessDDBB = createDDBBHandler(secondDatabase);
			Map<Integer, Identifiable> mapDDBB = myaccess.readObjects();
			myaccessDDBB.writeObjects(mapDDBB, false);
			break;
		case 2:
			try {
				String secondFilePath = view.askFilePath();
				FileHandler<Identifiable> myaccessFile = createFileHandler(secondFilePath); // Pasar a DataHandler
				Map<Integer, Identifiable> map = myaccess.readObjects();
				myaccessFile.writeObjects(map, false); // With DDBB, always false (do not overwrite)
				myaccessFile.close();
				break;
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private FileHandler<Identifiable> createFileHandler(String filePath) {
		FileHandler<Identifiable> access;
		try {
			switch (getExtension(filePath)) {
			case "txt":// Text
				access = new TextFileHandler<>(new File(filePath), factory);
				return access;
			case "dat":// Binary
			case "bin":
				access = new BinaryFileHandler<>(new File(filePath));
				return access;
			case "xml":// XML
				access = new XMLFileHandler<>(new File(filePath), factory);
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

	private DDBBHandler<Identifiable> createDDBBHandler(String database) {
		DDBBHandler<Identifiable> access;
		String DataBaseType = getExtension(database);
		try {
			if (DataBaseType.equals(database)) {
				access = new MySQLHandler<>(database, factory, table);
				return access;
			}
			
			switch (DataBaseType) {
			case "db":// Text
				access = new SQLiteHandler<>(database, factory, table);
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
	
	private HibernateHandler<Identifiable> createHibernateHandler(){
		return new HibernateHandler<>(clazz);
	}
}
