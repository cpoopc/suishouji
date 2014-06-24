package com.cp.suishouji;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.AccountAdapter;
import com.cp.suishouji.dao.AccountInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 账户信息
 * 数据库表"t_account"-->childInfolist(子信息)
 * 表"t_account_group"-->parentInfo(父信息,所属分类)
 * 			-->secendparentInfo(二级分类)
 * @author cp
 *	
 */
public  class AccountDetailActivity extends UmengSherlockActivity implements OnClickListener {

	private TextView tv_title;
	private ListView listView;
	private ArrayList<AccountInfo> accountList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accountdetail);
		initActionbar();
		listView = (ListView) findViewById(R.id.listView1);
		accountList = readDb();
		double balance = 0;
		double amountOfLiability = 0;
		double amountOfCredit = 0;
		for (int i = 0; i < accountList.size(); i++) {
			if(accountList.get(i).parentAccountGroupPOID == 1){
				balance += accountList.get(i).balance;
				amountOfLiability += accountList.get(i).amountOfLiability;
				amountOfCredit += accountList.get(i).amountOfCredit;
			}
		}
		AccountAdapter accountAdapter = new AccountAdapter(this, accountList);
		View headview = getLayoutInflater().inflate(R.layout.item_nav_transaction_headerview, null);
		TextView tv_total = (TextView) headview.findViewById(R.id.tv_total);
		TextView tv_income = (TextView) headview.findViewById(R.id.tv_income);
		TextView tv_expense = (TextView) headview.findViewById(R.id.tv_expense);
		((TextView) headview.findViewById(R.id.tv_11)).setText("净资产:");
		((TextView) headview.findViewById(R.id.tv_21)).setText("资产:");
		((TextView) headview.findViewById(R.id.tv_22)).setText("负债:");
		tv_total.setText(MyUtil.doubleFormate(balance-amountOfLiability+amountOfCredit));
		tv_income.setText(MyUtil.doubleFormate(balance+amountOfCredit));
		tv_expense.setText(MyUtil.doubleFormate(amountOfLiability));
		listView.addHeaderView(headview);
		listView.setAdapter(accountAdapter);
	}
/**
 * readDb伪代码
 * 查询数据库
 * accountInfolist<--db.query("t_account");				//显示数据name,balance
 * 查询数据库
 * db.query("t_account_group")									//显示数据 name
 * if(parentAccountGroupPOID=1)
	 * groupInfoTopMap<--;//第一分类,以accountGroupPOID作索引
	 * else
	 * groupInfoSecMap<--;//第二分类,以parentAccountGroupPOID作索引
 * list = new arraylist
 *  
 * 
 * 数据库不一致,此方法失效
 * for(;AccountInfolist.size;)
	 * groupTopInfo = groupInfoMap.get(accountInfolist.get(i).accountGroupPOID)//获得第一分类
	 * groupSecInfo = groupInfoMap.get(groupTopInfo.accountGroupPOID)//第二分类
	 * accountInfolist.get(i).parentname=
	 * list.add(groupTopInfo)
	 * list.add([account.get(i)+groupSecInfo]);
	 * 
 */
	public static  ArrayList<AccountInfo> readDb(){
		ArrayList<AccountInfo> list = new ArrayList<AccountInfo>();
		SQLiteDatabase db = DataBaseUtil.getDb();
		//读取账户信息
		Cursor query_account = db.query("t_account", null, null, null, null, null, "ordered");
		ArrayList<AccountInfo> accountList = new ArrayList<AccountInfo>();
		boolean toFirst1 = query_account.moveToFirst();
		AccountInfo accountInfo;
		while(toFirst1){
			accountInfo = new AccountInfo();
			accountInfo.name = query_account.getString(query_account.getColumnIndex("name"));
			accountInfo.balance = query_account.getDouble(query_account.getColumnIndex("balance"));
			accountInfo.amountOfLiability = query_account.getDouble(query_account.getColumnIndex("amountOfLiability"));
			accountInfo.amountOfCredit = query_account.getDouble(query_account.getColumnIndex("amountOfCredit"));
			accountInfo.accountPOID = query_account.getLong(query_account.getColumnIndex("accountPOID"));
			accountInfo.accountGroupPOID = query_account.getLong(query_account.getColumnIndex("accountGroupPOID"));
			accountList.add(accountInfo);
			toFirst1 = query_account.moveToNext();
		}
//		Log.e("accountList", accountList.toString());
		//按groupid排序
		Collections.sort(accountList);
//		Log.e("accountList", accountList.toString());
		HashMap<Long, AccountInfo> groupTopMap = new HashMap<Long, AccountInfo>();
		//读取分组信息
		Cursor query_group = db.query("t_account_group", null, null, null, null, null, null);
		boolean toFirst2 = query_group.moveToFirst();
		while(toFirst2){
			accountInfo = new AccountInfo();
			accountInfo.name = query_group.getString(query_group.getColumnIndex("name"));
			accountInfo.accountGroupPOID = query_group.getLong(query_group.getColumnIndex("accountGroupPOID"));
			accountInfo.parentAccountGroupPOID = query_group.getLong(query_group.getColumnIndex("parentAccountGroupPOID"));
			accountInfo.type = query_group.getInt(query_group.getColumnIndex("type"));
			groupTopMap.put(accountInfo.accountGroupPOID, accountInfo);
			toFirst2 = query_group.moveToNext();
		}
//		Log.e("groupTopMap", groupTopMap.toString());
		for (int i = 0; i < accountList.size(); i++) {
			accountInfo = accountList.get(i);
			AccountInfo groupinfo = groupTopMap.get(accountInfo.accountGroupPOID);
			if(groupinfo.parentAccountGroupPOID==1){
				//一级目录
//				groupTopMap.get(groupinfo.);
				groupinfo.balance +=accountInfo.balance;
				groupinfo.amountOfLiability +=accountInfo.amountOfLiability;
				groupinfo.amountOfCredit +=accountInfo.amountOfCredit;
				accountInfo.type = groupinfo.type;
				if(!list.contains(groupinfo)){
					list.add(groupinfo);
				}
			}else{
				//二级目录
				accountInfo.secgroupname = groupinfo.name;
				AccountInfo groupTop = groupTopMap.get(groupinfo.parentAccountGroupPOID);
				groupTop.balance +=accountInfo.balance;
				groupTop.amountOfLiability +=accountInfo.amountOfLiability;
				groupTop.amountOfCredit +=accountInfo.amountOfCredit;
				accountInfo.type = groupTop.type;
				if(!list.contains(groupTop)){
					list.add(groupTop);
				}
			}
			list.add(accountInfo);
		}
//		Log.e("groupTopMap", groupTopMap.toString());
//		Log.e("list", list.toString());
		return list;
	}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_account_titlebar, null);
		inflate.findViewById(R.id.title).setOnClickListener(this);
		inflate.findViewById(R.id.img_ok).setVisibility(View.GONE);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			finish();
			break;
		default:
			break;
		}
	}

}
