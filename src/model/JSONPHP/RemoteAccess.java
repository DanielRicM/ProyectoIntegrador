package model.JSONPHP;

public class RemoteAccess  {
	private String SERVER_PATH, GET_STUDENT, SET_STUDENT, GET_SONG ,SET_SONG;
	
	
	public RemoteAccess() {


		SERVER_PATH = "http://localhost/Irene/BaloncestoJSONServer/";
		GET_STUDENT = "readStudents.php";
		SET_STUDENT = "writeStudent.php";
		GET_SONG = "readSongs.php";
		SET_SONG = "writeSong.php";

	}

}
