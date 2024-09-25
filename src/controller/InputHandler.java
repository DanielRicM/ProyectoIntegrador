package controller;
import model.entities.Student;
import java.util.Scanner;

public class InputHandler{
    private Scanner entrada;

    public InputHandler() {
        this.entrada = new Scanner(System.in);
    }

    /*public Student getStudentDetails() {
        return getStudentDetails(null);
    }
    */

    public Student getStudentDetails(Student existingStudent) {
    	System.out.println("Introduce el id: ");
		int id = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el nombre: ");
		String name = entrada.nextLine();
		
		System.out.println("Introduce la edad: ");
		int age = Integer.parseInt(entrada.nextLine());
		
		System.out.println("Introduce el curso: ");
		String course = entrada.nextLine();

        if (existingStudent != null) {
            existingStudent.setName(name);
            existingStudent.setAge(age);
            existingStudent.setCourse(course);
            return existingStudent;
        } else {
            return new Student(id, name, age, course);
        }
    }
}