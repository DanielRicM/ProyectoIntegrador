package controller;

import java.util.Scanner;

import model.entities.Song;
import model.entities.Student;
import model.interfaces.Identifiable;

public class SongInputHandler implements InputHandler{

	private Scanner entrada;
	
	public SongInputHandler() {
		entrada = new Scanner(System.in);
	}
	
	@Override
	public Song getDetails() {
		return getDetails(null);
	}

	@Override
	public Song getDetails(Identifiable object) {
	    Song song = (object instanceof Song) ? (Song) object : null;

	    System.out.println("Introduce el nombre: ");
	    String name = entrada.nextLine();
	    if (name.isBlank() && song == null) {
	        System.out.println("El nombre es obligatorio.");
	        return getDetails(object); // Restart input for creation.
	    }

	    System.out.println("Introduce el nombre del autor: ");
	    String author = entrada.nextLine();
	    if (author.isBlank() && song == null) {
	        System.out.println("El nombre del autor es obligatorio.");
	        return getDetails(object); // Restart input for creation.
	    }

	    System.out.println("Introduce el álbum: ");
	    String album = entrada.nextLine();
	    if (album.isBlank() && song == null) {
	        System.out.println("El nombre del álbum es obligatorio.");
	        return getDetails(object); // Restart input for creation.
	    }

	    // If modifying, retain original values if input is blank.
	    if (song != null) {
	        if (!name.isBlank()) song.setName(name);
	        if (!author.isBlank()) song.setAuthor(author);
	        if (!album.isBlank()) song.setAlbum(album);
	        return song;
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
	    return new Song(id, name, author, album);
	}


}
