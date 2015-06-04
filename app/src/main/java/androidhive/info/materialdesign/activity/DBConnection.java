package androidhive.info.materialdesign.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConnection extends SQLiteOpenHelper {

	private static final String TAG = "DBConnection";

	private static String DB_PATH = "";

	private static final String DB_NAME = "PMI";

	public static final String LIST_TABLE = "tblList";

	private static final String CREATE_LIST_TABLE = "create table IF NOT EXISTS "
			+ LIST_TABLE
			+ " (ID integer primary key autoincrement, Name text);";

	private SQLiteDatabase myDataBase;

	private MainActivity_as myContext;
	private Context context;

	public String errorMsg;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 *
	 * @param context
	 */
	public DBConnection(MainActivity_as context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;

		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	}

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 *
	 * @param context
	 */
	public DBConnection(Context context) {

		super(context, DB_NAME, null, 1);
		this.context = context;

		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();
			try {
				createNewDataBase();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Error("Error creating database");
			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		try {
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
		} catch (Exception ee) {
			Log.i("Log", "Exception while checkDatabase()...");
		}
		if (myDataBase != null) {
			myDataBase.close();
		}

		return myDataBase != null ? true : false;
	}

	private boolean isAllTableAvailable() {
		boolean isAllTableExists = false;
		String sTableName = "'" + CREATE_LIST_TABLE + "'";
		boolean exists = isTableExists(sTableName, false);
//		L.debug("sTableName --> " + sTableName + "; All tables exists ? "
//				+ exists);
		if (exists)
			isAllTableExists = true;
		return isAllTableExists;
	}

	public boolean isTableExists(String tableName, boolean openDb) {
		if (openDb) {
			if (myDataBase == null || !myDataBase.isOpen()) {
				myDataBase = getReadableDatabase();
			}

			if (!myDataBase.isReadOnly()) {
				myDataBase.close();
				myDataBase = getReadableDatabase();
			}
		}
		String sQry = "select count(tbl_name) from sqlite_master where tbl_name IN ("
				+ tableName + ")";
//		L.debug("sQry --> " + sQry);
		Cursor cursor = myDataBase.rawQuery(sQry, null);
		if (cursor != null && cursor.moveToNext()) {
			int count = cursor.getInt(0);
//			L.debug("count --> " + count);
			if (count == 4)
				return true;
		}
		return false;
	}

	public void createTables() {
//		L.debug("Creating neccessary tables....");

		executeUpdate(CREATE_LIST_TABLE);
	}

	void createNewDataBase() {
		try {
			String MY_DATABASE_NAME = DB_NAME; // DB_PATH +
			SQLiteDatabase myDB = null;
			if (myContext != null)
				myDB = this.myContext.openOrCreateDatabase(MY_DATABASE_NAME, 0,
						null);
			else
				myDB = this.context.openOrCreateDatabase(MY_DATABASE_NAME, 0,
						null);
			myDB.close();
			open();
			createTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public int QueryEngineIn(String SQLStr) {

		int retval = 0;
		try {

			myDataBase.execSQL(SQLStr);

			retval = 0;
		} catch (Exception ex) {

			retval = 1;
			Log.v(TAG, ex.toString());
		}
		return retval;
	}

	public Cursor QueryEngine(String SQLStr) {
		Cursor RtC;
		String[] SelectionArgs = null;
		try {

			RtC = myDataBase.rawQuery(SQLStr, SelectionArgs);

		} catch (Exception ex) {

			RtC = null;
			Log.v(TAG, ex.toString());
		}
		return RtC;
	}

	public Cursor executeQuery(String sql) throws SQLException {

//		L.debug("sql --> " + sql);
		Cursor c = null;
		try {
			this.errorMsg = "";
			c = myDataBase.rawQuery(sql, null);
		} catch (Exception e) {
			this.errorMsg = "" + e.toString();
			e.printStackTrace();
		}

		return c;
	}

	/** insert values into table */
	public long insert(ContentValues insertData, String table) {
//		L.debug(TAG + "::Inserted:" + insertData.toString());
		String insert = insertData.toString();
		System.out.println("" + insert);
		return myDataBase.insert(table, null, insertData);
	}

	public void call(){
		System.out.println("print");
	}

	/** update values in the table */
	public long update(ContentValues insertData, String table,
					   String selection, String[] selectionValue) {
//		L.debug(TAG + "::Inserted:" + insertData.toString());
		return myDataBase.update(table, insertData, selection, selectionValue);
	}

	public String getValue(String tablename, String rvalues, String cond,
						   String value) {
		String name = "";
		Cursor cur = QueryEngine("select " + rvalues + " from" + tablename
				+ " where" + cond + "='" + value + "'");
		cur.moveToFirst();
		if (cur.isAfterLast() == false) {
			name = cur.getString(0);
			cur.moveToNext();
		}
		cur.close();
		return name;
	}


	public boolean executeUpdate(String sql) throws SQLException {
//		L.debug(TAG + "::executeUpdate()::sql --> " + sql);
		boolean qryExecuted = false;
		try {
			this.errorMsg = "";
			myDataBase.execSQL(sql);
			qryExecuted = true;
		} catch (Exception e) {
			this.errorMsg = "" + e.toString();
			e.printStackTrace();
			qryExecuted = false;
		}
		return qryExecuted;
	}

	public int countRecords(String tblName) {
		return countRecords(tblName, "");
	}

	public int countRecords(String tblName, String creteria) {
		String sCountQuery = "SELECT COUNT(*) FROM " + tblName;
		if (creteria != null && creteria.trim().length() > 0) {
			sCountQuery += " WHERE " + creteria;
		}
		int iCount = -1;
		Cursor recordSet = executeQuery(sCountQuery);
		if (recordSet != null) {
			recordSet.moveToNext();
			iCount = recordSet.getInt(0);
		}
		recordSet.close();
		recordSet = null;
		return iCount;
	}

	public int getProfilesCount(String tblName) {
		String countQuery = "SELECT  * FROM " + tblName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void createTable() {
		/** Creating DB and Table if not exists */
		try {
			myDataBase
					.execSQL("CREATE TABLE IF NOT EXISTS allprocess (id INTEGER PRIMARY KEY, knowledgearea varchar(550), processgroup varchar(550), processname varchar(550))");

//			myDataBase
//			.execSQL("CREATE TABLE IF NOT EXISTS allprocess (id INTEGER PRIMARY KEY, knowledgearea varchar(550), processgroup varchar(550), processname varchar(550))");

//			CREATE TABLE IF NOT EXISTS user (name varchar(550), lastname varchar(550), email
//					 varchar(550), password varchar(550), country varchar(550), mobile varchar(550))
			;
			myDataBase
					.execSQL("CREATE TABLE IF NOT EXISTS user (name varchar(550), lastname varchar(550), email varchar(550), password varchar(550), country varchar(550), mobile varchar(550),imageurl varchar(550))");
		} catch (Exception e) {

		}

	}

	public void dropTable() {
		try {
			myDataBase.execSQL("DELETE FROM user");
//			myDataBase.execSQL("DELETE FROM ");

		} catch (Exception e) {

		}
	}

	public ArrayList<HashMap<Integer, String>> fetchDropDownValues(
			String tableName) {
		ArrayList<HashMap<Integer, String>> arrDrodown = new ArrayList<HashMap<Integer, String>>();
		HashMap<Integer, String> valueMap = new HashMap<Integer, String>();
		try {
			Cursor cur1 = executeQuery("SELECT * FROM " + tableName);
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				valueMap.put(cur1.getInt(0), cur1.getString(1));
				arrDrodown.add(valueMap);
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return arrDrodown;
	}

	public ArrayList<String> getSpinnerknowledgearea(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("SelectAll");
		try {
			Cursor cur1 = executeQuery("SELECT knowledgearea FROM allprocess");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				list.add(cur1.getString(0));
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return list;
	}
	public ArrayList<String> getProcessgroup(String knArea ){
		ArrayList<String> list = new ArrayList<String>();
		list.add("SelectAll");
		try {
			Cursor cur1 = executeQuery("SELECT processgroup FROM allprocess WHERE knowledgearea = '"+knArea+"'");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				list.add(cur1.getString(0));
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return list;
	}
	public ArrayList<String> getProcessName(String knArea, String progrup){
		ArrayList<String> list = new ArrayList<String>();
		list.add("SelectAll");
		try {
			Cursor cur1 = executeQuery("SELECT processname FROM allprocess WHERE knowledgearea = '"+knArea+"' and processgroup = '"+progrup+"'");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				list.add(cur1.getString(0));
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return list;
	}

	public String getuserName(){
		String name = "";
		try {
			Cursor cur1 = executeQuery("SELECT name FROM user");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				name = cur1.getString(0);
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return name;
	}

	public String getuserEmail() {
		String name = "";
		try {
			Cursor cur1 = executeQuery("SELECT email FROM user");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				name = cur1.getString(0);
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return name;
	}

	public String getuserImageurl() {
		String name = "";
		try {
			Cursor cur1 = executeQuery("SELECT imageurl FROM user");
			cur1.moveToFirst();
			while (cur1.isAfterLast() == false) {
				name = cur1.getString(0);
				cur1.moveToNext();
			}
			cur1.close();
		} catch (Exception e) {

		}
		return name;
	}
}
