package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class User {

	private int id;
	private String name;
	private String facebookId;
	private boolean friend;

	public User(String name) {
		this.setName(name);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return facebookId;
	}

	/**
	 * @param facebookId
	 *            the facebookId to set
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	/**
	 * @return the friend
	 */
	public boolean isFriend() {
		return friend;
	}

	/**
	 * @param friend
	 *            the friend to set
	 */
	public void setFriend(boolean friend) {
		this.friend = friend;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @param context
	 * @param id
	 * @return the user selected
	 */
	public static User get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("friends", new String[] { "id", "name" },
				"id = ?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		User user = new User(cursor.getString(1));
		user.setId(cursor.getInt(0));
		cursor.close();
		dbh.close();

		return user;
	}

	/**
	 * @param context
	 * @return all users who are friends
	 */
	public static ArrayList<User> getAllFriends(Context context) {
		ArrayList<User> users = new ArrayList<User>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery(
				"SELECT * FROM users WHERE friend = 1 ORDER BY name ASC", null);
		if (cursor.moveToFirst()) {
			do {
				User user = new User(cursor.getString(1));
				user.setId(cursor.getInt(0));
				users.add(user);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return users;
	}

}
