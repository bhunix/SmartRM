package com.bhunix.smartrm.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * @author black
 *	接收系统重启信号, 重新初始化所有alarm
 */
public class InitReceiver extends BroadcastReceiver{
	
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";
	
	public void onReceive(Context ctx, Intent intent){
		if(intent.getAction().equals(ACTION)){
			AlarmsHelper initHelper = new AlarmsHelper(ctx);
			initHelper.initAlarms();
		}
	}
	
}
