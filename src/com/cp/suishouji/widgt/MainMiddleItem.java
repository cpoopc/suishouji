package com.cp.suishouji.widgt;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.PeriodInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainMiddleItem extends RelativeLayout {

	private TextView tv_imcome;
	private TextView tv_expense;
	private TextView tv_today;
	private TextView tv_date;
	private boolean isHightDpi;
	public MainMiddleItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}
	public void setPeriodInfo(PeriodInfo info){
		String income = MyUtil.doubleFormate(info.income);
		String expense = MyUtil.doubleFormate(info.cost);
		
		tv_imcome.setText("¥ "+income);
		tv_expense.setText("¥ "+expense);
	}
	public void setTitle(String str ){
		tv_today.setText(str);
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		sdf.applyPattern("MM月dd日");
		String title = "";
		if(isHightDpi){
			if("本周".equals(str)){
				title += sdf.format(new Date(MyUtil.getWeekMillis(0)));
				title += "-" + sdf.format(new Date(MyUtil.getWeekMillis(1)-3600000));
			}else if("本月".equals(str)){
				sdf.applyPattern("MM月");
				title = sdf.format(new Date(MyUtil.getMonthMillis(0))) +"01日-"+ sdf.format(new Date(MyUtil.getMonthMillis(0)));
				Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
				int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				title += maxDays+"日";
			}
			
			if(tv_date!=null)
			tv_date.setText(title);
		}
	}
	private CpButtonClickListener listener;
	public void setOnCpButtonClickListener(CpButtonClickListener listener){
		this.listener = listener;
	}
	private void initUI(Context context) {
		View view = View.inflate(context, R.layout.main_middle_item, null);
		isHightDpi = MyUtil.isHighDpi(context);
		if(isHightDpi){
			tv_date = (TextView) view.findViewById(R.id.tv_date);
		}
		tv_imcome = (TextView) view.findViewById(R.id.tv_imcome);
		tv_expense = (TextView) view.findViewById(R.id.tv_expense);
		tv_today = (TextView) view.findViewById(R.id.tv_today);
		if(view!=null){
			this.addView(view);
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if(listener!=null){
				listener.onclick(this);
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
}
