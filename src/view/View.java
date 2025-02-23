package view;

import java.util.Map;

import model.interfaces.Identifiable;

public interface View {

	boolean startMenu();

	int askObjectType();
	
	int askDataAccessType();

	int dataActions();

	int askId();

	void displayAllObjects(Map<Integer, ? extends Identifiable> map);

	void displayOneObject(Identifiable object);

	void displayMessage(String message);
}