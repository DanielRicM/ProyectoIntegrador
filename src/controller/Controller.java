package controller;

import java.io.File;
import java.io.IOException;

import view.View;
import model.interfaces.DataHandler;
import model.entities.Student;
import model.factory.ObjFactory;
import model.fileio.BinaryFileHandler;
import model.fileio.TextFileHandler;
import model.fileio.XMLFileHandler;
import model.factory.StudentFactory;

public class Controller<T> {

	private View view;

	public Controller() {
		view = new View();
	}

	public void initialize(File file) {
		int accessOption = view.typeDataAccess();

		switch (accessOption) {
		case 1:// File
			int objectOption = view.objectType();
			handleFileOption(file, objectOption);
			break;
		case 2:// BBDD

			break;
		default:
			System.out.println("Opción no válida");
			break;
		}
	}

	public void handleFileOption(File file, int objectOption) {
		ObjFactory<T> factory = getFactoryByOption(objectOption); // Obtener la fábrica correcta según la opción


		DataHandler<T> myaccess;
		int fileOption = view.fileType();
		
		try {
			switch (fileOption) {
			case 1:// Text
				myaccess = new TextFileHandler<>(file, factory);
				handleDataActions(myaccess);
				break;
			case 2:
				myaccess = new BinaryFileHandler<>(file);
				handleDataActions(myaccess);
				break;
			case 3:
				myaccess = new XMLFileHandler<>(file);
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

	private ObjFactory<T> getFactoryByOption(int objectOption) {
		switch (objectOption) {
		case 1:
			return (ObjFactory<T>)new StudentFactory(); // Fábrica para Student
		
		default:
            throw new IllegalArgumentException("Opción de objeto no válida: " + objectOption);
		}
	}
	
	//Falta controlar los write dependiendo del tipo que sea el objeto, de momento solo con estudiantes
	@SuppressWarnings("unchecked")
	public void handleDataActions(DataHandler<T> myaccess) {
		int dataActionOption= view.dataActions();
		
		switch(dataActionOption) {
		case 1: 
			myaccess.readObjects();
			break;
		case 2:
			myaccess.readObject(view.askIdToRead());
			break;
		case 3:
			myaccess.writeObjects();
			break;
		case 4:
			String studentData=view.askStudentToWrite();
			Student student=new StudentFactory().create(studentData);
			myaccess.writeObject((T)student);
			break;
		case 5:
			String studentData2=view.askNewStudent();
			Student student2=new StudentFactory().create(studentData2);
			myaccess.modifyObject(view.askIdToModify(), (T)student2);
			break;
		case 6:
			myaccess.deleteObject(view.askIdToRemove());
			break;
		case 7:
			System.out.println("Has salido del menú");
			break;
		}
	}

}
