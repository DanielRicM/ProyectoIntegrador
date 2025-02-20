package view;

import java.util.Map;
import java.util.Scanner;

import model.interfaces.Identifiable;

public class ConsoleView implements View {

    private final Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    @Override
    public boolean startMenu() {
        System.out.print("Press Enter to continue or Q to quit");
        String option = scanner.nextLine();
        return !option.equalsIgnoreCase("Q");
    }

    @Override
    public int askObjectType() {
        System.out.print("Select an Object type:\n1- Student\n2- Song\nOption: ");
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public int askDataAccessType() {
        System.out.print("Select a data access type:\n1- Database\n2- File\n3- Hibernate\n4- Client/Server\n5- OODB\n6- BaseX\n7- MongoDB\nOption: ");
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public String askFilePath() {
        System.out.print("Enter the Filepath: ");
        return scanner.nextLine();
    }

    @Override
    public String askDatabase() {
        System.out.print("Enter the Database name: ");
        return scanner.nextLine();
    }

    @Override
    public int dataActions() {
        System.out.print("What do you want to do?\n1- Read all objects\n2- Read object by Id\n3- Create an object\n4- Update an object\n5- Delete an object\n6- Move everyting to another access type\n7- Exit\nOption: ");
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public int askId() {
        System.out.print("Enter the ID of the object you want to read: ");
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public void displayAllObjects(Map<Integer, ? extends Identifiable> map) {
        for (Identifiable object : map.values()) {
            System.out.println(object);
        }
    }

    @Override
    public void displayOneObject(Identifiable object) {
        System.out.println(object);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void optionNotValid() {
        this.displayMessage("Option not valid. Try again.");
    }

}
