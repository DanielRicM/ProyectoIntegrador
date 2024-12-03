package controller;

import java.util.Scanner;

import model.entities.Student;
import model.interfaces.Identifiable;

public class StudentInputHandler implements InputHandler {

	private Scanner entrada;

	public StudentInputHandler() {
		entrada = new Scanner(System.in);
	}

	@Override
	public Student getDetails() {
		return getDetails(null);
	}

	@Override
	public Student getDetails(Identifiable object) {
		Student student = (object instanceof Student) ? (Student) object : null;

		System.out.print("Enter the name: ");
		String name = entrada.nextLine();
		if (name.isBlank() && student == null) {
			System.out.println("The name is mandatory.");
			return getDetails(object);
		}

		System.out.print("Enter the age: ");
		String ageInput = entrada.nextLine();
		Integer age = null;
		if (!ageInput.isBlank()) {
			try {
				age = Integer.parseInt(ageInput);
			} catch (NumberFormatException e) {
				System.out.println("Age is not valid. Please enter a number");
			}
		} else if (student == null) {
			System.out.println("Age is mandatory.");
			return getDetails(object);
		}

		System.out.print("Enter the course: ");
		String course = entrada.nextLine();
		if (course.isBlank() && student == null) {
			System.out.println("The course is mandatory.");
			return getDetails(object);
		}

		if (student != null) {
			if (!name.isBlank())
				student.setName(name);
			if (age != null)
				student.setAge(age);
			if (!course.isBlank())
				student.setCourse(course);
			return student;
		}
		
		int id = 0;
		while (id == 0) {
			System.out.println("Enter the id: ");
			String idInput = entrada.nextLine();
			try {
				id = Integer.parseInt(idInput);
			} catch (NumberFormatException e) {
				System.out.println("ID is not valid. Please enter a number.");
			}
		}
		return new Student(id, name, age, course);
	}

}
