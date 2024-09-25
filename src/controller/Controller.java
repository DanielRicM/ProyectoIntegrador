package controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

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

	public void initialize() {
		int accessOption = view.typeDataAccess();

		switch (accessOption) {
		case 1:// File
			int objectOption = view.objectType();
			handleFileOption(objectOption);
			break;
		case 2:// BBDD

			break;
		default:
			System.out.println("Opción no válida");
			break;
		}
	}

	public void handleFileOption(int objectOption) {
		ObjFactory<T> factory = getFactoryByOption(objectOption); // Obtener la fábrica correcta según la opción


		DataHandler<T> myaccess;
		int fileOption = view.fileType();
		
		try {
			Properties properties = new Properties();
			switch (fileOption) {
			case 1:// Text
				File file=new File(properties.getProperty("text.file.path"));
				myaccess = new TextFileHandler<>(file, factory);
				handleDataActions(myaccess);
				break;
			case 2:
				File file2=new File(properties.getProperty("binary.file.path"));
				myaccess = new BinaryFileHandler<>(file2);
				handleDataActions(myaccess);
				break;
			case 3:
				File file3=new File(properties.getProperty("xml.file.path"));
				myaccess = new XMLFileHandler<>(file3);
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
	
	@SuppressWarnings("unchecked")
	public void handleDataActions(DataHandler<T> myaccess) {
		int dataActionOption= view.dataActions();
		
		switch(dataActionOption) {
		case 1: 
			Map<Integer,T>map=myaccess.readObjects();
			System.out.println(map);
			break;
		case 2:
			Object object=myaccess.readObject(view.askIdToRead());
			System.out.println(object);
			break;
		case 3:
			//myaccess.writeObjects();
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
