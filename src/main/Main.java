package main;

import controller.ConsoleController;
import view.ConsoleView;

public class Main {

    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(view);
        controller.run();
    }
}
