package com.bhunix.smartrm.ui;

import com.bhunix.smartrm.bean.Alarm;
import com.bhunix.smartrm.utils.AlarmHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;



/**
 * @author black
 *	当时间到时, 接收alarm参数并设置对应mode
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		
		int mode = AudioManager.RINGER_MODE_NORMAL;
		Alarm alarm = new Alarm();
		AlarmHelper alarmHelper = new AlarmHelper(context, alarm);
		
		alarm = alarmHelper.getAlarmFromIntent(intent);
		
		AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		int startOrEnd = intent.getIntExtra(AlarmHelper.START_OR_END, 1);
		if(startOrEnd == AlarmHelper.START){
			if(alarm.mode.equals("mute"))
				mode = AudioManager.RINGER_MODE_SILENT;
			if(alarm.mode.equals("vibrate"))
				mode = AudioManager.RINGER_MODE_VIBRATE;
		}
		am.setRingerMode(mode);

		alarmHelper.setNextAlert(startOrEnd);
	}
}
