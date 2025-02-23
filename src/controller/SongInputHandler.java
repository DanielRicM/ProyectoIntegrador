package controller;

import java.util.Scanner;

import model.entities.Song;
import model.interfaces.Identifiable;

public class SongInputHandler implements InputHandler {

    private final Scanner scanner;

    public SongInputHandler() {
        scanner = new Scanner(System.in);
    }

    @Override
    public Song getDetails(Identifiable object) {
        Song song;
        if (object instanceof Song s) {
            song = s;
        } else {
            song = null;
        }

        String name = getNonEmptyInput("Enter the name: ", song == null);
        String author = getNonEmptyInput("Enter the author's name: ", song == null);
        String album = getNonEmptyInput("Enter the album's name: ", song == null);

        if (song != null) {
            if (!name.isBlank()) song.setName(name);
            if (!author.isBlank()) song.setAuthor(author);
            if (!album.isBlank()) song.setAlbum(album);
            return song;
        }

        int id = getValidId();
        return new Song(id, name, author, album);
    }

    private String getNonEmptyInput(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank() || !required) {
                return input;
            }
            System.out.println("This field is mandatory.");
        }
    }

    private int getValidId() {
        while (true) {
            System.out.print("Enter the id: ");
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ID is not valid. Please enter a number.");
            }
        }
    }


}
