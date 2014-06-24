package com.cp.suishouji.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cp.suishouji.R;
import com.cp.suishouji.adapter.NavTransactionListProAdapter.OnMoneyChangeListener;
import com.cp.suishouji.dao.IncomeExpenseInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 年:12个月:groupview12个
 * childview:n个,根据数据库查询结果,返回transactionlist.size()确定
 * @author Administrator
 *
 */
public class NavTransactionExListAdapter extends BaseExpandableListAdapter{
	private Context context;
	private ArrayList<ArrayList<TransactionInfo>> monthTransactionList;
	private ArrayList<IncomeExpenseInfo> ieList;
	private int year;
	public NavTransactionExListAdapter(Context context,
			ArrayList<ArrayList<TransactionInfo>> monthTransactionList,//) {
			ArrayList<IncomeExpenseInfo> ieList,int year){
		super();
		this.context = context;
		this.monthTransactionList = monthTransactionList;
		this.ieList = ieList;
		this.year = year;
	}

	@Override
	public int getGroupCount() {
		return monthTransactionList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
//		if(count>getGroupCount()){return convertView;}
//		MyUtil.logTime("getGroupView1:groupPosition"+groupPosition);
		View layout = LayoutInflater.from(context).inflate(R.layout.item_nav_exlist_month, null);
		TextView tv_month = (TextView) layout.findViewById(R.id.tv_month);
		
		TextView tv_income = (TextView) layout.findViewById(R.id.tv_income);
		TextView tv_expense = (TextView) layout.findViewById(R.id.tv_expense);
		TextView tv_total = (TextView) layout.findViewById(R.id.tv_total);
		
		TextView tv_date = (TextView) layout.findViewById(R.id.tv_date);
		TextView line_income = (TextView) layout.findViewById(R.id.line_income);
		TextView line_expense = (TextView) layout.findViewById(R.id.line_expense);
		TextView line_total = (TextView) layout.findViewById(R.id.line_total);
		//设置月份
		int m = getGroupCount()-groupPosition;
		String month = "";
		if(m<10){
			month = "0"+m;
		}else{
			month = String.valueOf(m);
		}
		tv_month.setText(month);
		//设置收入,支出,结余
		double income =0,expense = 0,total = 0;
		if(ieList!=null){
			IncomeExpenseInfo expenseInfo = ieList.get(getGroupCount()-groupPosition-1);
			income = expenseInfo.income;
			expense = expenseInfo.expense;
		}
		total = income - expense;
		tv_income.setText(MyUtil.doubleFormate(income));
		tv_expense.setText(MyUtil.doubleFormate(expense));
		tv_total.setText(MyUtil.doubleFormate(total));
		//设置线长度
		MyUtil.setLength(context, line_income.getLayoutParams(), line_expense.getLayoutParams(), line_total.getLayoutParams(), 
				income, expense, total);
		//日期设置 2014.06.01-06.30
//		int month = getGroupCount()-groupPosition-1;//1base
		int day = MyUtil.getMaxDayOfMonth(year, Integer.valueOf(month)-1);
		if(day<10){
			tv_date.setText(year+"."+month+".01"+"-"+month+".0"+day);
		}else{
			tv_date.setText(year+"."+month+".01"+"-"+month+"."+day);
		}
		return layout;
	}

	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View layout = null;
//		MyUtil.logTime("getChildView1:position:"+groupPosition);
		// 耗时:2ms级别
		ArrayList<TransactionInfo> transactionList = monthTransactionList.get(getGroupCount()-groupPosition-1);
		if(transactionList!=null){
			layout = LayoutInflater.from(context).inflate(R.layout.item_nav_list_day, null);
			ListView listView = (ListView) layout.findViewById(R.id.listView1);
			HashMap<Integer,Integer> typeMap = new HashMap<Integer, Integer>();
			initTypeMap(transactionList, typeMap);
			NavTransactionListProAdapter adapter = new NavTransactionListProAdapter(context, transactionList, typeMap);
			//		NavTransactionListAdapter adapter = new NavTransactionListAdapter(context, transactionList);
			adapter.setOnMoneyChangeListener(new MoneyChangeListener());
			listView.setAdapter(adapter);
			childAdpMap.put(groupPosition, adapter);
			//CP 计算高度
			LayoutParams params = listView.getLayoutParams();
			params.height = (int) (transactionList.size()*MyUtil.dip2px(context,49.2f));
			listView.setLayoutParams(params);
		}else{
			//显示没有记录
			layout = LayoutInflater.from(context).inflate(R.layout.item_nav_exlist_empty, null);
		}
//		MyUtil.logTime("getChildView2:position:"+groupPosition);
		return layout;
	}
	private OnMoneyChangeListener moneyChangeListener;
//	public interface OnMoneyChangeListener {
//		public abstract void onMoneyChange();
//	}
	public void setOnMoneyChangeListener(OnMoneyChangeListener moneyChangeListener){
		this.moneyChangeListener = moneyChangeListener;
	}
	//子view变化,调用次方法,此方法再调用外界实现的方法
	class MoneyChangeListener implements OnMoneyChangeListener{

		@Override
		public void onMoneyChange() {
			if(moneyChangeListener!=null){
				moneyChangeListener.onMoneyChange();
			}
		}
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	private void initTypeMap(ArrayList<TransactionInfo> transactionList,HashMap<Integer,Integer> typeMap) {
		long nextdateMillis= MyUtil.getNextDay(transactionList.get(0).tradeTime);
		typeMap.put(0, 2);
		for (int i = 1; i < transactionList.size(); i++) {
			 TransactionInfo info = transactionList.get(i);
		    if(info.tradeTime > nextdateMillis){
		        nextdateMillis = MyUtil.getNextDay(info.tradeTime);
		        typeMap.put(i,2);    //b1=1 头, b1=0 不是头
		        int x = typeMap.get(i-1) ;    //头之前是尾   b0=1 尾,b0=0 不是尾
		        typeMap.put(i-1,  x+1);
		    }else{
		    	typeMap.put(i,0);
		    }
		}
		typeMap.put(transactionList.size()-1, typeMap.get(transactionList.size()-1)+1);
	}
	public void hideMunuView() {
		childAdpMap.get(groupPosition).hideMunuView();
	}

	public boolean isShown() {
		if(childAdpMap.get(groupPosition)==null)return false;
		return childAdpMap.get(groupPosition).isShown();
	}

	public View getShownView() {
		return childAdpMap.get(groupPosition).getShownView();
	}
	private HashMap<Integer, NavTransactionListProAdapter> childAdpMap = new HashMap<Integer, NavTransactionListProAdapter>(); 
	private int groupPosition;
	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}
}
