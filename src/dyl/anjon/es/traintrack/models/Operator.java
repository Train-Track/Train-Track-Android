package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Operator {

	public static final String TABLE_NAME = "operators";

	private int id;

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("delay_repay_url")
	private String delayRepayUrl;

	public Operator() {
		this.setCode("");
		this.setName("Unknown");
		this.setDelayRepayUrl("");
	}

	/**
	 * @param code
	 * @param name
	 * @param delayRepayUrl
	 */
	public Operator(String code, String name, String delayRepayUrl) {
		this.setCode(code);
		this.setName(name);
		this.setDelayRepayUrl(delayRepayUrl);
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the delayRepayUrl
	 */
	public String getDelayRepayUrl() {
		return delayRepayUrl;
	}

	/**
	 * @param delayRepayUrl
	 *            the delayRepayUrl to set
	 */
	public void setDelayRepayUrl(String delayRepayUrl) {
		this.delayRepayUrl = delayRepayUrl;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @return true or false
	 */
	@Override
	public boolean equals(Object operator) {
		if (this.toString().equals(operator.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param context
	 * @param id
	 * @return the operator selected
	 */
	public static Operator get(int id) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "code",
				"name", "delay_repay_url" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Operator operator = new Operator();
		operator.setId(cursor.getInt(0));
		operator.setCode(cursor.getString(1));
		operator.setName(cursor.getString(2));
		operator.setDelayRepayUrl(cursor.getString(3));
		cursor.close();
		dbh.close();

		return operator;
	}

	/**
	 * @param context
	 * @param id
	 * @return the operator selected
	 */
	public static Operator getByCode(String code) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "code",
				"name", "delay_repay_url" }, "code = ?",
				new String[] { String.valueOf(code) }, null, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Operator operator = new Operator();
		operator.setId(cursor.getInt(0));
		operator.setCode(cursor.getString(1));
		operator.setName(cursor.getString(2));
		operator.setDelayRepayUrl(cursor.getString(3));
		cursor.close();
		dbh.close();

		return operator;
	}

	/**
	 * @param context
	 * @return all operators
	 */
	public static ArrayList<Operator> getAll() {
		ArrayList<Operator> operators = new ArrayList<Operator>();

		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " ORDER BY name ASC", null);
		if (cursor.moveToFirst()) {
			do {
				Operator operator = new Operator();
				operator.setId(cursor.getInt(0));
				operator.setCode(cursor.getString(1));
				operator.setName(cursor.getString(2));
				operator.setDelayRepayUrl(cursor.getString(3));
				operators.add(operator);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();
		return operators;
	}

	public Operator save() {
		if (getId() != 0) {
			DatabaseHandler dbh = new DatabaseHandler();
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("code", getCode());
			values.put("name", getName());
			values.put("delay_repay_url", getDelayRepayUrl());
			db.update(TABLE_NAME, values, "id = ?",
					new String[] { String.valueOf(getId()) });
			dbh.close();
		}
		return this;
	}

}
