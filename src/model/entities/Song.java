package model.entities;

import java.io.Serializable;

import model.interfaces.Identifiable;

public class Song implements Serializable, Identifiable{

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String author;
	private String album;
	
	
	public Song(int id, String name, String author, String album) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.album = album;
	}
	
	public Song() {
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
	
	@Override
	public String toString() {
		return "Song " + id + ":\nName: " + name + "\nAuthor: " + author + "\nAlbum: " + album;
	}
	
}
