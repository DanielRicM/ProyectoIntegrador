package controller;

import java.io.File;
import java.io.IOException;

import view.View;
import model.interfaces.DataHandler;
import model.factory.ObjFactory;
import model.fileio.TextFileHandler;
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

		if (factory == null) {
			System.out.println("Opción de objeto no válida");
			return;
		}

		DataHandler<T> myaccess;
		int fileOption = view.fileType();
		try {
			
			switch (fileOption) {
			case 1:// Text
				myaccess = new TextFileHandler<>(file, factory);
			}
		} catch (IOException ex) {
			System.out.println("Error al manejar el archivo: " + ex.getMessage());
		}
	}

	private ObjFactory<T> getFactoryByOption(int objectOption) {
		switch (objectOption) {
		case 1:
			return (ObjFactory<T>)new StudentFactory(); // Fábrica para Student
		// Puedes agregar otros casos para diferentes tipos de objetos
		default:
			return null; // Opción no válida
		}
	}

}
