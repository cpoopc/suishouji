package com.cp.suishouji;


import java.util.ArrayList;

import com.cp.suishouji.adapter.NavTransactionListAdapter;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.widgt.MyListView;
import com.cp.suishouji.widgt.PullToRefreshView;
import com.cp.suishouji.widgt.PullToRefreshView.OnFooterRefreshListener;
import com.cp.suishouji.widgt.PullToRefreshView.OnHeaderRefreshListener;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 每日流水账
 * @author Administrator
 *
 */
public class NavTransactionActivity extends BaseNavTitleBarActivity implements OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener{
	private MyListView mListView;
	private ArrayList<TransactionInfo> transationList = new ArrayList<TransactionInfo>();
	private PullToRefreshView mPullToRefreshView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nav_transaction);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (MyListView) findViewById(R.id.xlistview);
		transationList = TransactionInfo.query(this, MyUtil.getDayMillis(0), MyUtil.getDayMillis(1));
//		Log.e("transationList", transationList.toString());
		mListView.setAdapter(new NavTransactionListAdapter(this, transationList));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}


}
