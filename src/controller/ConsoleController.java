package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;

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
import model.factory.SongFactory;

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

	public void run() {
		selectObjectType();

		selectDataAccess();

		handleDataActions();
	}

	private void selectObjectType() {
		while (true) {
			int option = view.askObjectType();
			switch (option) {
			case 1:
				inputHandler = new StudentInputHandler();
				factory = new StudentFactory();
				table = "students";
				clazz = "Student";
				return;
			case 2:
				inputHandler = new SongInputHandler();
				factory = new SongFactory();
				table = "songs";
				clazz = "Song";
				return;
			default:
				view.optionNotValid();
			}
		}
	}

	private void selectDataAccess() {
		while (true) {
			int option = view.askDataAccessType();
			switch (option) {
			case 1:
				myaccess = createDDBBHandler(view.askDatabase());
				return;
			case 2:
				myaccess = createFileHandler(view.askFilePath());
				return;
			case 3:
				myaccess = createHibernateHandler();
				return;
			default:
				view.optionNotValid();
			}
		}
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
					view.displayMessage("Error closing the access: " + e.getMessage());
				}
				System.out.println("Exiting...");
				running = false;
				break;
			default:
				view.optionNotValid();
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
		try {
			myaccess.writeObject(object);
		} catch (ConstraintViolationException e) {
			System.out.println("Constraint violation: " + e.getConstraintName());
		} catch (Exception e) {
			System.out.println("Error saving the object: " + e.getMessage());
		}
	}

	private void modifySingleObject() {
		int id = view.askId();

		try {
			Identifiable existingObject = (Identifiable) myaccess.readObject(id);
			Identifiable updatedObject = inputHandler.getDetails(existingObject);
			myaccess.modifyObject(id, updatedObject);
			view.displayMessage("Update successful.");
		} catch (IllegalArgumentException e) {
			view.displayMessage("Object not found.");
		}
	}

	private void deleteOneObject() {
		myaccess.deleteObject(view.askId());
	}

	private void writeAllObjects() {
		Map<Integer, Identifiable> map = myaccess.readObjects();
		int option = view.askDataAccessType();
		switch (option) {
		case 1:
			String secondDatabase = view.askDatabase();
			DDBBHandler<Identifiable> myaccessDDBB = createDDBBHandler(secondDatabase);
			myaccessDDBB.writeObjects(map, false);
			break;
		case 2:
			try {
				String secondFilePath = view.askFilePath();
				FileHandler<Identifiable> myaccessFile = createFileHandler(secondFilePath); // Pasar a DataHandler
				myaccessFile.writeObjects(map, false); // With DDBB, always false (do not overwrite)
				myaccessFile.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			break;
		case 3:
			HibernateHandler<Identifiable> myHibernateAccess = createHibernateHandler();
			myHibernateAccess.writeObjects(map, false);

		}
	}

	public String getExtension(String filePath) {
		String[] parts = filePath.split("\\.");
		return parts[parts.length - 1];
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
				view.optionNotValid();
				break;
			}
		} catch (IOException ex) {
			view.displayMessage("Error handling file: " + ex.getMessage());
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
			switch (DataBaseType) { // En un futuro se implementarán otras based de datos
			case "db":
				access = new SQLiteHandler<>(database, factory, table);
				return access;
			default:
				view.optionNotValid();
			}
		} catch (ClassNotFoundException cnfe) {
			view.displayMessage("Database class error" + cnfe);

		} catch (SQLException sqle) {
			view.displayMessage("Database error" + sqle);
		}
		return null;
	}

	private HibernateHandler<Identifiable> createHibernateHandler() {
		view.displayMessage("HibernateHandler Created");
		return new HibernateHandler<>(clazz);
	}
}
