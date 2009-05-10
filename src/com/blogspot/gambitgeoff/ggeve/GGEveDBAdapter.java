package com.blogspot.gambitgeoff.ggeve;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class GGEveDBAdapter {

	private static final String DATABASE_NAME = "ggeve.db";
	private static final String DATABASE_TABLE = "characterTable";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ID = "_id";

	public static final int COLUMN_CHARACTER_NAME = 1;
	public static final int COLUMN_CHARACTER_RACE = 2;
	public static final int COLUMN_CHARACTER_ID = 3;
	public static final int COLUMN_CHARACTER_BLOODLINE = 4;
	public static final int COLUMN_CHARACTER_GENDER = 5;
	public static final int COLUMN_CHARACTER_CORP_NAME = 6;
	public static final int COLUMN_CHARACTER_CORP_ID = 7;
	public static final int COLUMN_CHARACTER_BALANCE = 8;

	private SQLiteDatabase myDb;
	private final Context myContext;
	private DbHelper myDbHelper;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EveCharacter.KEY_CHARACTER_NAME + " TEXT NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_RACE + " TEXT, "
			+ EveCharacter.KEY_CHARACTER_ID + " INTEGER NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_BLOODLINE + " TEXT, "
			+ EveCharacter.KEY_CHARACTER_GENDER + " TEXT, "
			+ EveCharacter.KEY_CHARACTER_CORP_NAME + " TEXT NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_CORP_ID + " INTEGER NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_BALANCE + " INTEGER);";

	public GGEveDBAdapter(Context inContext) {
		myContext = inContext;
		myDbHelper = new DbHelper(myContext, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public GGEveDBAdapter open() throws SQLException {
		myDb = myDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDb.close();
	}

	private long insertEveCharacter(EveCharacter inEveCharacter) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(EveCharacter.KEY_CHARACTER_NAME, inEveCharacter
				.getCharacterName());
		contentValues.put(EveCharacter.KEY_CHARACTER_ID, inEveCharacter
				.getCharacterID());
		contentValues.put(EveCharacter.KEY_CHARACTER_BLOODLINE, inEveCharacter
				.getBloodline());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_ID, inEveCharacter
				.getCorporationID());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_NAME, inEveCharacter
				.getCorporationName());
		contentValues.put(EveCharacter.KEY_CHARACTER_GENDER, inEveCharacter
				.getGender());
		contentValues.put(EveCharacter.KEY_CHARACTER_RACE, inEveCharacter
				.getRace());
		contentValues.put(EveCharacter.KEY_CHARACTER_BALANCE, inEveCharacter
				.getBalance());
		return myDb.insert(DATABASE_TABLE, null, contentValues);
	}

	public boolean removeEveCharacter(String inCharacterName) {
		return myDb.delete(DATABASE_TABLE, EveCharacter.KEY_CHARACTER_NAME + "='" + inCharacterName + "'", null) > 0;
	}

	private Cursor getAllEntries() {
		return myDb
				.query(DATABASE_TABLE, new String[] { KEY_ID,
						EveCharacter.KEY_CHARACTER_NAME }, null, null, null,
						null, null);
	}

	public EveCharacter getEveCharacter(String inEveCharacterName) {
		String query = EveCharacter.KEY_CHARACTER_NAME + "="
				+ inEveCharacterName;
		Cursor cursor = myDb.query(DATABASE_TABLE, null, query, null, null,
				null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("Eve Character with name: "
					+ inEveCharacterName + " does not exist!");
		}

		String myName = cursor.getString(COLUMN_CHARACTER_NAME);
		String myRace = cursor.getString(COLUMN_CHARACTER_RACE);
		String myBloodline = cursor.getString(COLUMN_CHARACTER_BLOODLINE);
		String myGender = cursor.getString(COLUMN_CHARACTER_GENDER);
		String myCorporationName = cursor.getString(COLUMN_CHARACTER_CORP_NAME);
		int myCharacterID = cursor.getInt(COLUMN_CHARACTER_ID);
		int myCorporationID = cursor.getInt(COLUMN_CHARACTER_CORP_ID);
		int myBalance = cursor.getInt(COLUMN_CHARACTER_BALANCE);
		return new EveCharacter(myName, myCharacterID, myRace, myBloodline,
				myGender, myCorporationName, myCorporationID, myBalance);
	}
	
	public void addEveCharacter(EveCharacter inEveCharacter)
	{
		//check if already exists.
		//if exists, then update it otherwise just add it in new.
		String STATEMENT = EveCharacter.KEY_CHARACTER_NAME + "='" + inEveCharacter.getCharacterName()+"'";
		Cursor cursor = myDb.query(DATABASE_TABLE, null, STATEMENT, null, null, null, null, null);
		if (cursor.getCount()==0 || !cursor.moveToFirst())
		{
			insertEveCharacter(inEveCharacter);
		}
		else
		{
			updateEveCharacter(inEveCharacter);
		}
	}

	public Vector<EveCharacter> getEveCharacters() {
		Vector<EveCharacter> returnValue = new Vector<EveCharacter>();
		Cursor cursor = getAllEntries();
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String myName = cursor.getString(COLUMN_CHARACTER_NAME);
			String myRace = cursor.getString(COLUMN_CHARACTER_RACE);
			String myBloodline = cursor.getString(COLUMN_CHARACTER_BLOODLINE);
			String myGender = cursor.getString(COLUMN_CHARACTER_GENDER);
			String myCorporationName = cursor.getString(COLUMN_CHARACTER_CORP_NAME);
			int myCharacterID = cursor.getInt(COLUMN_CHARACTER_ID);
			int myCorporationID = cursor.getInt(COLUMN_CHARACTER_CORP_ID);
			int myBalance = cursor.getInt(COLUMN_CHARACTER_BALANCE);
			returnValue.add(new EveCharacter(myName, myCharacterID, myRace,
					myBloodline, myGender, myCorporationName, myCorporationID,
					myBalance));
		}
		return returnValue;
	}

	private int updateEveCharacter(EveCharacter inEveCharacter) {
		String where = EveCharacter.KEY_CHARACTER_NAME + "=" + inEveCharacter.getCharacterName();
		ContentValues contentValues = new ContentValues();
		contentValues.put(EveCharacter.KEY_CHARACTER_NAME, inEveCharacter
				.getCharacterName());
		contentValues.put(EveCharacter.KEY_CHARACTER_ID, inEveCharacter
				.getCharacterID());
		contentValues.put(EveCharacter.KEY_CHARACTER_BLOODLINE, inEveCharacter
				.getBloodline());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_ID, inEveCharacter
				.getCorporationID());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_NAME, inEveCharacter
				.getCorporationName());
		contentValues.put(EveCharacter.KEY_CHARACTER_GENDER, inEveCharacter
				.getGender());
		contentValues.put(EveCharacter.KEY_CHARACTER_RACE, inEveCharacter
				.getRace());
		contentValues.put(EveCharacter.KEY_CHARACTER_BALANCE, inEveCharacter
				.getBalance());
		return myDb.update(DATABASE_TABLE, contentValues, where, null);

	}

	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context inContext, String inName,
				CursorFactory inFactory, int inVersion) {
			super(inContext, inName, inFactory, inVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log
					.w("GGEveDBAdapter", "Upgrading from version " + oldVersion
							+ " to " + newVersion
							+ ", which will destroy all old data");
			// Upgrade the existing database to conform to the new version.
			// Multiple previous versions can be handled by comparing
			// _oldVersion and _newVersion values.
			// The simplest case is to drop the old table and create a
			// new one.
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(db);

		}
	}
}
