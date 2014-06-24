package com.cp.suishouji;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.cp.suishouji.adapter.NavTransactionListAdapter;
import com.cp.suishouji.adapter.NavTransactionListProAdapter;
import com.cp.suishouji.adapter.NavTransactionListProAdapter.OnMoneyChangeListener;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.widgt.MyListView;
import com.cp.suishouji.widgt.PullToRefreshView;
import com.cp.suishouji.widgt.PullToRefreshView.OnFooterRefreshListener;
import com.cp.suishouji.widgt.PullToRefreshView.OnHeaderRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 每日流水账
 * @author Administrator
 *
 */
public class NavMonthTransactionActivity extends BaseNavTitleBarActivity implements OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener{
	private ListView mListView;
	private ArrayList<TransactionInfo> transactionList;
	private PullToRefreshView mPullToRefreshView;
	private HashMap<Integer,Integer> typeMap;
	private double income;
	private double expense;
	private int  shift;
	private View headview;
	private TextView tv_total;
	private TextView tv_income;
	private TextView tv_expense;
	private View emptyview;
	private int WHITCH;
	private NavTransactionListProAdapter madapter;
	private int[] locationHeaderviews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nav_transaction);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) findViewById(R.id.xlistview);
		mListView.setOnItemClickListener(this);
		transactionList = new ArrayList<TransactionInfo>();
		String extra = getIntent().getStringExtra("date");
//		setActionbarTitle(extra+"流水账");
		shift = 0;
		if("今天".equals(extra)){
			WHITCH = 1;
			transactionList = TransactionInfo.query(this, MyUtil.getDayMillis(shift), MyUtil.getDayMillis(shift+1));
			setAcbTitle(1);
			setSpinnerText(new String[]{"上一天","下一天"});
		}else if("本周".equals(extra)){
			WHITCH = 2;
			transactionList = TransactionInfo.query(this, MyUtil.getWeekMillis(shift), MyUtil.getWeekMillis(shift+1));
			setAcbTitle(2);
			setSpinnerText(new String[]{"上一周","下一周"});
		}else if("本月".equals(extra)){
			WHITCH = 3;
			setAcbTitle(3);
			transactionList = TransactionInfo.query(this, MyUtil.getMonthMillis(shift), MyUtil.getMonthMillis(shift+1));
			setSpinnerText(new String[]{"上一月","下一月"});
		}
			
			headview = getLayoutInflater().inflate(R.layout.item_nav_transaction_headerview, null);
			tv_total = (TextView) headview.findViewById(R.id.tv_total);
			tv_income = (TextView) headview.findViewById(R.id.tv_income);
			tv_expense = (TextView) headview.findViewById(R.id.tv_expense);
			
			loadView();
	}
	
	
	
	private void loadView() {
		income = 0;
		expense = 0;
		if(transactionList==null){
			transactionList = new ArrayList<TransactionInfo>();
			//TODO 无数据,显示画面
			tv_income.setText("0.0");
			tv_expense.setText("0.0");
			tv_total.setText("0.0");
//			mListView.
			mListView.setAdapter(null);//防止2.3出错
			mListView.addHeaderView(headview);
			emptyview = getLayoutInflater().inflate(R.layout.item_nav_empty, null);
			mListView.addHeaderView(emptyview);
			madapter = new NavTransactionListProAdapter(this, transactionList,null);
			mListView.setAdapter(madapter);
			LayoutParams params = mListView.getLayoutParams();
			params.height = (int) (MyUtil.dip2px(this,120)
					+ MyUtil.dip2px(this,87));
			mListView.setLayoutParams(params);
		}else{
		emptyview = null;
		initTypeMap();
		tv_income.setText(MyUtil.doubleFormate(income));
		tv_expense.setText(MyUtil.doubleFormate(expense));
		tv_total.setText(MyUtil.doubleFormate(income-expense));
		mListView.setAdapter(null);//防止2.3出错
		mListView.addHeaderView(headview);
		madapter = new NavTransactionListProAdapter(this, transactionList,typeMap);
		madapter.setOnMoneyChangeListener(new MoneyChangeListener());
		mListView.setAdapter(madapter);
		LayoutParams params = mListView.getLayoutParams();
		params.height = (int) (transactionList.size()*MyUtil.dip2px(this,49.2f)
				+ MyUtil.dip2px(this,87));
		mListView.setLayoutParams(params);
		}
	}
	class MoneyChangeListener implements OnMoneyChangeListener{

		@Override
		public void onMoneyChange() {
			//TODO 刷新界面
			refresh();
		}}
