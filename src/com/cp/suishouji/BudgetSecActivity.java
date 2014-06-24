package com.cp.suishouji;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cp.suishouji.adapter.BudgetAdapter;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.fragment.CalculateFragment;
import com.cp.suishouji.fragment.CalculateSimpleFragment;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengSherlockFragmentActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BudgetSecActivity extends UmengSherlockFragmentActivity implements OnClickListener{
	private TextView tv_title;
	private ListView listView;
	private ArrayList<CategoryInfo> childList;
	private FragmentManager fm;
	private CalculateSimpleFragment calculateSimpleFragment;
	private View flowview;
	private int selecteditem;
	private BudgetAdapter budgetAdapter;
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget_sec);
		initActionbar();
		Intent data = getIntent();
		childList = data.getParcelableArrayListExtra("categoryList");//必须传来
		position = data.getIntExtra("position", 0);
		flowview = findViewById(R.id.flowview);
		calculateSimpleFragment = new CalculateSimpleFragment();
		fm = getSupportFragmentManager();
		fm.beginTransaction().add(R.id.fragment_container, calculateSimpleFragment).commit();
//		fm.beginTransaction().hide(calculateSimpleFragment);
		if(childList!=null){
			listView = (ListView) findViewById(R.id.listView1);
			budgetAdapter = new BudgetAdapter(this, childList ,true);
			listView.setAdapter(budgetAdapter);
			listView.setOnItemClickListener(new ItemClickListener());
			listView.setSelection(0);
		}
	}
	class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//TODO 设置预算值到计算器上
//			parent.setSelected(true);
			double budget = childList.get(position).budget;
			if(flowview.getVisibility() == View.GONE){
				view.setSelected(true);
				selecteditem = position;
				calculateSimpleFragment.setResult(budget);
//				calculateSimpleFragment.setResult(99.9);
				flowview.setVisibility(View.VISIBLE);
			}else{
				view.setSelected(false);
				flowview.setVisibility(View.GONE);
			}
			
		}
	}
	
	public void save(double result){
		//TODO 隐藏计算器,并把结果更新到界面
		CategoryInfo categoryInfo = childList.get(selecteditem);
		if(selecteditem!=0){
//			CategoryInfo categoryInfo0 = childList.get(0);
			double totalchild = 0;
			for (int i = 1; i < childList.size(); i++) {
				if(i==selecteditem){
					totalchild += result;
				}else{
					totalchild += childList.get(i).budget;
				}
			}
			if(totalchild>childList.get(0).budget){
				Toast.makeText(this, "二级分类预算不能超过一级分类预算", Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			double totalchild = 0;
			for (int i = 1; i < childList.size(); i++) {
				totalchild += childList.get(i).budget;
			}
			if(totalchild>result){
				Toast.makeText(this, "一级分类预算不能小于二级分类预算", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		flowview.setVisibility(View.GONE);
		categoryInfo.budget = result;
		budgetAdapter.notifyDataSetChanged();
		SQLiteDatabase db = DataBaseUtil.getDb();
		ContentValues values = new ContentValues();
		values.put("amount", result);
		Log.e("数据库insert", "result:"+result);
			Cursor query1 = db.query("t_budget_item", null, "categoryPOID=?", new String[]{String.valueOf(categoryInfo.getCategoryPOID())}, 
					null, null, null);
			if(query1.moveToFirst()){

				Log.e("数据库update", "有该条目");
			db.update("t_budget_item", values, "categoryPOID=?", new String[]{String.valueOf(categoryInfo.getCategoryPOID())});
			}else{
				Cursor query = db.query("t_budget_item", null, null, null, null, null, null);
				query.moveToLast();
				long budgetItemPOID = query.getLong(query.getColumnIndex("budgetItemPOID")) - 1;
				Log.e("数据库insert", "result:"+result+":budgetItemPOID:"+budgetItemPOID+" categoryInfo.getCategoryPOID():"+categoryInfo.getCategoryPOID());
				values.put("budgetItemPOID", budgetItemPOID);
				values.put("tradingEntityPOID", -3);
				values.put("categoryPOID", categoryInfo.getCategoryPOID());
				db.insert("t_budget_item", null, values);
			}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Toast.makeText(this, "退出二级预算界面", 0).show();
			//返回新budget
			Intent data = new Intent();
			data.putExtra("position", position);
			data.putExtra("budget", childList.get(0).budget);
			setResult(1, data);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_account_titlebar, null);
		inflate.findViewById(R.id.img_back).setOnClickListener(this);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		tv_title.setText("二级预算");
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			//返回新budget
			Intent data = new Intent();
			data.putExtra("position", position);
			data.putExtra("budget", childList.get(0).budget);
			setResult(1, data);
			finish();
			break;
		default:
			break;
		}
	}

}
