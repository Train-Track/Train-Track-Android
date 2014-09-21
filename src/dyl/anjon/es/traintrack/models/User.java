package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;
import dyl.anjon.es.traintrack.utils.Utils;

public class User {

	public static final String TABLE_NAME = "users";

	private int id;
	private String name;
	private String facebookId;
	private boolean friend;

	public User(String name) {
		this.setId(0);
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
	public static User get(int id) {
		Utils session = Utils.getSession();
		DatabaseHandler dbh = new DatabaseHandler(session.getContext());
		SQLiteDatabase db = dbh.getReadableDatabase();
		User user = null;
		Cursor cursor = db.query("users", new String[] { "id", "name",
				"facebook_id", "friend" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				user = new User(cursor.getString(1));
				user.setId(cursor.getInt(0));
				user.setFacebookId(cursor.getString(2));
				user.setFriend(cursor.getInt(3) == 1);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return user;
	}

	/**
	 * @param context
	 * @return all users who are friends
	 */
	public static ArrayList<User> getAllFriends() {
		ArrayList<User> users = new ArrayList<User>();

		DatabaseHandler dbh = new DatabaseHandler();
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

	public User save() {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", this.getName());
		values.put("facebook_id", this.getFacebookId());

		if (this.getId() != 0) {
			db.update("users", values, "id = ?",
					new String[] { String.valueOf(this.getId()) });
		} else {
			long id = db.insert("users", null, values);
			if (id > 0) {
				this.setId((int) id);
			}
		}

		return this;
	}

}
