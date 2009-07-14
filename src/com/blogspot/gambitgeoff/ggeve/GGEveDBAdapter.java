package com.blogspot.gambitgeoff.ggeve;

import java.text.SimpleDateFormat;
import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.Skill;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillGroup;
import com.blogspot.gambitgeoff.ggeve.eveapi.SkillTree;

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
	private static final String DATABASE_CHARACTER_TABLE = "characterTable";
	private static final String DATABASE_ACCOUNTS_TABLE = "accountTable";
	private static final String DATABASE_TRAINING_INFO_TABLE = "trainingInfoTable";
	private static final String DATABASE_SERVER_STATUS_TABLE = "serverStatusTable";
	private static final String DATABASE_SKILL_TABLE = "skillTable";
	private static final String DATABASE_SKILL_GROUP_TABLE = "skillGroupTable";
	private static final int DATABASE_VERSION = 1;

	private static final String KEY_TABLE_ID = "_id";

	public static final int COLUMN_SERVER_STATUS_ONLINE = 1;
	public static final int COLUMN_SERVER_STATUS_NUMPLAYERS = 2;
	public static final int COLUMN_SERVER_STATUS_CURRTIME = 3;
	public static final int COLUMN_SERVER_STATUS_CACHETIME = 4;

	public static final int COLUMN_CHARACTER_NAME = 1;
	public static final int COLUMN_CHARACTER_RACE = 2;
	public static final int COLUMN_CHARACTER_ID = 3;
	public static final int COLUMN_CHARACTER_BLOODLINE = 4;
	public static final int COLUMN_CHARACTER_GENDER = 5;
	public static final int COLUMN_CHARACTER_CORP_NAME = 6;
	public static final int COLUMN_CHARACTER_CORP_ID = 7;
	public static final int COLUMN_CHARACTER_BALANCE = 8;
	public static final int COLUMN_CHARACTER_USERID = 9; // a reference to the
	// userid which
	// "owns" this
	// character.
	public static final int COLUMN_CHARACTER_TRAIN_END_TIME = 10;
	public static final int COLUMN_CHARACTER_TRAIN_SKILL = 11;

	public static final int COLUMN_ACCOUNT_USERID = 1;
	public static final int COLUMN_ACCOUNT_PUBLIC_KEY = 2;
	public static final int COLUMN_ACCOUNT_PRIVATE_KEY = 3;

	public static final int COLUMN_TRAININFO_CACHED_UNTIL = 1;
	public static final int COLUMN_TRAININFO_CHARACTERID = 2;
	public static final int COLUMN_TRAININFO_CURRENT_TIME = 3;
	public static final int COLUMN_TRAININFO_SKILL_IN_TRAINING = 4;
	public static final int COLUMN_TRAININFO_ENDSP = 5;
	public static final int COLUMN_TRAININFO_ENDTIME = 6;
	public static final int COLUMN_TRAININFO_STARTSP = 7;
	public static final int COLUMN_TRAININFO_STARTTIME = 8;
	public static final int COLUMN_TRAININFO_TOLEVEL = 9;
	public static final int COLUMN_TRAININFO_TYPEID = 10;
	public static final int COLUMN_TRAININFO_TQTIME = 11;
	public static final int COLUMN_TRAININFO_TQTIME_OFFSET = 12;
	
	private static final int COLUMN_SKILL_GROUP_GROUPNAME = 1;
	private static final int COLUMN_SKILL_GROUP_GROUPID = 2;
	
	private static final int COLUMN_SKILL_NAME = 1;
	private static final int COLUMN_SKILL_GROUP_ID = 2;
	private static final int COLUMN_SKILL_TYPE_ID = 3;

	private SQLiteDatabase myDb;
	private final Context myContext;
	private DbHelper myDbHelper;
	
	private static final String DATABASE_CREATE_SKILLS_GROUP_TABLE = "create table " + DATABASE_SKILL_GROUP_TABLE + " ("
			+ KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SkillGroup.SKILLGROUP_NAME + " TEXT NOT NULL, "
			+ SkillGroup.SKILLGROUP_ID + " INTEGER NOT NULL);";
	
	private static final String DATABASE_CREATE_SKILLS_TABLE = "create table " + DATABASE_SKILL_TABLE + " ("
			+ KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Skill.TYPE_NAME + " TEXT NOT NULL, "
			+ Skill.GROUP_ID + " INTEGER NOT NULL, " + Skill.TYPE_ID + " INTEGER NOT NULL);";

	private static final String DATABASE_CREATE_SERVER_STATUS_TABLE = "create table " + DATABASE_SERVER_STATUS_TABLE + " ("
			+ KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + StatusInformation.KEY_IS_ONLINE + " TEXT NOT NULL, "
			+ StatusInformation.KEY_NUM_PLAYERS + " INTEGER NOT NULL, " + StatusInformation.KEY_CURRTIME + " TEXT NOT NULL, "
			+ StatusInformation.KEY_CACHETIME + " TEXT NOT NULL);";

	private static final String DATABASE_CREATE_CHARACTER_TABLE = "create table " + DATABASE_CHARACTER_TABLE + " (" + KEY_TABLE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + EveCharacter.KEY_CHARACTER_NAME + " TEXT NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_RACE + " TEXT, " + EveCharacter.KEY_CHARACTER_ID + " INTEGER NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_BLOODLINE + " TEXT, " + EveCharacter.KEY_CHARACTER_GENDER + " TEXT, "
			+ EveCharacter.KEY_CHARACTER_CORP_NAME + " TEXT NOT NULL, " + EveCharacter.KEY_CHARACTER_CORP_ID + " INTEGER NOT NULL, "
			+ EveCharacter.KEY_CHARACTER_BALANCE + " INTEGER, " + EveCharacter.KEY_CHARACTER_USERID + " INTEGER);";

	private static final String DATABASE_CREATE_ACCOUNTS_TABLE = "create table " + DATABASE_ACCOUNTS_TABLE + " (" + KEY_TABLE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + AccountDetails.KEY_ACCOUNT_USERID + " TEXT NOT NULL, "
			+ AccountDetails.KEY_ACCOUNT_APIKEY + " TEXT NOT NULL," + AccountDetails.KEY_ACCOUNT_PRIVATE_KEY + " TEXT);";

	private static final String DATABASE_CREATE_TRAINING_INFO_TABLE = "create table " + DATABASE_TRAINING_INFO_TABLE + " ("
			+ KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TrainingInformation.KEY_CACHED_UNTIL + " TEXT,"
			+ TrainingInformation.KEY_CHARACTER_ID + " INTEGER NOT NULL," + TrainingInformation.KEY_CURRENT_TIME + " TEXT,"
			+ TrainingInformation.KEY_SKILL_IN_TRAINING + " TEXT," + TrainingInformation.KEY_TRAIN_END_SP + " TEXT,"
			+ TrainingInformation.KEY_TRAIN_END_TIME + " TEXT," + TrainingInformation.KEY_TRAIN_START_SP + " TEXT,"
			+ TrainingInformation.KEY_TRAIN_START_TIME + " TEXT," + TrainingInformation.KEY_TRAIN_TO_LEVEL + " TEXT,"
			+ TrainingInformation.KEY_TRAIN_TYPE_ID + " TEXT," + TrainingInformation.KEY_TQTIME + " INTEGER,"
			+ TrainingInformation.KEY_TQTIME_OFFSET + " INTEGER);";

	public GGEveDBAdapter(Context inContext) {
		myContext = inContext;
		myDbHelper = new DbHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void updateSkillTree(SkillTree inSkillTree)
	{
		Vector<SkillGroup> sgs = inSkillTree.getSkillGroups();
		for (SkillGroup sg: sgs)
		{
			updateSkillGroup(sg);
		}
	}
	
	private long updateSkillGroup(SkillGroup inSkillGroup)
	{
		ContentValues cv = new ContentValues();
		cv.put(SkillGroup.SKILLGROUP_NAME, inSkillGroup.getGroupName());
		cv.put(SkillGroup.SKILLGROUP_ID, inSkillGroup.getGroupID());
		Cursor c = myDb.query(DATABASE_SKILL_GROUP_TABLE, null, null, null, null, null, null);
		long returnValue;
		if (c.getCount() > 0) {
			//System.out.println("Updating Server Status");
			c.close();
			returnValue = myDb.update(DATABASE_SKILL_GROUP_TABLE, cv, null, null);
		}
		else
		{
			c.close();
			returnValue = myDb.insert(DATABASE_SKILL_GROUP_TABLE, null, cv);
		}
		Vector<Skill> tempSkills = inSkillGroup.getSkills();
		for(Skill s: tempSkills)
			updateSkill(s);
		
		return returnValue;
	}
	
	/**
	 * 
	 * @param inSkill
	 */
	private void updateSkill(Skill inSkill)
	{
		ContentValues cv = new ContentValues();
		cv.put(Skill.TYPE_NAME, inSkill.getName());
		cv.put(Skill.GROUP_ID, inSkill.getGroupID());
		String where = Skill.TYPE_ID + "=" + inSkill.getTypeID();
		cv.put(Skill.TYPE_ID, inSkill.getTypeID());
		Cursor c = myDb.query(DATABASE_SKILL_TABLE, null, where, null, null, null, null);
		if (c.getCount() > 0) {
			c.close();
			myDb.update(DATABASE_SKILL_TABLE, cv, where, null);
		}
		else
		{
			c.close();
			myDb.insert(DATABASE_SKILL_TABLE, null, cv);
		}
	}
	
	public String getSkillName(int inSkillID)
	{
		String returnValue = null;
		String whereStatement = Skill.TYPE_ID + "=" + inSkillID;
		Cursor c = myDb.query(DATABASE_SKILL_TABLE, null, whereStatement, null, null, null, null);
		if (c.moveToFirst())
		{
			returnValue = c.getString(COLUMN_SKILL_NAME);
		}
		c.close();
		return returnValue;
	}
	
	public SkillTree getSkillTree()
	{
		SkillTree returnValue = new SkillTree();
		Vector<SkillGroup> skillGroups = getSkillGroups();
		Vector<Skill> skills = getSkills();
		for (SkillGroup sg: skillGroups)
		{
			returnValue.addSkillGroup(sg);
		}
		for (Skill s: skills)
		{
			returnValue.addSkill(s);
		}
		return returnValue;
	}
	
	private Vector<Skill> getSkills()
	{
		Vector <Skill> returnValue = new Vector<Skill>();
		Cursor c = myDb.query(DATABASE_SKILL_TABLE, null, null, null, null, null, null);
		boolean keepGoing = c.moveToFirst();
		while(keepGoing) {
			try {
				String skillName = c.getString(COLUMN_SKILL_NAME);
				int groupID = c.getInt(COLUMN_SKILL_GROUP_ID);
				int typeID = c.getInt(COLUMN_SKILL_TYPE_ID);
				Skill s = new Skill(skillName, groupID, typeID);
				returnValue.add(s);
				keepGoing = c.moveToNext();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		c.close();
		return returnValue;
	}
	
	private Vector<SkillGroup> getSkillGroups()
	{
		Vector <SkillGroup> returnValue = new Vector<SkillGroup>();
		Cursor c = myDb.query(DATABASE_SKILL_GROUP_TABLE, null, null, null, null, null, null);
		boolean keepGoing = c.moveToFirst();
		while(keepGoing) {
			try {
				String groupName = c.getString(COLUMN_SKILL_GROUP_GROUPNAME);
				int groupID = c.getInt(COLUMN_SKILL_GROUP_GROUPID);
				SkillGroup sg = new SkillGroup(groupName, groupID);
				returnValue.add(sg);
				keepGoing = c.moveToNext();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		c.close();
		return returnValue;
	}

	public long updateServerStatus(StatusInformation inServerStatus) {
		SimpleDateFormat sdf = GGEveApplicationRunner.getEveDateFormatter();
		ContentValues contentValues = new ContentValues();
		contentValues.put(StatusInformation.KEY_IS_ONLINE, ""+inServerStatus.getIsOnline());
		contentValues.put(StatusInformation.KEY_NUM_PLAYERS, new Integer(inServerStatus.getNumberOfPlayers()));
		contentValues.put(StatusInformation.KEY_CURRTIME, sdf.format(inServerStatus.getCurrentTime()));
		contentValues.put(StatusInformation.KEY_CACHETIME, sdf.format(inServerStatus.getCachedUntilTime()));
		Cursor c = myDb.query(DATABASE_SERVER_STATUS_TABLE, null, null, null, null, null, null);
		if (c.getCount() > 0) {
			c.close();
			return myDb.update(DATABASE_SERVER_STATUS_TABLE, contentValues, null, null);
		}
		else
		{
			c.close();
			return myDb.insert(DATABASE_SERVER_STATUS_TABLE, null, contentValues);
		}
	}

	public StatusInformation getServerStatus() {
		SimpleDateFormat sdf = GGEveApplicationRunner.getEveDateFormatter();
		Cursor c = myDb.query(DATABASE_SERVER_STATUS_TABLE, null, null, null, null, null, null);
		if (c.moveToFirst()) {
			try {
				StatusInformation returnValue = new StatusInformation();
				returnValue.setNumberOfPlayers(c.getInt(COLUMN_SERVER_STATUS_NUMPLAYERS));
				returnValue.setCurrentTime(sdf.parse(c.getString(COLUMN_SERVER_STATUS_CURRTIME)));
				returnValue.setCachedUntilTime(sdf.parse(c.getString(COLUMN_SERVER_STATUS_CACHETIME)));
				returnValue.setIsOnline(Boolean.parseBoolean(c.getString(COLUMN_SERVER_STATUS_ONLINE)));
				c.close();
				return returnValue;
			} catch (java.text.ParseException pe) {
				pe.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		c.close();
		return null;
	}

	public GGEveDBAdapter open() throws SQLException {
		myDb = myDbHelper.getWritableDatabase();
		return this;
	}

	public void reset() {
		if (myDb != null)
			myDbHelper.onUpgrade(myDb, 1, 1);
	}

	public void close() {
		myDb.close();
	}

	public Vector<AccountDetails> getAccounts() {
		// returns a vector of all the current accounts linked to GGEve.
		Vector<AccountDetails> returnValue = new Vector<AccountDetails>();
		Cursor cursor = getAllAccounts();
		boolean keepGoing = cursor.moveToFirst();
		while (keepGoing) {
			int userid = cursor.getInt(COLUMN_ACCOUNT_USERID);
			String apikey = cursor.getString(COLUMN_ACCOUNT_PUBLIC_KEY);
			returnValue.add(new AccountDetails(userid, apikey, null));
			keepGoing = cursor.moveToNext();
		}
		cursor.close();
		return returnValue;
	}

	public AccountDetails getAccount(int inUserID) {
		if (getAccountExists(inUserID)) {
			String STATEMENT = AccountDetails.KEY_ACCOUNT_USERID + "=" + inUserID;
			Cursor cursor = myDb.query(DATABASE_ACCOUNTS_TABLE, null, STATEMENT, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				String apikey = cursor.getString(COLUMN_ACCOUNT_PUBLIC_KEY);
				String privatekey = cursor.getString(COLUMN_ACCOUNT_PRIVATE_KEY);
				cursor.close();
				return new AccountDetails(inUserID, apikey, privatekey);
			} else
				return null;
		} else
			return null;
	}

	public long updateAccountDetails(AccountDetails inAccountDetails) {
		boolean accountExists = getAccountExists(inAccountDetails.getUserID());
		ContentValues contentValues = new ContentValues();
		contentValues.put(AccountDetails.KEY_ACCOUNT_USERID, new Integer(inAccountDetails.getUserID()));
		contentValues.put(AccountDetails.KEY_ACCOUNT_APIKEY, inAccountDetails.getPublicAPIKey());
		contentValues.put(AccountDetails.KEY_ACCOUNT_PRIVATE_KEY, inAccountDetails.getPrivateAPIKey());
		if (accountExists) {
			//System.out.println("Account: " + inAccountDetails.getUserID() + " already exists.  Updating");
			String where = AccountDetails.KEY_ACCOUNT_USERID + "=" + inAccountDetails.getUserID();
			return myDb.update(DATABASE_ACCOUNTS_TABLE, contentValues, where, null);
		} else {
			//System.out.println("Account: " + inAccountDetails.getUserID() + " does not exist, adding new.");
			return myDb.insert(DATABASE_ACCOUNTS_TABLE, null, contentValues);
		}
	}

	private boolean getAccountExists(int inUserID) {
		String STATEMENT = AccountDetails.KEY_ACCOUNT_USERID + "=" + inUserID;
		Cursor cursor = myDb.query(DATABASE_ACCOUNTS_TABLE, null, STATEMENT, null, null, null, null, null);
		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}
	}

	private long addNewEveCharacter(EveCharacter inEveCharacter) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(EveCharacter.KEY_CHARACTER_NAME, inEveCharacter.getCharacterName());
		contentValues.put(EveCharacter.KEY_CHARACTER_ID, inEveCharacter.getCharacterID());
		contentValues.put(EveCharacter.KEY_CHARACTER_BLOODLINE, inEveCharacter.getBloodline());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_ID, inEveCharacter.getCorporationID());
		contentValues.put(EveCharacter.KEY_CHARACTER_CORP_NAME, inEveCharacter.getCorporationName());
		contentValues.put(EveCharacter.KEY_CHARACTER_GENDER, inEveCharacter.getGender());
		contentValues.put(EveCharacter.KEY_CHARACTER_RACE, inEveCharacter.getRace());
		contentValues.put(EveCharacter.KEY_CHARACTER_BALANCE, inEveCharacter.getBalance());
		contentValues.put(EveCharacter.KEY_CHARACTER_USERID, inEveCharacter.getUserID());
		long returnValue = myDb.insert(DATABASE_CHARACTER_TABLE, null, contentValues);
		TrainingInformation info = inEveCharacter.getTrainingInformation();
		if (info != null) {
			SimpleDateFormat formatter = GGEveApplicationRunner.getEveDateFormatter();
			contentValues = new ContentValues();
			contentValues.put(TrainingInformation.KEY_CACHED_UNTIL, formatter.format(info.getCachedUntil()));
			contentValues.put(TrainingInformation.KEY_CHARACTER_ID, info.getCharacterID());
			contentValues.put(TrainingInformation.KEY_CURRENT_TIME, formatter.format(info.getCurrentTime()));
			contentValues.put(TrainingInformation.KEY_SKILL_IN_TRAINING, info.getSkillInTraining());
			contentValues.put(TrainingInformation.KEY_TRAIN_END_SP, info.getTrainingDestinationSP());
			contentValues.put(TrainingInformation.KEY_TRAIN_END_TIME, formatter.format(info.getTrainingEndTime()));
			contentValues.put(TrainingInformation.KEY_TRAIN_START_SP, info.getTrainingStartSP());
			contentValues.put(TrainingInformation.KEY_TRAIN_START_TIME, formatter.format(info.getTrainingStartTime()));
			contentValues.put(TrainingInformation.KEY_TRAIN_TO_LEVEL, info.getTrainingToLevel());
			contentValues.put(TrainingInformation.KEY_TRAIN_TYPE_ID, info.getTrainingTypeID());
			contentValues.put(TrainingInformation.KEY_TQTIME, formatter.format(info.getTQTime()));
			contentValues.put(TrainingInformation.KEY_TQTIME_OFFSET, info.getTQTimeOffset());
			long returnValue2 = myDb.insert(DATABASE_TRAINING_INFO_TABLE, null, contentValues);
			if (returnValue2 == -1 || returnValue == -1)
				return -1;
		}

		return returnValue;
	}

	public boolean removeEveCharacter(String inCharacterName) {
		return myDb.delete(DATABASE_CHARACTER_TABLE, EveCharacter.KEY_CHARACTER_NAME + "='" + inCharacterName + "'", null) > 0;
	}

	private Cursor getAllCharacters() {
		return myDb.query(DATABASE_CHARACTER_TABLE, null, null, null, null, null, null);
	}

	public Cursor getAllAccounts() {
		return myDb.query(DATABASE_ACCOUNTS_TABLE, null, null, null, null, null, null);
	}

	public EveCharacter getEveCharacter(String inEveCharacterName) {
		String query = EveCharacter.KEY_CHARACTER_NAME + "=" + "'" + inEveCharacterName + "'";
		Cursor cursor = myDb.query(DATABASE_CHARACTER_TABLE, null, query, null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("Eve Character with name: " + inEveCharacterName + " does not exist!");
		}
		EveCharacter character = getEveCharacter(cursor);
		cursor.close();
		return character;
	}

	public EveCharacter getEveCharacter(int inEveCharacterID) {
		String query = EveCharacter.KEY_CHARACTER_ID + "=" + inEveCharacterID;
		Cursor cursor = myDb.query(DATABASE_CHARACTER_TABLE, null, query, null, null, null, null, null);
		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("Eve Character with name: " + inEveCharacterID + " does not exist!");
		}
		EveCharacter character = getEveCharacter(cursor);
		cursor.close();
		return character;
	}

	public void updateEveCharacter(EveCharacter inEveCharacter) {
		// check if already exists.
		// if exists, then update it otherwise just add it in new.
		String STATEMENT = EveCharacter.KEY_CHARACTER_NAME + "='" + inEveCharacter.getCharacterName() + "'";
		Cursor cursor = myDb.query(DATABASE_CHARACTER_TABLE, null, STATEMENT, null, null, null, null, null);
		updateTrainingInformation(inEveCharacter.getTrainingInformation());
		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			addNewEveCharacter(inEveCharacter);
			cursor.close();
		} else {
			cursor.close();
			updateNewEveCharacter(inEveCharacter);
		}
	}

	public Vector<EveCharacter> getEveCharacters() {
		Vector<EveCharacter> returnValue = new Vector<EveCharacter>();
		Cursor cursor = getAllCharacters();
		boolean keepGoing = cursor.moveToFirst();
		while (keepGoing) {
			returnValue.add(getEveCharacter(cursor));
			keepGoing = cursor.moveToNext();
		}
		cursor.close();
		return returnValue;
	}

	public long updateTrainingInformation(TrainingInformation inTrainingInformation) {
		if (inTrainingInformation == null)
			return -1;
		String STATEMENT = TrainingInformation.KEY_CHARACTER_ID + "=" + inTrainingInformation.getCharacterID();
		Cursor cursor = myDb.query(DATABASE_TRAINING_INFO_TABLE, null, STATEMENT, null, null, null, null, null);
		SimpleDateFormat fmt = GGEveApplicationRunner.getEveDateFormatter();
		ContentValues cv = new ContentValues();
		if (inTrainingInformation.getCachedUntil() != null)
			cv.put(TrainingInformation.KEY_CACHED_UNTIL, fmt.format(inTrainingInformation.getCachedUntil()));
		if (inTrainingInformation.getCharacterID() != -1)
			cv.put(TrainingInformation.KEY_CHARACTER_ID, inTrainingInformation.getCharacterID());
		if (inTrainingInformation.getCurrentTime() != null)
			cv.put(TrainingInformation.KEY_CURRENT_TIME, fmt.format(inTrainingInformation.getCurrentTime()));
		if (inTrainingInformation.getSkillInTraining() != -1)
			cv.put(TrainingInformation.KEY_SKILL_IN_TRAINING, inTrainingInformation.getSkillInTraining());
		if (inTrainingInformation.getTrainingTypeID() != -1)
			cv.put(TrainingInformation.KEY_TRAIN_TYPE_ID, inTrainingInformation.getTrainingTypeID());
		if (inTrainingInformation.getTrainingEndTime() != null)
			cv.put(TrainingInformation.KEY_TRAIN_END_TIME, fmt.format(inTrainingInformation.getTrainingEndTime()));
		if (inTrainingInformation.getTrainingStartTime() != null)
			cv.put(TrainingInformation.KEY_TRAIN_START_TIME, fmt.format(inTrainingInformation.getTrainingStartTime()));
		if (inTrainingInformation.getTrainingToLevel() != -1)
			cv.put(TrainingInformation.KEY_TRAIN_TO_LEVEL, inTrainingInformation.getTrainingToLevel());
		long returnValue;
		if (cursor.moveToFirst()) {
			// update the current entry.
			returnValue = myDb.update(DATABASE_TRAINING_INFO_TABLE, cv, STATEMENT, null);
		} else {
			// add a new entry.
			returnValue = myDb.insert(DATABASE_TRAINING_INFO_TABLE, null, cv);
		}
		cursor.close();
		return returnValue;
	}

	private TrainingInformation getTrainingInformation(int inEveCharacterID) {
		String STATEMENT = TrainingInformation.KEY_CHARACTER_ID + "=" + inEveCharacterID;
		Cursor cursor = myDb.query(DATABASE_TRAINING_INFO_TABLE, null, STATEMENT, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			SimpleDateFormat formatter = GGEveApplicationRunner.getEveDateFormatter();
//			String cachedUntil = cursor.getString(COLUMN_TRAININFO_CACHED_UNTIL);
			int characterID = cursor.getInt(COLUMN_TRAININFO_CHARACTERID);
			String currentTime = cursor.getString(COLUMN_TRAININFO_CURRENT_TIME);
			int skillInTrain = cursor.getInt(COLUMN_TRAININFO_SKILL_IN_TRAINING);
//			int endsp = cursor.getInt(COLUMN_TRAININFO_ENDSP);
			String endtime = cursor.getString(COLUMN_TRAININFO_ENDTIME);
//			int startsp = cursor.getInt(COLUMN_TRAININFO_STARTSP);
			String starttime = cursor.getString(COLUMN_TRAININFO_STARTTIME);
			int tolevel = cursor.getInt(COLUMN_TRAININFO_TOLEVEL);
			int typeid = cursor.getInt(COLUMN_TRAININFO_TYPEID);
//			String tqtime = cursor.getString(COLUMN_TRAININFO_TQTIME);
//			int tqtimeoffset = cursor.getInt(COLUMN_TRAININFO_TQTIME_OFFSET);
			cursor.close();
			try {
				TrainingInformation trainInfo = new TrainingInformation();
				trainInfo.setCharacterID(characterID);
				trainInfo.setSkillInTraining(skillInTrain);
				if (skillInTrain > 0) {
					trainInfo.setTrainingTypeID(typeid);
					trainInfo.setTrainingToLevel(tolevel);
					trainInfo.setTrainingEndTime(formatter.parse(endtime));
					trainInfo.setTrainingStartTime(formatter.parse(starttime));
					trainInfo.setCurrentTime(formatter.parse(currentTime));
				}
				return trainInfo;
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Constructs an EveCharacter object from a cursor's information.
	 * 
	 * @param inCursor
	 * @return
	 */
	private EveCharacter getEveCharacter(Cursor inCursor) {
		String myName = inCursor.getString(COLUMN_CHARACTER_NAME);
		String myRace = inCursor.getString(COLUMN_CHARACTER_RACE);
		String myBloodline = inCursor.getString(COLUMN_CHARACTER_BLOODLINE);
		String myGender = inCursor.getString(COLUMN_CHARACTER_GENDER);
		String myCorporationName = inCursor.getString(COLUMN_CHARACTER_CORP_NAME);
		int myCharacterID = inCursor.getInt(COLUMN_CHARACTER_ID);
		int myCorporationID = inCursor.getInt(COLUMN_CHARACTER_CORP_ID);
		int myBalance = inCursor.getInt(COLUMN_CHARACTER_BALANCE);
		int myUserID = inCursor.getInt(COLUMN_CHARACTER_USERID);
		TrainingInformation info = getTrainingInformation(myCharacterID);

		return new EveCharacter(myName, myCharacterID, myRace, myBloodline, myGender, myCorporationName, myCorporationID, myBalance,
				myUserID, info);
	}

	private int updateNewEveCharacter(EveCharacter inEveCharacter) {
		String where = EveCharacter.KEY_CHARACTER_NAME + "='" + inEveCharacter.getCharacterName() + "'";
		ContentValues contentValues = new ContentValues();
		if (inEveCharacter.getCharacterName() != null)
			contentValues.put(EveCharacter.KEY_CHARACTER_NAME, inEveCharacter.getCharacterName());
		if (inEveCharacter.getCharacterID() != -1)
			contentValues.put(EveCharacter.KEY_CHARACTER_ID, inEveCharacter.getCharacterID());
		if (inEveCharacter.getBloodline() != null)
			contentValues.put(EveCharacter.KEY_CHARACTER_BLOODLINE, inEveCharacter.getBloodline());
		if (inEveCharacter.getCorporationID() != -1)
			contentValues.put(EveCharacter.KEY_CHARACTER_CORP_ID, inEveCharacter.getCorporationID());
		if (inEveCharacter.getCorporationName() != null)
			contentValues.put(EveCharacter.KEY_CHARACTER_CORP_NAME, inEveCharacter.getCorporationName());
		if (inEveCharacter.getGender() != null)
			contentValues.put(EveCharacter.KEY_CHARACTER_GENDER, inEveCharacter.getGender());
		if (inEveCharacter.getRace() != null)
			contentValues.put(EveCharacter.KEY_CHARACTER_RACE, inEveCharacter.getRace());
		if (inEveCharacter.getBalance() != -1)
			contentValues.put(EveCharacter.KEY_CHARACTER_BALANCE, inEveCharacter.getBalance());
		if (inEveCharacter.getUserID() != -1)
			contentValues.put(EveCharacter.KEY_CHARACTER_USERID, inEveCharacter.getUserID());
		int returnValue = myDb.update(DATABASE_CHARACTER_TABLE, contentValues, where, null);

		where = TrainingInformation.KEY_CHARACTER_ID + "=" + inEveCharacter.getCharacterID();
		contentValues = new ContentValues();
		TrainingInformation info = inEveCharacter.getTrainingInformation();
		if (info != null) {
			SimpleDateFormat fm = GGEveApplicationRunner.getEveDateFormatter();
			if (info.getCachedUntil() != null)
				contentValues.put(TrainingInformation.KEY_CACHED_UNTIL, fm.format(info.getCachedUntil()));
			if (info.getCharacterID() != -1)
				contentValues.put(TrainingInformation.KEY_CHARACTER_ID, info.getCharacterID());
			if (info.getCurrentTime() != null)
				contentValues.put(TrainingInformation.KEY_CURRENT_TIME, fm.format(info.getCurrentTime()));
			if (info.getSkillInTraining() != -1)
				contentValues.put(TrainingInformation.KEY_SKILL_IN_TRAINING, info.getSkillInTraining());
			int returnValue2 = myDb.update(DATABASE_TRAINING_INFO_TABLE, contentValues, where, null);
			if (returnValue == -1 || returnValue2 == -1)
				return -1;
		}
		return returnValue;

	}

	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context inContext, String inName, CursorFactory inFactory, int inVersion) {
			super(inContext, inName, inFactory, inVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_CHARACTER_TABLE);
			db.execSQL(DATABASE_CREATE_ACCOUNTS_TABLE);
			db.execSQL(DATABASE_CREATE_TRAINING_INFO_TABLE);
			db.execSQL(DATABASE_CREATE_SERVER_STATUS_TABLE);
			db.execSQL(DATABASE_CREATE_SKILLS_TABLE);
			db.execSQL(DATABASE_CREATE_SKILLS_GROUP_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log.w("GGEveDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			// Upgrade the existing database to conform to the new version.
			// Multiple previous versions can be handled by comparing
			// _oldVersion and _newVersion values.
			// The simplest case is to drop the old table and create a
			// new one.
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CHARACTER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TRAINING_INFO_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ACCOUNTS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SERVER_STATUS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SKILL_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SKILL_GROUP_TABLE);
			// Create a new one.
			onCreate(db);

		}
	}
}
