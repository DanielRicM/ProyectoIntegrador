package model.entities;

import java.io.Serializable;

import model.interfaces.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Student implements Serializable, Identifiable {

	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private int age;
	private String course;

	public Student(int id, String name, int age, String course) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.course = course;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Student() {
	}

	@Override
	public String toString() {
		return "Student " + id + ":\nname: " + name + "\nage: " + age + "\ncourse: " + course + "\n";
	}

}
