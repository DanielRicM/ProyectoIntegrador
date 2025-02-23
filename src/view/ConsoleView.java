package view;

import java.text.NumberFormat;
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
        try{
            return Integer.parseInt(scanner.nextLine());
        }catch(NumberFormatException e){
            return 0;
        }

    }

    @Override
    public int askDataAccessType() {
        System.out.print("Select a data access type:\n1- MySQL\n2- SQLite\n3- Text File\n4- XML File\n5- Binary File\n6- Hibernate\n7- Client/Server\n8- OODB\n9- BaseX\n10- MongoDB\nOption: ");
        try{
            return Integer.parseInt(scanner.nextLine());
        }catch(NumberFormatException e){
            return 0;
        }
    }

    @Override
    public int dataActions() {
        System.out.print("What do you want to do?\n1- Read all objects\n2- Read object by Id\n3- Create an object\n4- Update an object\n5- Delete an object\n6- Move everyting to another access type\n7- Exit\nOption: ");
        try{
            return Integer.parseInt(scanner.nextLine());
        }catch(NumberFormatException e){
            return 0;
        }
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
