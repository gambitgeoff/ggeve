package com.blogspot.gambitgeoff.ggeve;

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
	private static final String DATABASE_TABLE = "mainTable";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;

	private SQLiteDatabase myDb;
	private final Context myContext;
	private DbHelper myDbHelper;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_NAME
			+ " text not null);";

	public GGEveDBAdapter(Context inContext)
	{
		myContext = inContext;
		myDbHelper = new DbHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public GGEveDBAdapter open() throws SQLException
	{
		myDb = myDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		myDb.close();
	}
	
	public long insertEntry(EveCharacter inEveCharacter)
	{
		ContentValues contentValues = new ContentValues();
		return myDb.insert(DATABASE_TABLE, null, contentValues);
	}
	
	public boolean removeEntry(long inRowIndex)
	{
		return myDb.delete(DATABASE_TABLE, KEY_ID + "=" + inRowIndex, null) > 0;
	}
	
	public Cursor getAllEntries()
	{
		return myDb.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME}, null,null,null,null,null);
	}
	
	public EveCharacter getEntry(long inRowIndex)
	{
		EveCharacter objectInstance = new EveCharacter("",-1);
		return objectInstance;
	}
	
	public int updateEntry(long inRowIndex, EveCharacter inEveCharacter)
	{
		String where = KEY_ID + "=" + inRowIndex;
		ContentValues contentValues = new ContentValues();
		// TODO fill in the ContentValue based on the new object
		return myDb.update(DATABASE_TABLE, contentValues, where, null);

	}
	
	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context inContext, String inName,
				CursorFactory inFactory, int inVersion) {
			super(inContext, inName, inFactory, inVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
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
