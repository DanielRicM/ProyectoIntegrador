package view;

import java.util.Map;

import model.interfaces.Identifiable;

public interface View {

	int askObjectType();
	
	int askDataAccessType();
	
	String askFilePath();
	
	String askDatabase();

	int dataActions();

	int askId();

	void displayAllObjects(Map<Integer, ? extends Identifiable> map);

	void displayOneObject(Identifiable object);

	void displayMessage(String message);
}