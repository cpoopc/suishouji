package com.cp.suishouji.widgt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.PeriodInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainTopItem extends RelativeLayout{
//	private PeriodInfo periodInfo;
	private TextView tv_date;
	private TextView tv_imcome;
	private TextView tv_expense;
	public MainTopItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}
	private CpButtonClickListener listener;
	public void setOnCpButtonClickListener(CpButtonClickListener listener){
		this.listener = listener;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			Log.e("press", "up");
			Log.e("this.getId()", this.getId()+"..");
			if(listener!=null){
				listener.onclick(this);
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
	public void setPeriodInfo(PeriodInfo info){
		String income = MyUtil.doubleFormate(info.income);
		String expense = MyUtil.doubleFormate(info.cost);
		
		tv_imcome.setText("¥ "+income);
		tv_expense.setText("¥ "+expense);
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		tv_date.setText(sdf.format(date));
	}
	private void initUI(Context context) {
		View view = View.inflate(context, R.layout.main_top_item, null);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_imcome = (TextView) view.findViewById(R.id.tv_imcome);
		tv_expense = (TextView) view.findViewById(R.id.tv_expense);
		if(view!=null){
			this.addView(view);
		}
	}
//	@Override //使用自己的listener
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
////		Log.e("MainMiddleItem", "onInterceptTouchEvent");
//		return true;//返回true,不拦截?否则传不到上层
//	}
}
