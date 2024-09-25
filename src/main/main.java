package main;

import controller.Controller;
import view.View;

public class main {
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        controller.run();
    }
}