/**
 * 事件分发
 */
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
	if(locationHeaderviews==null){
		//计算起始点Y坐标
		locationHeaderviews = new int[2];
		headview.getLocationOnScreen(locationHeaderviews);
	}
	if(madapter.isShown()){
		//菜单显示状态
		View shownView = madapter.getShownView();
		int[] location = new int[2];
		shownView.getLocationOnScreen(location);
		int startY = Math.max(locationHeaderviews[1],location[1]);
		int endY  = Math.max(locationHeaderviews[1],startY + shownView.getHeight()); 
		if(ev.getY()>startY && ev.getY()<endY){
			//点击菜单
			return super.dispatchTouchEvent(ev);
		}else{
			//点击其他位置,隐藏菜单
			madapter.hideMunuView();
			return true;
		}
	}
	
	return super.dispatchTouchEvent(ev);
}
@Override
public boolean onTouchEvent(MotionEvent event) {
//	Log.e("流水账", "touchevent"); 
	return super.onTouchEvent(event);
}
	//倒序版
	private void initTypeMap() {
		long todayMillis= MyUtil.getCurrentDay(transactionList.get(0).tradeTime);
		typeMap = new HashMap<Integer, Integer>();
		if(transactionList.get(0).type==1){
			income += transactionList.get(0).buyerMoney;
		}else{
			expense += transactionList.get(0).buyerMoney;
		}
		typeMap.put(0, 2);
		for (int i = 1; i < transactionList.size(); i++) {
			 TransactionInfo info = transactionList.get(i);
			 if(info.type==1){
				 income += info.buyerMoney;
			 }else{
				 expense += info.buyerMoney;
			 }
		    if(info.tradeTime < todayMillis){
		    	todayMillis = MyUtil.getCurrentDay(info.tradeTime);
		        typeMap.put(i,2);    //b1=1 头, b1=0 不是头
		        int x = typeMap.get(i-1) ;    //头之前是尾   b0=1 尾,b0=0 不是尾
		        typeMap.put(i-1,  x+1);
		    }else{
		    	typeMap.put(i,0);
		    }
		}
		typeMap.put(transactionList.size()-1, typeMap.get(transactionList.size()-1)+1);
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
		default:
			break;
		}
	}
/**
 * 上拉加载
 */
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		shift --;
		refresh();
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}
	/**
	 * 下拉刷新
	 */
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		shift ++;
		refresh();
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);
	}



	private void refresh() {
		switch (WHITCH) {
		case 1:
			transactionList = TransactionInfo.query(this, MyUtil.getDayMillis(shift), MyUtil.getDayMillis(shift+1));
			setAcbTitle(1);
			break;
		case 2:
			transactionList = TransactionInfo.query(this, MyUtil.getWeekMillis(shift), MyUtil.getWeekMillis(shift+1));
			setAcbTitle(2);
			break;
		case 3:
			transactionList = TransactionInfo.query(this, MyUtil.getMonthMillis(shift), MyUtil.getMonthMillis(shift+1));
			setAcbTitle(3);
			break;
			
		default:
			break;
		}

		mListView.removeHeaderView(headview);
		if(emptyview!=null){
			mListView.removeHeaderView(emptyview);
		}
		loadView();
	}
	//
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
			switch (position) {
			case 1:
				//加载
				shift --;
				if("上一天".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getDayMillis(shift), MyUtil.getDayMillis(shift+1));
					setAcbTitle(1);
				}else if("上一周".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getWeekMillis(shift), MyUtil.getWeekMillis(shift+1));
					setAcbTitle(2);
				}else if("上一月".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getMonthMillis(shift), MyUtil.getMonthMillis(shift+1));
					setAcbTitle(3);
				}
				mListView.removeHeaderView(headview);
				if(emptyview!=null){
					mListView.removeHeaderView(emptyview);
				}
				loadView();
				break;
			case 2:
				//加载
				shift ++;
				if("下一天".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getDayMillis(shift), MyUtil.getDayMillis(shift+1));
					setAcbTitle(1);
				}else if("下一周".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getWeekMillis(shift), MyUtil.getWeekMillis(shift+1));
					setAcbTitle(2);
				}else if("下一月".equals(parent.getItemAtPosition(position-1))){
					transactionList = TransactionInfo.query(this, MyUtil.getMonthMillis(shift), MyUtil.getMonthMillis(shift+1));
					setAcbTitle(3);
				}
				mListView.removeHeaderView(headview);
				if(emptyview!=null){
					mListView.removeHeaderView(emptyview);
				}
				loadView();
				break;

			default:
				break;
			}
			parent.setSelection(0);
	}
	//每日,每周.,每月流水账
	private void setAcbTitle(int i){
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String title = "";
		switch (i) {
		case 1:
			sdf.applyPattern("MM月d日");
			title = sdf.format(new Date(MyUtil.getDayMillis(shift)));
			break;
		case 2:
			sdf.applyPattern("yyyy.M.d");
			title += sdf.format(new Date(MyUtil.getWeekMillis(shift)));
			sdf.applyPattern("M.d");
			title += "-" + sdf.format(new Date(MyUtil.getWeekMillis(shift+1)-3600000));
			break;
		case 3:
			sdf.applyPattern("yyyy年M月");
			title = sdf.format(new Date(MyUtil.getMonthMillis(shift)));
			break;
		default:
			break;
		}
			setActionbarTitle(title);
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refresh();
		super.onActivityResult(requestCode, resultCode, data);
	}


	boolean isFirst = true;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(isFirst){
			Toast.makeText(this, "长按试试", 0).show();
		}
	}
}
