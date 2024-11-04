package controller;

import model.interfaces.Identifiable;

public interface InputHandler {
	
	public Identifiable getDetails();
	
	public Identifiable getDetails(Identifiable object);

}
