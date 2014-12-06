package dyl.anjon.es.traintrack.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Image")
public class Image extends ParseObject {

	public Image() {
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return getString("title");
	}

	/**
		put("title", title);
	 * @return the user
	 */
	public ParseUser getUser() {
		return getParseUser("user");
	}

	/**
	 * @return the file
	 */
	public ParseFile getFile() {
		return getParseFile("file");
	}

	/**
	 * @return the title
	 */
	public String toString() {
		return getTitle();
	}

}