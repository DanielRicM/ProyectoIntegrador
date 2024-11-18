package controller;

import java.util.Scanner;

import model.entities.Student;
import model.interfaces.Identifiable;

public class StudentInputHandler implements InputHandler{

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

	    System.out.println("Introduce el nombre: ");
	    String name = entrada.nextLine();
	    if (name.isBlank() && student == null) {
	        System.out.println("El nombre es obligatorio.");
	        return getDetails(object); // Restart input for creation.
	    }

	    System.out.println("Introduce la edad: ");
	    String ageInput = entrada.nextLine();
	    Integer age = null;
	    if (!ageInput.isBlank()) {
	        try {
	            age = Integer.parseInt(ageInput);
	        } catch (NumberFormatException e) {
	            System.out.println("Edad no válida.");
	        }
	    } else if (student == null) {
	        System.out.println("La edad es obligatoria.");
	        return getDetails(object); // Restart input for creation.
	    }

	    System.out.println("Introduce el curso: ");
	    String course = entrada.nextLine();
	    if (course.isBlank() && student == null) {
	        System.out.println("El curso es obligatorio.");
	        return getDetails(object); // Restart input for creation.
	    }

	    // If modifying, retain original values if input is blank.
	    if (student != null) {
	        if (!name.isBlank()) student.setName(name);
	        if (age != null) student.setAge(age);
	        if (!course.isBlank()) student.setCourse(course);
	        return student;
	    }

	    // If creating, all values are mandatory and validated.
	    int id = 0;
	    while (id == 0) {
	        System.out.println("Introduce el id: ");
	        String idInput = entrada.nextLine();
	        try {
	            id = Integer.parseInt(idInput);
	        } catch (NumberFormatException e) {
	            System.out.println("ID no válido. Por favor, introduce un número.");
	        }
	    }
	    return new Student(id, name, age, course);
	}


}
