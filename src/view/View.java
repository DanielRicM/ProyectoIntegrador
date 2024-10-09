package view;

import java.util.Map;

import model.entities.Student;

public interface View {

	int askDataAccessType();
	
	String askFilePath();
	
	String askDatabase();

	int dataActions();

	int askId();

	void displayAllObjects(Map<Integer, Student> map);

	void displayOneObject(Student object);

	void displayMessage(String message);
}