package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Friend {

	private int id;
	private String name;

	public Friend(String name) {
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
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @param context
	 * @param id
	 * @return the friend selected
	 */
	public static Friend get(Context context, int id) {
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

		Friend friend = new Friend(cursor.getString(1));
		friend.setId(cursor.getInt(0));
		dbh.close();

		return friend;
	}

	/**
	 * @param context
	 * @return all friends
	 */
	public static ArrayList<Friend> getAll(Context context) {
		ArrayList<Friend> friends = new ArrayList<Friend>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM friends ORDER BY name ASC",
				null);
		if (cursor.moveToFirst()) {
			do {
				Friend friend = new Friend(cursor.getString(1));
				friend.setId(cursor.getInt(0));
				friends.add(friend);
			} while (cursor.moveToNext());
		}
		dbh.close();

		return friends;
	}

}
