package model.factory;

import org.jdom2.Attribute;
import org.jdom2.Element;

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

	@Override
	public String toCSV(Student student) {
		return student.getId() + ";" + student.getName() + ";" + student.getAge() + ";" + student.getCourse();
	}

	@Override
	public Student create(Element studentElement) {
		int id = Integer.parseInt(studentElement.getAttributeValue("id"));
		String name = studentElement.getChildText("name");
		int age = Integer.parseInt(studentElement.getChildText("age"));
		String course = studentElement.getChildText("course");

		Student student = new Student(id, name, age, course);
		return student;
	}

	@Override
	public Element toXML(Student student) {
		Element studentElement = new Element("student");
		studentElement.setAttribute(new Attribute("id", String.valueOf(student.getId())));

		Element name = new Element("name").setText(student.getName());
		Element age = new Element("age").setText(String.valueOf(student.getAge()));
		Element course = new Element("course").setText(student.getCourse());

		studentElement.addContent(name);
		studentElement.addContent(age);
		studentElement.addContent(course);
		return studentElement;
	}

	@Override
	public String toQuery(Student student) {
		String queryValues= String.valueOf(student.getId())+ ", '"
				+student.getName()+ ", '"
		+String.valueOf(student.getAge())
		+ ", '"+student.getCourse();
		return queryValues;
	}
	
	public String toUpdateQuery(Student student) {
        return "UPDATE students SET name = '" + student.getName() + 
               "', age = " + student.getAge() + 
               ", course = '" + student.getCourse() + 
               "'";
    }
}
