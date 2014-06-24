package com.cp.suishouji.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.CategoryInfo;

/**
 *  账户
 * t_account
 * t_account_group
 */
public class CalendarFragment extends BaseFragment {
	private int whitchTv;
	private WheelView month;
	private WheelView year;
	private WheelView day;
	private WheelView hour;
	private WheelView minute;
	private int lastYear = -1;
	private int lastMonth = -1;
	private int lastDay = -1;
	private int lastHour = -1;
	private int lastMinute = -1;
	public CalendarFragment(int whitchTv) {
		this.whitchTv = whitchTv;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_calendar, container, false);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

		month = (WheelView) layout.findViewById(R.id.month);
		year = (WheelView) layout.findViewById(R.id.year);
		day = (WheelView) layout.findViewById(R.id.day);
		hour = (WheelView) layout.findViewById(R.id.hour);
		minute = (WheelView) layout.findViewById(R.id.mininute);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				updateDays(year, month, day);
//				Log.e("OnWheelChangedListener", "OnWheelChangedListener");
				updateDays(year, month, day, hour, minute);
				lastYear = year.getCurrentItem();
				lastMonth = month.getCurrentItem();
				lastDay = day.getCurrentItem();
				lastHour = hour.getCurrentItem();
				lastMinute = minute.getCurrentItem();
			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] {"一月", "二月", "三月", "四月", "五月",
				"六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
		month.setViewAdapter(new DateArrayAdapter(getActivity(), months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR);
		year.setViewAdapter(new DateNumericAdapter(getActivity(), curYear - 10, curYear + 10, 10));
		year.setCurrentItem(10);
		year.addChangingListener(listener);

		//day
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.addChangingListener(listener);
		
		//hour,minute
		String[] hours = new String[24];
		for (int i = 0; i < 24; i++) {
			if(i<10){
				hours[i] = "0"+i;
			}else{
				hours[i] = String.valueOf(i);
			}
		}
		String[] minutes = new String[60];
		for (int i = 0; i < 60; i++) {
			if(i<10){
				minutes[i] = "0"+i;
			}else{
				minutes[i] = String.valueOf(i);
			}
		}
		
		hour.setViewAdapter(new DateArrayAdapter(getActivity(), hours, calendar.get(Calendar.HOUR_OF_DAY)));
		hour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		minute.setViewAdapter(new DateArrayAdapter(getActivity(), minutes, calendar.get(Calendar.MINUTE)));
		minute.setCurrentItem(calendar.get(Calendar.MINUTE));
		hour.addChangingListener(listener);
		minute.addChangingListener(listener);
		//CP 缓存
		if(lastYear!=-1){
			year.setCurrentItem(lastYear);
			month.setCurrentItem(lastMonth);
			day.setCurrentItem(lastDay);
			hour.setCurrentItem(lastHour);
			minute.setCurrentItem(lastMinute);
		}
		return layout;
	}
	private void setText(int year, int month, int day ,int hour,int minute){
		FragmentActivity activity = getActivity();
		if(activity instanceof AddOrEditTransActivity){
				((AddOrEditTransActivity)activity).setText(whitchTv, year,month,day,hour,minute);
		}
	};
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem() -10);
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(getActivity(), 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
//		setText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	public void updateDays(){
		updateDays(year, month, day, hour, minute);
	}
	void updateDays(WheelView year, WheelView month, WheelView day, WheelView hour, WheelView minute) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem() -10);
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(getActivity(), 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		setText(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, day.getCurrentItem()+1,hour.getCurrentItem(),minute.getCurrentItem());
	}
	
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	@Override
	public void btnok() {
		
	}
}
