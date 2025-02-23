package model.factory;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.json.simple.JSONObject;

import model.entities.Student;
import model.interfaces.Identifiable;

public class StudentFactory implements ObjFactory<Identifiable> {

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
    public String toCSV(Identifiable object) {
        Student student = (Student) object;
        return student.getId() + ";" + student.getName() + ";" + student.getAge() + ";" + student.getCourse();
    }

    @Override
    public Student create(Element rootElement) {
        int id = Integer.parseInt(rootElement.getAttributeValue("id"));
        String name = rootElement.getChildText("name");
        int age = Integer.parseInt(rootElement.getChildText("age"));
        String course = rootElement.getChildText("course");

        return new Student(id, name, age, course);
    }

    @Override
    public Element toXML(Identifiable object) {
        Student student = (Student) object;
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
    public String toQuery(Identifiable object) {
        Student student = (Student) object;
        return String.valueOf(student.getId()) + ", '" + student.getName() + "', " + student.getAge() + ", '" + student.getCourse() + "'";
    }

    public String toUpdateQuery(Identifiable object) {
        Student student = (Student) object;
        return "UPDATE student SET name = '" + student.getName() + "', age = " + student.getAge() + ", course = '" + student.getCourse() + "' WHERE id = " + student.getId();
    }

    public Student create(JSONObject row) {

        int id = Integer.parseInt(row.get("id").toString());
        String name = row.get("name").toString();
        int age = Integer.parseInt(row.get("age").toString());
        String course = row.get("course").toString();

        return new Student(id, name, age, course);
    }

    @Override
    public JSONObject toJSONObject(Identifiable object) {
        JSONObject jsonObject = new JSONObject();

        Student student = (Student) object;

        jsonObject.put("id", student.getId());
        jsonObject.put("name", student.getName());
        jsonObject.put("age", student.getAge());
        jsonObject.put("course", student.getCourse());

        return jsonObject;
    }

    @Override
    public String createTableQuery() {
        return "CREATE TABLE IF NOT EXISTS student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, course TEXT)";
    }
}
