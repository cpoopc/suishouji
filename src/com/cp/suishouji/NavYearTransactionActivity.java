package com.cp.suishouji;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.cp.suishouji.adapter.NavTransactionExListAdapter;
import com.cp.suishouji.adapter.NavTransactionListAdapter;
import com.cp.suishouji.adapter.NavTransactionListProAdapter.OnMoneyChangeListener;
import com.cp.suishouji.dao.IncomeExpenseInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.widgt.MyExpandableListView;
import com.cp.suishouji.widgt.MyListView;
import com.cp.suishouji.widgt.PullToRefreshView;
import com.cp.suishouji.widgt.PullToRefreshView.OnFooterRefreshListener;
import com.cp.suishouji.widgt.PullToRefreshView.OnHeaderRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 年流水账,ExpandableList
 * @author cp
 *
 */
public class NavYearTransactionActivity extends BaseNavTitleBarActivity implements OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener{
	private ExpandableListView mExpandablelistview;
	private ArrayList<ArrayList<TransactionInfo>> monthTransactionList = new ArrayList<ArrayList<TransactionInfo>>();
	private ArrayList<IncomeExpenseInfo> ieList = new ArrayList<IncomeExpenseInfo>();
	private PullToRefreshView mPullToRefreshView;
	private int height;
	private int lastexpandposition = -1;
	private int year;
	private int month;
	private double total_income = 0;
	private double total_expense = 0;
	private TextView tv_total;
	private TextView tv_income;
	private TextView tv_expense;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nav_year_transaction);
		mExpandablelistview = (ExpandableListView) findViewById(R.id.expandablelistview);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//东八区
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		setActionbarTitle(year);
		setSpinnerText(new String[]{"上一年","下一年"});
		//TODO 耗时?
		initData();
		//headview
		initExHeaderView();
		
		mExpandablelistview.setGroupIndicator(null);
		adapter = new NavTransactionExListAdapter(this,monthTransactionList,ieList,year);
		adapter.setOnMoneyChangeListener(new MoneyChangeListener());
		mExpandablelistview.setAdapter(adapter);
		//初始化高度
		LayoutParams params = mExpandablelistview.getLayoutParams();
		params.height = (int) ((month+1)*MyUtil.dip2px(this, 55)
		+ MyUtil.dip2px(NavYearTransactionActivity.this,86));//groupitem 高度  dp
