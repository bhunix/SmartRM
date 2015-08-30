package com.bhunix.smartrm.ui;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bhunix.smartrm.R;
import com.bhunix.smartrm.bean.Alarm;
import com.bhunix.smartrm.utils.AlarmHelper;

/**
 * @author black
 *	编辑alarm的界面和逻辑
 */
public class EditRingerMode extends Activity {
	
	Alarm mAlarm = new Alarm();
	AlarmHelper alarmHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_ringer_mode);
		alarmHelper = new AlarmHelper(EditRingerMode.this, mAlarm);
		if(getIntent().getIntExtra(AlarmHelper.REQUEST_CODE, AlarmHelper.CREATE_REQUEST_CODE)
				== AlarmHelper.EDIT_REQUEST_CODE){
		mAlarm = alarmHelper.getAlarmFromIntent(getIntent());
		}
		setView(mAlarm);
	}

	
	/**
	 * 	从界面中提取值, 并填充alarm
	 */
	public void buildAlarmFromUI(){
		TimePicker stp = (TimePicker)findViewById(R.id.sTimePicker);
		mAlarm.stpHour	 	 = stp.getCurrentHour();
		mAlarm.stpMinute  	 = stp.getCurrentMinute(); 
		
		TimePicker etp = (TimePicker)findViewById(R.id.eTimePicker);
		mAlarm.etpHour		 = etp.getCurrentHour();
		mAlarm.etpMinute		 = etp.getCurrentMinute();
	
		Spinner spinner = (Spinner)findViewById(R.id.spinnerMode);
		if(spinner.getSelectedItemPosition() == 0){
			mAlarm.mode = "vibrate";
		}else{
			mAlarm.mode = "mute";
		}
		
		CheckBox check = (CheckBox)findViewById(R.id.boxRepeat);
		if(check.isChecked()){
			mAlarm.repeat|=0x01;
		}
		
		mAlarm.activate = 1;
	}
	
	class RepeatButton{
		
		private ToggleButton MonButton;
		private ToggleButton TueButton;
		private ToggleButton WedButton;
		private ToggleButton ThuButton;
		private ToggleButton FriButton;
		private ToggleButton SatButton;
		private ToggleButton SunButton;
		private int repeat;
		private int unrepeat;
		
		public RepeatButton(){
			MonButton = (ToggleButton)findViewById(R.id.MonButton);
			TueButton = (ToggleButton)findViewById(R.id.TueButton);
			WedButton = (ToggleButton)findViewById(R.id.WedButton);
			ThuButton = (ToggleButton)findViewById(R.id.ThuButton);
			FriButton = (ToggleButton)findViewById(R.id.FriButton);
			SatButton = (ToggleButton)findViewById(R.id.SatButton);
			SunButton = (ToggleButton)findViewById(R.id.SunButton);
			repeat = getResources().getColor(R.color.skyblue);
			unrepeat = getResources().getColor(R.color.gainsboro);
			setDayButtonListener();
		}
		
		public void initDayForEdit(Alarm alarm){
			if(alarm.isSet(Calendar.MONDAY))
				MonButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.TUESDAY))
				TueButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.WEDNESDAY))
				WedButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.THURSDAY))
				ThuButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.FRIDAY))
				FriButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.SATURDAY))
				SatButton.setTextColor(repeat);
			if(alarm.isSet(Calendar.SUNDAY))
				SunButton.setTextColor(repeat);
		}
	
		private void setDayButtonListener()
		{
			MonButton.setOnCheckedChangeListener(new DayListener());
			TueButton.setOnCheckedChangeListener(new DayListener());
			WedButton.setOnCheckedChangeListener(new DayListener());
			ThuButton.setOnCheckedChangeListener(new DayListener());
			FriButton.setOnCheckedChangeListener(new DayListener());
			SatButton.setOnCheckedChangeListener(new DayListener());
			SunButton.setOnCheckedChangeListener(new DayListener());
		}
	
		class DayListener implements CompoundButton.OnCheckedChangeListener
		{
			public void onCheckedChanged(CompoundButton toggle, boolean checked)
			{
			switch(toggle.getId())
			{
			case R.id.MonButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.MONDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.MONDAY, false);
				}
				break;
				
			case R.id.TueButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.TUESDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.TUESDAY, false);
				}
				break;
				
			case R.id.WedButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.WEDNESDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.WEDNESDAY, false);
				}
				break;
				
			case R.id.ThuButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.THURSDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.THURSDAY, false);
				}
				break;
				
			case R.id.FriButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.FRIDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.FRIDAY, false);
				}
				break;
				
			case R.id.SatButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.SATURDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.SATURDAY, false);
				}
				break;
				
			case R.id.SunButton:
				if(checked==true){
					toggle.setTextColor(repeat);
					mAlarm.setRepeat(Calendar.SUNDAY, true);
				}
				else{
					toggle.setTextColor(unrepeat);
					mAlarm.setRepeat(Calendar.SUNDAY, false);
				}
				break;
			
			}	
		}
	}
	}
	
	//set listener of save button and cancel button

//set the Time picker and the mode selector 
	private void setView(Alarm alarm)
	{
		TimePicker stp = (TimePicker)findViewById(R.id.sTimePicker);
		stp.setIs24HourView(true);	
		TimePicker etp = (TimePicker)findViewById(R.id.eTimePicker);
		etp.setIs24HourView(true);
		
		Spinner spinner = (Spinner)findViewById(R.id.spinnerMode);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mode, 
															android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		setSaveListener();
		RepeatButton repeatButton = new RepeatButton();
		if(alarm != null){
			stp.setCurrentHour(alarm.stpHour);
			stp.setCurrentMinute(alarm.stpMinute);
			etp.setCurrentHour(alarm.etpHour);
			etp.setCurrentMinute(alarm.etpMinute);
			repeatButton.initDayForEdit(alarm);
			if(alarm.repeatOrNot()){
				CheckBox box = (CheckBox)findViewById(R.id.boxRepeat);
				box.setChecked(true);
			}
			spinner.setSelection(adapter.getPosition(alarm.mode));
		}
	}
		
		
	private void setSaveListener()
	{
		Button save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new SaveListener());
			
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new SaveListener());
	}
		
	class SaveListener implements Button.OnClickListener
	{
		public void onClick(View v){
			switch(v.getId()){
			case R.id.save:
				if(getIntent().getIntExtra(AlarmHelper.REQUEST_CODE, AlarmHelper.CREATE_REQUEST_CODE)
						== AlarmHelper.EDIT_REQUEST_CODE){
					//oldAlarmHelper, 取消旧的Alarm
					alarmHelper.disableAlarm();
					//newAlarmHelper, 更新Alarm, 并启动Alarm
					buildAlarmFromUI();
					alarmHelper = new AlarmHelper(getApplicationContext(), mAlarm);
					if(alarmHelper.updateAlarm()){
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_update_successful),
								Toast.LENGTH_SHORT).show();
					}
				}else{
					//新增Alarm
					buildAlarmFromUI();
					alarmHelper = new AlarmHelper(getApplicationContext(), mAlarm);
					if(alarmHelper.addAlarm()){
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.toast_create_successful),
								Toast.LENGTH_SHORT).show();
					}
				}
				Intent intent = new Intent();
				alarmHelper.putAlarmToIntent(intent);
				setResult(RESULT_OK, intent);
				finish();
				break;
				
			case R.id.cancel:
				setResult(RESULT_CANCELED);
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_ringer_mode, menu);
		return true;
	}

}
