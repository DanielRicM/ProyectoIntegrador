package model.factory;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.json.simple.JSONObject;

import model.entities.Song;
import model.entities.Student;
import model.interfaces.Identifiable;

public class SongFactory implements ObjFactory<Identifiable> {

	@Override
	public Song create(String line) {
		String[] parts = line.split(";");
		int id = Integer.parseInt(parts[0]);
		String name = parts[1];
		String author = parts[2];
		String album = parts[3];

		return new Song(id, name, author, album);
	}

	@Override
	public String toCSV(Identifiable object) {
		Song song = (Song) object;
		return song.getId() + ";" + song.getName() + ";" + song.getAuthor() + ";" + song.getAlbum();
	}

	@Override
	public Song create(Element songElement) {
		int id = Integer.parseInt(songElement.getAttributeValue("id"));
		String name = songElement.getChildText("name");
		String author = songElement.getChildText("author");
		String album = songElement.getChildText("album");

		Song song = new Song(id, name, author, album);
		return song;
	}

	@Override
	public Element toXML(Identifiable object) {
		Song song = (Song) object;
		Element songElement = new Element("song");
		songElement.setAttribute(new Attribute("id", String.valueOf(song.getId())));

		Element name = new Element("name").setText(song.getName());
		Element author = new Element("author").setText(song.getAuthor());
		Element album = new Element("album").setText(song.getAlbum());

		songElement.addContent(name);
		songElement.addContent(author);
		songElement.addContent(album);
		return songElement;
	}

	@Override
	public String toQuery(Identifiable object) {
		Song song = (Song) object;
		String queryValues = String.valueOf(song.getId()) + ", '" + song.getName() + "', '" + song.getAuthor() + "', '"
				+ song.getAlbum() + "'";
		return queryValues;
	}

	@Override
	public String toUpdateQuery(Identifiable object) {
		Song song = (Song) object;
		return "UPDATE song SET name = '" + song.getName() + "', author = '" + song.getAuthor() + "', album = '"
				+ song.getAlbum() + "' WHERE id = " + song.getId();
	}
	
	public Song create(JSONObject row) {
		
		int id = Integer.parseInt(row.get("id").toString());
		String name = row.get("name").toString();
		String author = row.get("author").toString();
		String album = row.get("album").toString();
		
		return new Song(id,name,author,album);
		
		
	}

	@Override
	public JSONObject toJSONObject(Identifiable object) {
		JSONObject jsonObject= new JSONObject();
		
		Song song = (Song) object;
		
		jsonObject.put("id",song.getId());
		jsonObject.put("name",song.getName());
		jsonObject.put("author", song.getAuthor());
		jsonObject.put("album", song.getAlbum());
		
		return jsonObject;
	}

	@Override
	public String createTable() {
		String query = "CREATE TABLE IF NOT EXISTS song (\n" +
				"    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
				"    name TEXT,\n" +
				"    author TEXT,\n" +
				"    album TEXT\n" +
				")";
		return query;
	}

}
