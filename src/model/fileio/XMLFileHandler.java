package model.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.entities.Student;

public class XMLFileHandler<T> extends FileHandler<Student> {

    public XMLFileHandler(File file) throws IOException {
        super(file);
    }

    @Override
    protected Map<Integer, Student> initialReadObjects() throws IOException {
        Map<Integer, Student> studentMap = new HashMap<>();
        if (file.length() == 0) {
            return studentMap;
        }
        
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);
            Element rootElement = document.getRootElement();
            
            List<Element> studentList = rootElement.getChildren("student");
            for (Element studentElement : studentList) {
                int id = Integer.parseInt(studentElement.getAttributeValue("id"));
                String name = studentElement.getChildText("name");
                int age = Integer.parseInt(studentElement.getChildText("age"));
                String course = studentElement.getChildText("course");

                Student student = new Student(id, name, age, course);
                studentMap.put(id, student);
            }
        } catch (Exception e) {
            throw new IOException("Error parsing XML file", e);
        }
        
        return studentMap;
    }

    @Override
    protected void finalWriteObjects() throws IOException {
        Element rootElement = new Element("students");
        Document document = new Document(rootElement);
        
        for (Student student : map.values()) {
            Element studentElement = new Element("student");
            studentElement.setAttribute(new Attribute("id", String.valueOf(student.getId())));
            
            Element name = new Element("name").setText(student.getName());
            Element age = new Element("age").setText(String.valueOf(student.getAge()));
            Element course = new Element("course").setText(student.getCourse());

            studentElement.addContent(name);
            studentElement.addContent(age);
            studentElement.addContent(course);
            
            rootElement.addContent(studentElement);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document, fos);
        }
    }
}
