package com.bhunix.smartrm.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmartrmDB{
	
	public static final String KEY_ID				="id";
	public static final String KEY_STIME_HOUR		="sTimeHour";
	public static final String KEY_STIME_MINUTE	="sTimeMinute";
	public static final String KEY_ETIME_HOUR		="eTimeHour";
	public static final String KEY_ETIME_MINUTE	="eTimeMinute";
	public static final String KEY_REPEAT			="repeat";
	public static final String KEY_MODE			="mode";
	public static final String KEY_ACTIVATE		="activate";
	
	private static final String DATABASE_NAME ="smartrmDB";
	private static final String DATABASE_TABLE="tb_record";
	private static final int DATABASE_VERSION = 1;
	
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDB;
	
	private Context ctx;
	
	private static final String DATABASE_CREATE_TABLE = "create table tb_record("
									+ "id integer primary key autoincrement, "
									+ "sTimeHour int, sTimeMinute int,"
									+ "eTimeHour int, eTimeMinute int, "
									+ "repeat int, mode varchar(10), activate int)";
									
	
	public class DatabaseHelper extends SQLiteOpenHelper{
		
		
		public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
							int version)
		{
			super(context, name, factory, version);
		}
		
		public DatabaseHelper(Context context, String name, int version)
		{
			this(context, name, null, version);
		}
		
		public DatabaseHelper(Context context, String name)
		{
			this(context, name, DATABASE_VERSION);
		}
		
		public DatabaseHelper(Context context)
		{
			this(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE_TABLE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS smartRM");
			onCreate(db);
		}
	}
	
	public SmartrmDB(Context ctx){
		this.ctx = ctx;
	}
	
	public SmartrmDB open() throws SQLException{
		mDBHelper = new DatabaseHelper(ctx);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDBHelper.close();
	}
	
	public long createRecord(int stime_hour,int stime_minute, int etime_hour, int etime_minute,
									int repeat, String mode){
		ContentValues values = new ContentValues();
		values.put(KEY_STIME_HOUR, stime_hour);
		values.put(KEY_STIME_MINUTE, stime_minute);
		values.put(KEY_ETIME_HOUR, etime_hour);
		values.put(KEY_ETIME_MINUTE, etime_minute);
		values.put(KEY_REPEAT, repeat);
		values.put(KEY_MODE, mode);
		values.put(KEY_ACTIVATE, 1);
		return mDB.insert(DATABASE_TABLE, null, values);
	}
	
	public boolean deleteRecord(int rowId){
		return mDB.delete(DATABASE_TABLE, KEY_ID+"="+rowId, null)>0;
	}
	
	public Cursor getAllRecords(){
		Cursor cursor =
		mDB.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_STIME_HOUR, KEY_STIME_MINUTE, 
													KEY_ETIME_HOUR, KEY_ETIME_MINUTE,
													KEY_REPEAT, KEY_MODE, KEY_ACTIVATE},
													null,null,null,null,null);
		if(cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor getRecord(int rowId){
		Cursor mCursor =
		mDB.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_STIME_HOUR, KEY_STIME_MINUTE, 
													KEY_ETIME_HOUR, KEY_ETIME_MINUTE,
													KEY_REPEAT, KEY_MODE, KEY_ACTIVATE},
													KEY_ID+"="+rowId,
													null,null,null,null,null);
		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateRecord(int stime_hour,int stime_minute, int etime_hour, int etime_minute,
									int repeat, String mode, int rowId){
		ContentValues values = new ContentValues();
		values.put(KEY_STIME_HOUR, stime_hour);
		values.put(KEY_STIME_MINUTE, stime_minute);
		values.put(KEY_ETIME_HOUR, etime_hour);
		values.put(KEY_ETIME_MINUTE, etime_minute);
		values.put(KEY_REPEAT, repeat);
		values.put(KEY_MODE, mode);
		return mDB.update(DATABASE_TABLE, values, KEY_ID+"="+rowId, null)>0;
	}
	
	/**
	 * 更改闹铃是否启动的标志
	 * @param rowId
	 * @param activate
	 * @return
	 */
	public boolean updateActivate(int rowId, int activate){
		ContentValues values = new ContentValues();
		values.put(KEY_ACTIVATE, activate);
		return mDB.update(DATABASE_TABLE, values, KEY_ID+"="+rowId, null)>0;
	}
	
}
