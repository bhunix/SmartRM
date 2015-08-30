package com.bhunix.smartrm.ui;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bhunix.smartrm.R;
import com.bhunix.smartrm.bean.Alarm;
import com.bhunix.smartrm.sqlitedb.SmartrmDB;
import com.bhunix.smartrm.utils.AlarmHelper;

/**
 * @author black
 * 定时模式小程序, 可以设置一个时间区间的手机模式, 有震动和静音两种
 * 当时间区间结束时, 会重新设置为普通模式
 * 每一个设置都存储进数据库, 并显示在此主页面
 */
public class MainActivity extends ListActivity {
	
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	//	getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
   //              WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView(R.layout.activity_main);
		
		Button addButton = (Button)findViewById(R.id.add);
		addButton.setOnClickListener(new addListener());
		
		mAdapter = new MyAdapter();
		setListAdapter(mAdapter);
		getListView().setOnItemLongClickListener(new LongClickListener());
	}
	
	
	
	/**
	 * @author Jet
	 * 监听listview中每一个item的长按事件
	 */
	class LongClickListener implements android.widget.AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Alarm alarm = (Alarm)parent.getItemAtPosition(position);
			final AlarmHelper alarmHelper = new AlarmHelper(getApplicationContext(), alarm);
			new AlertDialog.Builder(MainActivity.this)
							.setTitle(R.string.DeleteDialogTitle)
							.setNegativeButton(R.string.No, null)
							.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener(){
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									alarmHelper.deleteAlarm();
									updateLayout();
								}
							})
							.show();
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 *  监听listview中每一个item
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, EditRingerMode.class);
		Alarm alarm = (Alarm)getListView().getItemAtPosition(position);
		AlarmHelper alarmHelper = new AlarmHelper(MainActivity.this, alarm);
		alarmHelper.putAlarmToIntent(intent);
		intent.putExtra(AlarmHelper.REQUEST_CODE, AlarmHelper.EDIT_REQUEST_CODE);
		startActivityForResult(intent, AlarmHelper.EDIT_REQUEST_CODE);
	}
	
	/*
	 * 创建按钮的监听器
	 */
	class addListener implements OnClickListener
	{
		public void onClick(View v)
		{
			Intent intent = new Intent(MainActivity.this, EditRingerMode.class);
			intent.putExtra(AlarmHelper.REQUEST_CODE, AlarmHelper.CREATE_REQUEST_CODE);
			startActivityForResult(intent, AlarmHelper.CREATE_REQUEST_CODE);
		}
	}
	
	

	public void onActivityResult(int requestCode, int resultCode, Intent data){
			if(resultCode == RESULT_OK){
				updateLayout();
			}
	}
	
	
	final class HoldView{
		public TextView tvTime;
		public TextView tvMode;
		public TextView tvMON;
		public TextView tvTUE;
		public TextView tvWED;
		public TextView tvTHU;
		public TextView tvFRI;
		public TextView tvSAT;
		public TextView tvSUN;
		public ToggleButton activate;
	}
	
	class MyAdapter extends BaseAdapter{
		Cursor cursor;
		SmartrmDB db;
		
		public MyAdapter(){
		db = new SmartrmDB(MainActivity.this);
		db.open();
		cursor = db.getAllRecords();
		}
		/*
		@Override
        public int getViewTypeCount() {                 
                      //Count=Size of ArrayList.
            return cursor.getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }
        */
		public void updateData(){
			cursor = db.getAllRecords();
		}
		@Override
		public int getCount(){
			return cursor.getCount();
		}
		
		@Override
		public Alarm getItem(int position){
			cursor.moveToPosition(position);
			Alarm alarm = new Alarm(cursor);
			return alarm;
		}
		
		@Override
		public long getItemId(int position){
			return position;
		}
		
		@Override
		public View getView(int position, View covertView, ViewGroup parent){
			HoldView holdView;
			Alarm alarm;
			if(cursor == null)
				System.out.println("error");
			cursor.moveToPosition(position);
			alarm = new Alarm(cursor);
			
			if(covertView == null){
			holdView = new HoldView();
			covertView = getLayoutInflater().inflate(R.layout.listview_item, null);
			holdView.tvTime = (TextView)covertView.findViewById(R.id.timeView);
			holdView.tvMode = (TextView)covertView.findViewById(R.id.modeView);
			holdView.tvMON = (TextView)covertView.findViewById(R.id.MonView);
			holdView.tvTUE = (TextView)covertView.findViewById(R.id.TueView);
			holdView.tvWED = (TextView)covertView.findViewById(R.id.WedView);
			holdView.tvTHU = (TextView)covertView.findViewById(R.id.ThuView);
			holdView.tvFRI = (TextView)covertView.findViewById(R.id.FriView);
			holdView.tvSAT = (TextView)covertView.findViewById(R.id.SatView);
			holdView.tvSUN = (TextView)covertView.findViewById(R.id.SunView);
			holdView.activate = (ToggleButton)covertView.findViewById(R.id.activateButton);
			covertView.setTag(holdView);
			}
			else{
				holdView = (HoldView)covertView.getTag();
			}
			holdView.activate.setTag(alarm);
			//显示设置的时间
			StringBuilder time = new StringBuilder();
			if(alarm.stpHour<10)
				time.append(0);
			time.append(alarm.stpHour);
			time.append(":");
			if(alarm.stpMinute<10)
			time.append(0);
				time.append(alarm.stpMinute);
			time.append(" - ");
			if(alarm.etpHour<10)
				time.append(0);
			time.append(alarm.etpHour);
			time.append(":");
			if(alarm.etpMinute<10)
				time.append(0);
			time.append(alarm.etpMinute);
			holdView.tvTime.setText(time.toString());
			
			if(alarm.repeatOrNot()){
		    //显示重复的日期
			int repeat = getResources().getColor(R.color.skyblue);
			int unrepeat = getResources().getColor(R.color.gainsboro);
			if(alarm.isSet(Calendar.MONDAY))
				holdView.tvMON.setTextColor(repeat);
			else
				holdView.tvMON.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.TUESDAY))
				holdView.tvTUE.setTextColor(repeat);
			else
				holdView.tvTUE.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.WEDNESDAY))
				holdView.tvWED.setTextColor(repeat);
			else
				holdView.tvWED.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.THURSDAY))
				holdView.tvTHU.setTextColor(repeat);
			else
				holdView.tvTHU.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.FRIDAY))
				holdView.tvFRI.setTextColor(repeat);
			else
				holdView.tvFRI.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.SATURDAY))
				holdView.tvSAT.setTextColor(repeat);
			else
				holdView.tvSAT.setTextColor(unrepeat);
			
			if(alarm.isSet(Calendar.SUNDAY))
				holdView.tvSUN.setTextColor(repeat);
			else
				holdView.tvSUN.setTextColor(unrepeat);
			}
			
			//显示设置的模式
			holdView.tvMode.setText(alarm.mode);
			
			holdView.activate.setFocusable(false);
			
			//设置开关
			holdView.activate.setChecked(alarm.checkActivate());
			
			class onCheckedChangeListener implements OnCheckedChangeListener{
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Alarm alarm = (Alarm)buttonView.getTag();
					AlarmHelper alarmHelper = new AlarmHelper(getApplicationContext(), alarm);
					if(isChecked){
						alarmHelper.enableAlarm();
					}else{
						alarmHelper.disableAlarm();
					}
				}
			}
			holdView.activate.setOnCheckedChangeListener(new onCheckedChangeListener());
			
			return covertView;
		}
		
	}

	//当数据改变时, 更新ListView显示
	private void updateLayout(){
		mAdapter.updateData();
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.help:
			Intent intent = new Intent(this, Help.class);
			startActivity(intent);
			return true;
		 default:
	       return super.onOptionsItemSelected(item);
		}
	}
	
	
}
