package controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import view.View;
import model.interfaces.DataHandler;
import model.entities.Student;
import model.factory.ObjFactory;
import model.fileio.BinaryFileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.factory.StudentFactory;

public class Controller {

	private View view;
	private DataHandler<Student> myaccess;
	private String filePath;

	public Controller(View view) {
		this.view = view;
	}

	
	public void run() {
		getFilePath();
		String extension=getExtension();
		
		
		try {
			switch (extension) {
			case "txt":// Text
				myaccess = new TextFileHandler<>(new File(view.askFilePath()), factory);
				handleDataActions(myaccess);
				break;
			case "dat"://Binary
				myaccess = new BinaryFileHandler<>(new File(view.askFilePath()));
				handleDataActions(myaccess);
				break;
			case "bin":
				myaccess = new BinaryFileHandler<>(new File(view.askFilePath()));
				handleDataActions(myaccess);
			case "xml"://XML
				myaccess = new XMLFileHandler<>(new File(view.askFilePath()));
				handleDataActions(myaccess);
				break;
			default:
				System.out.println("Opción no válida");
				break;
			}
		} catch (IOException ex) {
			System.out.println("Error al manejar el archivo: " + ex.getMessage());
		}
			
	}
	
	public String getFilePath() {
		this.filePath=view.askFilePath();
		return filePath;
	}
	
	
	
	
	public String getExtension() {
		String[] parts = filePath.split(".");

		switch (parts[1]) {
		case "txt":
			return "txt";
		case "dat":
			return "dat";
		case "bin":
			return "bin";
		case "xml":
			return "xml";
		}
		return null;
	}

	public void handleFileOption(int objectOption) {
		ObjFactory<T> factory = getFactoryByOption(objectOption); // Obtener la fábrica correcta según la opción


		
		
		
	}

	private ObjFactory<T> getFactoryByOption(int objectOption) {
		switch (objectOption) {
		case 1:
			return (ObjFactory<T>)new StudentFactory(); // Fábrica para Student
		
		default:
            throw new IllegalArgumentException("Opción de objeto no válida: " + objectOption);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void handleDataActions(DataHandler<Student> myaccess) {
		int dataActionOption= view.dataActions();
		
		switch(dataActionOption) {
		case 1: 
			viewAllObjects();
			break;
		case 2:
			viewOneObject(myaccess);
			break;
		case 3:
			//myaccess.writeObjects();
			break;
		case 4:
			writeOneObject(myaccess);
			break;
		case 5:
			int id = view.askIdToModify();
			Student Student=new InputHandler().getStudentDetails(null);
			myaccess.modifyObject(id, Student);
			break;
		case 6:
			myaccess.deleteObject(view.askIdToRemove());
			break;
		case 7:
			System.out.println("Has salido del menú");
			break;
		case 8:
			break;
		default:
			System.out.println("Opción no válida");
			break;
		}
	}

	private void writeOneObject() {
		Student Student=new InputHandler().getStudentDetails(null);
		myaccess.writeObject(Student);
	}

	private void viewOneObject() {
		Student object=myaccess.readObject(view.askIdToRead());
		view.displayOneObject(object);
	}

	private void viewAllObjects() {
		Map<Integer,Student>map=myaccess.readObjects();
		view.displayAllObjects(map);
	}
	
	
	

}
