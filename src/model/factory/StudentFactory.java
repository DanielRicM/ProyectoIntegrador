package model.factory;

import model.entities.Student;

public class StudentFactory implements ObjFactory<Student> {

	@Override
	public Student create(String line) {
		String[] parts = line.split(";");
		int id = Integer.parseInt(parts[0]);
		String name = parts[1];
		int age = Integer.parseInt(parts[2]);
		String course = parts[3];

		return new Student(id, name, age, course);
	}
}
