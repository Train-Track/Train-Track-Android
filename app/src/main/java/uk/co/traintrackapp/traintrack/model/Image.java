package uk.co.traintrackapp.traintrack.model;

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
     * @param title
     *            the title of the image
     */
    public void setTitle(String title) {
        put("title", title);
    }

    /**
     * @return the user
     */
    public ParseUser getUser() {
        return getParseUser("user");
    }

    /**
     * @param user
     *            the owner
     */
    public void setUser(ParseUser user) {
        put("user", user);
    }

    /**
     * @return the file
     */
    public ParseFile getFile() {
        return getParseFile("file");
    }

    /**
     * @param file
     *            the data file
     */
    public void setFile(ParseFile file) {
        put("file", file);
    }

    /**
     * @return the title
     */
    public String toString() {
        return getTitle();
    }

}