package com.bhunix.smartrm.utils;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

import com.bhunix.smartrm.bean.Alarm;
import com.bhunix.smartrm.sqlitedb.SmartrmDB;


/**
 * @author black
 *	封装对单个alarm的操作
 */
public class AlarmHelper{
	
	public static final int CREATE_REQUEST_CODE = 1;
	public static final int EDIT_REQUEST_CODE = 2;
	public static final String REQUEST_CODE = "requestCode";
	public static final String ALARM_ACTION = "com.example.smartrm.ALARM_ACTION";
	public static final String ALARM = "alarm";
	public static final String START_OR_END = "startOrEnd";
	public static final int START = 1;
	public static final int END = 0;
	
	public Alarm alarm;
	private Context context;
	private SmartrmDB db;
	
	public AlarmHelper(Context ctx, Alarm mAlarm){
		alarm = mAlarm;
		context = ctx;
		db = new SmartrmDB(context);
		db.open();
	}
	
	public AlarmHelper(Context ctx){
		context = ctx;
		db = new SmartrmDB(context);
		db.open();
	}
	/*
	 * 计算下一个Alert和现在的间隔时间
	 * beginOrEnd为1则计算startTime, 为0则计算endTime;
	 * 返回long值直接用于设置AlarmManager
	 */
	public long CalculateNextAlarm(int beginOrEnd){
		int nowHour;
		int nowMinute;
		int minute;
		int hour;
		int dayCount;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		nowHour = c.get(Calendar.HOUR_OF_DAY);
		nowMinute = c.get(Calendar.MINUTE);
		if(beginOrEnd==1){
			hour=alarm.stpHour;
			minute=alarm.stpMinute;
		}else{
			hour=alarm.etpHour;
			minute=alarm.etpMinute;
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
//		如果时间小于现在, 则设置日期为明天
		if( (hour<nowHour) || (hour==nowHour) && (minute<=nowMinute) ){
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
//		计算出下一个Alert距离这个Alert的天数
		for(dayCount=0;dayCount<7;dayCount++){
//			余7则为下个星期
			int day = (c.get(Calendar.DAY_OF_WEEK)+dayCount)%7;
			if(day == 0)
				day = Calendar.SATURDAY;
			if(alarm.isSet(day))
				break;
		}
		c.add(Calendar.DAY_OF_YEAR, dayCount);

		return c.getTimeInMillis();
	}
		
	/*
	 * 设置闹铃, 发送PendingIntent
	 * 
	 * @param startOrEnd
	 *		START 计算开始时间的下一个闹铃, 并设置PendingIntent
	 *		END   计算结束时间的下一个闹铃, 并设置PengingIntent 
	 */
	public void setNextAlert(int startOrEnd){
			int requestCode;
			if(startOrEnd == 1){
				requestCode = alarm.id;
			}
			else{
				requestCode = alarm.id+100;
			}
			Intent intent = new Intent(ALARM_ACTION);
			putAlarmToIntent(intent);
			intent.putExtra(START_OR_END, startOrEnd);
			PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, CalculateNextAlarm(startOrEnd), pi);
	}
	
	/*
	 * 取消闹铃, 发送PendingIntent
	 * 
	 * @param startOrEnd
	 *		START 计算开始时间的下一个闹铃, 并取消PendingIntent
	 *		END   计算结束时间的下一个闹铃, 并取消PengingIntent 
	 */
	public void disableAlert(int startOrEnd){
		int requestCode;
		if(startOrEnd == 1){
			requestCode= alarm.id;
		}
		else{
			requestCode = alarm.id+100;
		}
		Intent intent = new Intent(ALARM_ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
	
	
	/*
	 * 添加闹铃
	 */
	public boolean addAlarm(){
		long id;
		id = db.createRecord(alarm.stpHour, alarm.stpMinute, alarm.etpHour, alarm.etpMinute,
							alarm.repeat, alarm.mode);
		if(id <= 0)
			return false;
		alarm.id = (int)id;
		enableAlarm();
		return true;
	}
	
	public boolean updateAlarm(){
		enableAlarm();
		return db.updateRecord(alarm.stpHour, alarm.stpMinute, alarm.etpHour, alarm.etpMinute,
				alarm.repeat, alarm.mode, alarm.id);
	}
	
	
	public boolean deleteAlarm(){
		disableAlarm();
		db.deleteRecord(alarm.id);
		return true;
	}
	
	/*
	 * 启用alarm, 设置开始时间和结束时间两个PendingIntent
	 */
	public void enableAlarm(){
		db.updateActivate(alarm.id, 1);
		setNextAlert(START);
		setNextAlert(END);
	}
	
	/*
	 * 禁用alarm, 取消开始时间和结束时间两个PendingIntent
	 */
	public void disableAlarm(){
		db.updateActivate(alarm.id, 0);
		disableAlert(START);
		disableAlert(END);
	}
	
	public Intent putAlarmToIntent(Intent intent){
		Parcel out = Parcel.obtain();
		alarm.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(ALARM, out.marshall());
		return intent;
	}
	
	public Alarm getAlarmFromIntent(Intent intent){
		byte[] data = intent.getByteArrayExtra(ALARM);
		if(data != null){
			Parcel in = Parcel.obtain();
			in.unmarshall(data, 0, data.length);
			in.setDataPosition(0);
			alarm = Alarm.CREATOR.createFromParcel(in);
		}
		return alarm;
	}
	
}
