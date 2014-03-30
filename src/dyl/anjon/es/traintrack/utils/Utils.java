package dyl.anjon.es.traintrack.utils;

import android.util.Log;
import dyl.anjon.es.traintrack.models.User;

public class Utils {

	private static Utils instance;
	private User user;

	private Utils() {
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public boolean isLoggedIn() {
		return this.getUser() != null;
	}

	/**
	 * @return the session currently stored
	 */
	public static synchronized Utils getSession() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	/**
	 * @param message
	 *            the log message to write
	 */
	public static void log(String message) {
		Log.i("TrainTrack", message);
	}

}
