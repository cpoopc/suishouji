package com.cp.suishouji.widgt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainBottomItem extends RelativeLayout{
	
	private TextView tv_imcome;
	private TextView tv_expense;
	private TextView tv_category;
	private TextView tv_date;
	private ImageView img_icon;
	private CpButtonClickListener listener;
	public void setOnCpButtonClickListener(CpButtonClickListener listener){
		this.listener = listener;
	}
	public MainBottomItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI(context);
	}
	public void setTransactionInfo(TransactionInfo info){
		String str = null;
		if(info.type==1){//收入
			tv_expense.setVisibility(View.GONE);
			tv_imcome.setVisibility(View.VISIBLE);
			tv_imcome.setText("¥ "+MyUtil.doubleFormate(info.buyerMoney));
			str = "[收入]";
		}else{//支出
			tv_imcome.setVisibility(View.GONE);
			tv_expense.setVisibility(View.VISIBLE);
			tv_expense.setText("¥ "+MyUtil.doubleFormate(info.buyerMoney));
			str = "[支出]";
		}
			tv_category.setText(info.name);
			img_icon.setImageResource(info.imgResId);
//			Log.e("MainBottonButon:info.tradeTime", info.tradeTime+":+currenttime:"+System.currentTimeMillis());
			Date date = new Date(info.tradeTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			tv_date.setText(sdf.format(date)+str);
	}
	public void restore(){
		tv_imcome.setVisibility(View.GONE);
		tv_expense.setVisibility(View.VISIBLE);
		tv_expense.setText("¥ 0.00");
		tv_category.setText("暂无记录");
		tv_date.setText("----.--.--");
	}
	private void initUI(Context context) {
		View view = View.inflate(context, R.layout.main_bottom_item, null);
		tv_imcome = (TextView) view.findViewById(R.id.tv_imcome);
		tv_expense = (TextView) view.findViewById(R.id.tv_expense);
		tv_category = (TextView) view.findViewById(R.id.tv_category);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		img_icon = (ImageView) view.findViewById(R.id.img_icon);
		if(view!=null){
			this.addView(view);
		}
	}
	
	boolean flag;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(ev.getX()<0||ev.getX()>getWidth()||ev.getY()<0||ev.getY()>getHeight()){
			flag = false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			flag = true;
			break; 
		case MotionEvent.ACTION_UP:
			if(flag&&listener!=null){
				listener.onclick(this);
			}
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
