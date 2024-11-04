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
		Student student = (Student)object;
		
		System.out.println("Introduce el nombre: ");
		String name = entrada.nextLine();
		
		System.out.println("Introduce la edad: ");
		int age = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el curso: ");
		String course = entrada.nextLine();

        if (student != null) {
        	student.setName(name);
        	student.setAge(age);
        	student.setCourse(course);
            return student;
        } else {
        	System.out.println("Introduce el id: ");
    		int id = Integer.parseInt(entrada.nextLine());
            return new Student(id, name, age, course);
        }
	}

}
