package com.bhunix.smartrm.utils;

import android.content.Context;
import android.database.Cursor;

import com.bhunix.smartrm.bean.Alarm;
import com.bhunix.smartrm.sqlitedb.SmartrmDB;

/**
 * @author black
 * 封装对所有alarm的操作
 *
 */
public class AlarmsHelper {
	
	private SmartrmDB db;
	private Cursor cursor;
	private Context context;

	public AlarmsHelper(Context ctx) {
		// TODO Auto-generated constructor stub
		context = ctx;
		db = new SmartrmDB(context);
		db.open();
		cursor = db.getAllRecords();
	}
	
	/*
	 * 启用所有activated的alarm
	 */
	public void initAlarms(){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Alarm alarm = new Alarm(cursor);
			if(alarm.checkActivate()){
				AlarmHelper alarmHelper = new AlarmHelper(context, alarm);
				alarmHelper.setNextAlert(AlarmHelper.START);
				alarmHelper.setNextAlert(AlarmHelper.END);
			}
			cursor.moveToNext();
		}
	}
	
	/**
	 * 启用所有alarm
	 */
	public void enableAllAlarms(){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Alarm alarm = new Alarm(cursor);
			if(!alarm.checkActivate()){
				AlarmHelper alarmHelper = new AlarmHelper(context, alarm);
				alarmHelper.enableAlarm();
			}
			cursor.moveToNext();
		}
	}
	
	/*
	 * 禁用所有alarm
	 */
	public void disableAllAlarms(){
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Alarm alarm = new Alarm(cursor);
			if(alarm.checkActivate()){
				AlarmHelper alarmHelper = new AlarmHelper(context, alarm);
				alarmHelper.disableAlarm();
			}
			cursor.moveToNext();
		}
	}
	
}
