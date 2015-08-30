package com.bhunix.smartrm.bean;

import java.util.Calendar;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.bhunix.smartrm.sqlitedb.SmartrmDB;

public class Alarm implements Parcelable{
	
	public final static Parcelable.Creator<Alarm> CREATOR= new Parcelable.Creator<Alarm>(){
		public Alarm createFromParcel(Parcel source){
			return new Alarm(source);
		}
		public Alarm[] newArray(int size){
			return new Alarm[size];
		}
	};
		
		public int describeContents(){
			return 0;
		}
		
		public void writeToParcel(Parcel des, int flags){
			des.writeInt(id);
			des.writeInt(stpHour);
			des.writeInt(stpMinute);
			des.writeInt(etpHour);
			des.writeInt(etpMinute);
			des.writeInt(repeat);
			des.writeInt(activate);
			des.writeString(mode);
		}
		// ////////////////////////////
		// end Parcelable apis
		// ////////////////////////////
			
		//the attribute of alarm
			public int id;
			public int stpHour;
			public int stpMinute;
			public int etpHour;
			public int etpMinute;
			public int repeat;
			public int activate;
			public String mode;
		//	
			public Alarm(Parcel source){
				id 			= source.readInt();
				stpHour	= source.readInt();
				stpMinute	= source.readInt();
				etpHour	= source.readInt();
				etpMinute	= source.readInt();
				repeat		= source.readInt();
				activate	= source.readInt();
				mode		= source.readString();
			}
			
			public Alarm(Cursor c){
				id 			= c.getInt(c.getColumnIndex(SmartrmDB.KEY_ID));
				stpHour	= c.getInt(c.getColumnIndex(SmartrmDB.KEY_STIME_HOUR));
				stpMinute	= c.getInt(c.getColumnIndex(SmartrmDB.KEY_STIME_MINUTE));
				etpHour	= c.getInt(c.getColumnIndex(SmartrmDB.KEY_ETIME_HOUR));
				etpMinute	= c.getInt(c.getColumnIndex(SmartrmDB.KEY_ETIME_MINUTE));
				repeat		= c.getInt(c.getColumnIndex(SmartrmDB.KEY_REPEAT));
				activate 	= c.getInt(c.getColumnIndex(SmartrmDB.KEY_ACTIVATE));
				mode		= c.getString(c.getColumnIndex(SmartrmDB.KEY_MODE));
			}
			
			public Alarm(){
			}
			/*
			 * day of week to repeat
			 */
	
				/*
				 * 设置重复时间
				 */
				public void setRepeat(int day, boolean repeatOrNot){
					//因为Calendar.SUNDAY等于1, 而我们的repeat中sunday对应第七位.
					int weekDay;
					if(day == Calendar.SUNDAY){
						weekDay = day+6;
					}else{
						weekDay = day-1;
					}
					
					if(repeatOrNot == true){
						repeat |= (0x1<<weekDay);
					}else{
						repeat &= (~(0x1<<weekDay));
					}
				}
				
				//判断是否每周重复
				public boolean repeatOrNot(){
					if((repeat&0x01) != 0)
						return true;
					return false;
				}
				
				//判断某一天是否重复
				public boolean isSet(int day){
					int weekDay;
					if(day == Calendar.SUNDAY){
						weekDay = day+6;
					}else{
						weekDay = day-1;
					}
					
					int result = repeat&(0x1<<weekDay);
					if(result == 0)
						return false;
					else
						return true;
				}
				
				public void setActivate(int activateOrNot){
					activate = activateOrNot;
				}
				
				public boolean checkActivate(){
					return	activate==1?true:false;
				}
	
}
