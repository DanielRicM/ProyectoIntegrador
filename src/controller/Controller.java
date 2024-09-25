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
				myaccess = new TextFileHandler<>(new File(view.askFilePath()), new StudentFactory());
				handleDataActions();
				break;
			case "dat"://Binary
			case "bin":
				myaccess = new BinaryFileHandler<>(new File(view.askFilePath()));
				handleDataActions();
				break;
			case "xml"://XML
				myaccess = new XMLFileHandler<>(new File(view.askFilePath()));
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
	
	
	@SuppressWarnings("unchecked")
	public void handleDataActions() {
		int dataActionOption= view.dataActions();
		
		switch(dataActionOption) {
		case 1: 
			viewAllObjects();
			break;
		case 2:
			viewOneObject();
			break;
		case 3:
			//myaccess.writeObjects();
			break;
		case 4:
			writeOneObject();
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
	
	private void modifySingleObject() {
        int id = view.askIdToModify();
        try {
            Student existingStudent = myaccess.readObject(id);
            Student updatedStudent = inputHandler.getStudentDetails(existingStudent);
            dataHandler.modifyObject(id, updatedStudent);
            view.displayMessage("Student updated successfully.");
        } catch (IllegalArgumentException e) {
            view.displayMessage("Student not found.");
        }
    }
	
	
	

}