//		params.height = (int) ((month+1)*getResources().getDimension(R.dimen.groupheight)
//				+ MyUtil.dip2px(NavYearTransactionActivity.this,86));//groupitem 高度  dp
		height = params.height;
		mExpandablelistview.setLayoutParams(params);
		setExpandListener(adapter);
	}
	/**
	 * 事件分发
	 */
	private int[] locationHeaderviews;
	private View headview;
	private NavTransactionExListAdapter adapter;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(locationHeaderviews==null){
			//计算起始点Y坐标
			locationHeaderviews = new int[2];
			headview.getLocationOnScreen(locationHeaderviews);
		}
		if(adapter.isShown()){
			//菜单显示状态
			View shownView = adapter.getShownView();
			int[] location = new int[2];
			shownView.getLocationOnScreen(location);
			int startY = Math.max(locationHeaderviews[1],location[1]);
			int endY  = Math.max(locationHeaderviews[1],startY + shownView.getHeight()); 
			if(ev.getY()>startY && ev.getY()<endY){
				//点击菜单
				return super.dispatchTouchEvent(ev);
			}else{
				//点击其他位置,隐藏菜单
				adapter.hideMunuView();
				return true;
			}
		}
		
		return super.dispatchTouchEvent(ev);
	}
	//流水账被修改时
	class MoneyChangeListener implements OnMoneyChangeListener{

		@Override
		public void onMoneyChange() {
			renewalData();
		}
	}
	private void initExHeaderView() {
		headview = getLayoutInflater().inflate(R.layout.item_nav_transaction_headerview, null);
		tv_total = (TextView) headview.findViewById(R.id.tv_total);
		tv_income = (TextView) headview.findViewById(R.id.tv_income);
		tv_expense = (TextView) headview.findViewById(R.id.tv_expense);
		tv_income.setText(MyUtil.doubleFormate(total_income));
		tv_expense.setText(MyUtil.doubleFormate(total_expense));
		tv_total.setText(MyUtil.doubleFormate(total_income-total_expense));
		mExpandablelistview.addHeaderView(headview);
	}
	private void renewHeight(final NavTransactionExListAdapter adapter,
			int groupPosition) {
		ArrayList<TransactionInfo> arrayList = monthTransactionList.get(adapter.getGroupCount()-1-groupPosition);
		int count = 1;
		if(arrayList!=null){
			count = arrayList.size();
			LayoutParams params = mExpandablelistview.getLayoutParams();
			params.height = (int) ((month+1)*MyUtil.dip2px(NavYearTransactionActivity.this, 55) 
					+ count*MyUtil.dip2px(NavYearTransactionActivity.this, 49.2f)
					+ MyUtil.dip2px(NavYearTransactionActivity.this,86));
//					- MyUtil.dip2px(NavYearTransactionActivity.this,10);//微调
//			params.height = height+ count*MyUtil.dip2px(NavYearTransactionActivity.this, 50);
			mExpandablelistview.setLayoutParams(params);
		}else{
			LayoutParams params = mExpandablelistview.getLayoutParams();
			params.height = (int) (height+ MyUtil.dip2px(NavYearTransactionActivity.this, 60));
			mExpandablelistview.setLayoutParams(params);
		}
	}
	
	private void setExpandListener(final NavTransactionExListAdapter adapter) {
		mExpandablelistview.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				lastexpandposition = groupPosition;
				adapter.setGroupPosition(groupPosition);
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					if(i!=groupPosition){
						mExpandablelistview.collapseGroup(i);
					}
				}
				renewHeight(adapter, groupPosition);
			}

			
		});
		mExpandablelistview.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				if(groupPosition==lastexpandposition){
					LayoutParams params = mExpandablelistview.getLayoutParams();
					params.height = height;
					mExpandablelistview.setLayoutParams(params);
				}
			}
		});
	}

	private void initData() {
		total_income = 0;total_expense = 0;
		for (int i = 0; i < month+1; i++) {
			// 查询每个月
			ArrayList<TransactionInfo> transationList;
			if(i==11){
				transationList = TransactionInfo.query(this, MyUtil.getYearMonthMillis(year, 11), MyUtil.getYearMonthMillis(year+1, 0));
				monthTransactionList.add(transationList);
			}else{
				transationList = TransactionInfo.query(this, MyUtil.getYearMonthMillis(year, i), MyUtil.getYearMonthMillis(year, i+1));
				monthTransactionList.add(transationList);
//			Log.e("monthTransactionList.get", monthTransactionList.get(i).toString());
//			Log.e("monthTransactionList.get", monthTransactionList.get(i)+"");
				//没有数据为null
			}
			//计算
			double income =0,expense = 0;
			if(transationList!=null){
				for (int j = 0; j < transationList.size(); j++) {
					TransactionInfo info = transationList.get(j);
					if(info.type==1){
						income += info.buyerMoney;
					}else{
						expense += info.buyerMoney;
					}
				}
			}
			total_income +=income;
			total_expense +=expense;
			ieList.add(new IncomeExpenseInfo(income, expense));
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			finish();
			break;
		case R.id.img_add:
			Intent intent = new Intent(this,AddOrEditTransActivity.class);
			startActivityForResult(intent, 0);
			break;

		default:
			break;
		}
	}
	//加载上一年
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		year --;
		renewalData();
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	//加载下一年
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		year ++;
		renewalData();
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	private void renewalData() {
		setActionbarTitle(year);
		if(year == Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.YEAR)){
			month = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MONTH);
		}else{
			month = 11;
		}
		monthTransactionList.clear();
		ieList.clear();
		initData();
		adapter = new NavTransactionExListAdapter(this,monthTransactionList,ieList,year);
		adapter.setOnMoneyChangeListener(new MoneyChangeListener());
		mExpandablelistview.setAdapter(adapter);
		//CP 恢复状态
		mExpandablelistview.expandGroup(lastexpandposition);
		//初始化高度
		renewHeight(adapter, lastexpandposition);
//		LayoutParams params = mExpandablelistview.getLayoutParams();
//		params.height = (int) ((month+1)*MyUtil.dip2px(this, 55)
//				+ MyUtil.dip2px(NavYearTransactionActivity.this,86));//groupitem 高度  dp
//		height = params.height;
//		mExpandablelistview.setLayoutParams(params);
		tv_income.setText(MyUtil.doubleFormate(total_income));
		tv_expense.setText(MyUtil.doubleFormate(total_expense));
		tv_total.setText(MyUtil.doubleFormate(total_income-total_expense));
		setExpandListener(adapter);
	}
//spinner事件
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
			parent.setSelection(0);
			switch (position) {
			case 1:
				//加载上一年
				year --;
				renewalData();
				break;
			case 2:
				//加载下一年
				year ++;
				renewalData();
				break;

			default:
				break;
			}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		renewalData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
