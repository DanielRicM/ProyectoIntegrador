package controller;

import java.util.Scanner;

import model.entities.Song;
import model.entities.Student;
import model.interfaces.Identifiable;

public class StudentInputHandler implements InputHandler {

    private final Scanner scanner;

    public StudentInputHandler() {
        scanner = new Scanner(System.in);
    }

    @Override
    public Student getDetails(Identifiable object) {
        Student student;
        if (object instanceof Student s) {
            student = s;
        } else {
            student = null;
        }

        String name = getNonEmptyInput("Enter the name: ", student == null);
        Integer age = getValidIntegerInput("Enter the age: ", student == null);
        String course = getNonEmptyInput("Enter the course: ", student == null);

        if (student != null) {
            if (!name.isBlank()) student.setName(name);
            if (age != null) student.setAge(age);
            if (!course.isBlank()) student.setCourse(course);
            return student;
        }

        int id = getValidId();
        return new Student(id, name, (age != null ? age : 0), course);
    }


    private String getNonEmptyInput(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank() || !required) {
                return input;
            }
            System.out.println("This field is mandatory.");
        }
    }

    private Integer getValidIntegerInput(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            } else if (required) {
                System.out.println("This field is mandatory.");
            } else {
                return null;
            }
        }
    }

    private int getValidId() {
        while (true) {
            System.out.print("Enter the id: ");
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ID is not valid. Please enter a number.");
            }
        }
    }


}
