package main;

import controller.Controller;
import view.ConsoleView;

public class Main {
	
	public static void main(String[] args) {
		ConsoleView view = new ConsoleView();
		Controller controller = new Controller(view);
		controller.run();
	}
}
