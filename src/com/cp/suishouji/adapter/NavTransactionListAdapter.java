package com.cp.suishouji.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NavTransactionListAdapter extends AbstractAdapter{
	private Context context;
	private ArrayList<TransactionInfo> transationList;
	
	public NavTransactionListAdapter(Context context,
			ArrayList<TransactionInfo> transationList) {
		super();
		this.transationList = transationList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return transationList.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = LayoutInflater.from(context).inflate(R.layout.item_navtransaction_listview, null);
		TransactionInfo info = transationList.get(position);
		TextView tv_category = (TextView) layout.findViewById(R.id.tv_category);
		TextView tv_expense = (TextView) layout.findViewById(R.id.tv_expense);
		TextView tv_imcome = (TextView) layout.findViewById(R.id.tv_imcome);
		TextView tv_date = (TextView) layout.findViewById(R.id.tv_date);
		ImageView img_icon = (ImageView) layout.findViewById(R.id.img_icon);
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
		if(position==0){
			TextView tv_dayofmonth = (TextView) layout.findViewById(R.id.tv_dayofmonth);
			TextView tv_xingqi = (TextView) layout.findViewById(R.id.tv_xingqi);
			tv_dayofmonth.setText(MyUtil.getDate(info.tradeTime));
			tv_xingqi.setText(MyUtil.getDay(info.tradeTime));
			tv_dayofmonth.setVisibility(View.VISIBLE);
			tv_xingqi.setVisibility(View.VISIBLE);
			MyUtil.getDate(System.currentTimeMillis());
			Log.e("position", position+","+MyUtil.getDay(info.tradeTime)+","+MyUtil.getDate(info.tradeTime));
		}else if(position==transationList.size()-1){
			TextView tv_divider1 = (TextView) layout.findViewById(R.id.tv_divider1);
//			TextView tv_divider2 = (TextView) layout.findViewById(R.id.tv_divider2);
			tv_divider1.setVisibility(View.VISIBLE);
//			tv_divider2.setVisibility(View.VISIBLE);
		}
		Date date = new Date(info.tradeTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		tv_date.setText(sdf.format(date)+str);
		return layout;
	}

}
