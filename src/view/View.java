package view;

import java.util.Map;
import model.interfaces.Identifiable;

public interface View {

	String askFilePath();

	int dataActions();

	int askId();

	void displayAllObjects(Map<Integer, ? extends Identifiable> map);

	void displayOneObject(Identifiable object);

	void displayMessage(String message);
}