package controller;

import java.util.Scanner;

import model.entities.Song;
import model.interfaces.Identifiable;

public class SongInputHandler implements InputHandler {

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

		System.out.print("Enter the name: ");
		String name = entrada.nextLine();
		if (name.isBlank() && song == null) {
			System.out.print("The name is mandatory.");
			return getDetails(object);
		}

		System.out.print("Enter the author's name: ");
		String author = entrada.nextLine();
		if (author.isBlank() && song == null) {
			System.out.println("The author's name is mandatory.");
			return getDetails(object);
		}

		System.out.print("Enter the album's name: ");
		String album = entrada.nextLine();
		if (album.isBlank() && song == null) {
			System.out.println("The album's name is mandatory.");
			return getDetails(object);
		}

		if (song != null) {
			if (!name.isBlank())
				song.setName(name);
			if (!author.isBlank())
				song.setAuthor(author);
			if (!album.isBlank())
				song.setAlbum(album);
			return song;
		}

		int id = 0;
		while (id == 0) {
			System.out.println("Enter the id: ");
			String idInput = entrada.nextLine();
			try {
				id = Integer.parseInt(idInput);
			} catch (NumberFormatException e) {
				System.out.println("ID is not valid. Please enter a number.");
			}
		}
		return new Song(id, name, author, album);
	}

}
