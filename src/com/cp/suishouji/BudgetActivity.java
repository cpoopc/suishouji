package com.cp.suishouji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.BudgetAdapter;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.dao.ExpenseInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 预算
 * @author cp
 *
 */
public class BudgetActivity extends UmengSherlockActivity implements OnClickListener, OnItemClickListener{
	private TextView tv_title;
	private ListView listView;
	private ArrayList<ExpenseInfo> expenseList;//主页查询传来的消费记录
	private HashMap<Integer, ArrayList<CategoryInfo>> childlistMap; //
	private ArrayList<CategoryInfo> fatherList;
	private BudgetAdapter budgetAdapter;
	private TextView tv_total;
	private TextView tv_income;
	private TextView tv_expense;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
		initActionbar();
		listView = (ListView) findViewById(R.id.listView1);
		Intent data = getIntent();
		expenseList = data.getParcelableArrayListExtra("expenseList");
		if(expenseList!=null){
			Collections.sort(expenseList);//排序,提高效率
		}else{
			Toast.makeText(this, "请先记账", Toast.LENGTH_SHORT).show();
		}
		initData();
		addHeadview();
		budgetAdapter = new BudgetAdapter(this, fatherList, false);
		listView.setAdapter(budgetAdapter);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * 与expenseList结合,计算出所有分类花费;
	 */
	private void initData(){
		SQLiteDatabase db = DataBaseUtil.getDb();
		//TODO 读取一级分类信息,二级分类
		Cursor query_category = db.query("t_category", null, "type=?",
				new String[]{String.valueOf(0)}, null, null, "categoryPOID");//消费类
		boolean toLast = query_category.moveToLast();
		ArrayList<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();
		fatherList = new ArrayList<CategoryInfo>();
		ArrayList<CategoryInfo> childList = new ArrayList<CategoryInfo>();
		childlistMap = new HashMap<Integer, ArrayList<CategoryInfo>>();//二级分类列表图
		while(toLast){
			String name = query_category.getString(query_category.getColumnIndex("name"));
			String _tempIconName = query_category.getString(query_category.getColumnIndex("_tempIconName"));
			int categoryPOID = query_category.getInt(query_category.getColumnIndex("categoryPOID"));
			int parentCategoryPOID = query_category.getInt(query_category.getColumnIndex("parentCategoryPOID"));
			int ordered = query_category.getInt(query_category.getColumnIndex("ordered"));
			CategoryInfo info = new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered);
			categoryList.add(info);
			if(parentCategoryPOID == -1 ){
				fatherList.add(info);
			}else{
				childList.add(info);
			}
			toLast = query_category.moveToPrevious();
		}
//		Log.e("expenseList", expenseList.toString());
//		Log.e("categoryList", categoryList.toString());
		
		
		// 读取预算信息
				Cursor query_budget = db.query("t_budget_item", null, null, null, null, null, null);
				boolean toFirst_budget = query_budget.moveToFirst();
				ArrayList<ExpenseInfo> budgetList = new ArrayList<ExpenseInfo>();	//预算
				while(toFirst_budget){
					long sellerCategoryPOID = query_budget.getLong(query_budget.getColumnIndex("categoryPOID"));
					double amount = query_budget.getDouble(query_budget.getColumnIndex("amount"));
					budgetList.add(new ExpenseInfo(sellerCategoryPOID, amount));
					toFirst_budget = query_budget.moveToNext();
				}
		/**
		 * 加入消费信息,预算信息
		 */
		if(expenseList!=null){
			int count_expense = 0;
			int count_budget = 0;
			for (int i = 0; i < categoryList.size(); i++) {
				CategoryInfo categoryInfo = categoryList.get(i);
				for (int j = count_expense; j < expenseList.size(); j++) {
					if(expenseList.get(j).sellerCategoryPOID == categoryInfo.getCategoryPOID()){
						count_expense ++;
						categoryInfo.expense +=expenseList.get(j).buyermoney;//汇总花费
					}
				}
				for (int j = count_budget; j < budgetList.size(); j++) {
					if(budgetList.get(j).sellerCategoryPOID == categoryInfo.getCategoryPOID()){
						count_budget ++;
						categoryInfo.budget =budgetList.get(j).buyermoney;//预算
					}
				}
			}
		}
//		Log.e("categoryList", categoryList.toString());
		/**
		 * 二级分类花费汇总到一级类,并加入map
		 */
		Collections.sort(fatherList, new Comparator<CategoryInfo>() {

			@Override
			public int compare(CategoryInfo lhs, CategoryInfo rhs) {
				return rhs.getCategoryPOID() - lhs.getCategoryPOID();
			}
		} );
		Collections.sort(childList, new Comparator<CategoryInfo>() {
			
			@Override
			public int compare(CategoryInfo lhs, CategoryInfo rhs) {
				return rhs.getParentCategoryPOID() - lhs.getParentCategoryPOID();
			}
		} );
		int count_child = 0;
		for (int i = 0; i < fatherList.size(); i++) {
			CategoryInfo infoFather = fatherList.get(i);
			CategoryInfo infoChild;
			ArrayList<CategoryInfo> infoList = new ArrayList<CategoryInfo>();
			for (int j = count_child; j < childList.size(); j++) {
				infoChild = childList.get(j);
				if(infoFather.getCategoryPOID() == infoChild.getParentCategoryPOID()){
					count_child ++;
					infoFather.expense += infoChild.expense;
					infoList.add(infoChild);
				}
			}
			childlistMap.put(infoFather.getCategoryPOID(), infoList);
		}
	}


	private void addHeadview() {
		View headview = getLayoutInflater().inflate(R.layout.item_nav_transaction_headerview, null);
		tv_total = (TextView) headview.findViewById(R.id.tv_total);
		tv_income = (TextView) headview.findViewById(R.id.tv_income);
		tv_expense = (TextView) headview.findViewById(R.id.tv_expense);
		TextView tv_11 = (TextView) headview.findViewById(R.id.tv_11);
		TextView tv_21 = (TextView) headview.findViewById(R.id.tv_21);
		TextView tv_22 = (TextView) headview.findViewById(R.id.tv_22);
		tv_11.setText("总预算:");
		tv_21.setText("已用:");
		tv_22.setText("可用:");
		double budget = 0;
		double expense = 0;
		
		for (int i = 0; i < fatherList.size(); i++) {
			budget += fatherList.get(i).budget;
			expense += fatherList.get(i).expense;
		}
		tv_total.setText("¥ "+MyUtil.doubleFormate(budget));		//总预算
		tv_income.setText("¥ "+MyUtil.doubleFormate(expense));//已用
		tv_expense.setText("¥ "+MyUtil.doubleFormate(budget - expense));//可用
		listView.addHeaderView(headview);
	}

	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_account_titlebar, null);
		inflate.findViewById(R.id.title).setOnClickListener(this);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		tv_title.setText("一级预算");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position!=0){
			int categoryPOID = fatherList.get(position-1).getCategoryPOID();
			ArrayList<CategoryInfo> childList = childlistMap.get(categoryPOID);//去掉headview
			Intent intent = new Intent(BudgetActivity.this,BudgetSecActivity.class);
			if(childList.get(0).getCategoryPOID()!=categoryPOID){
				childList.add(0, fatherList.get(position-1));
			}
			intent.putParcelableArrayListExtra("categoryList", childList);
			intent.putExtra("position", position);
			startActivityForResult(intent, 0);//TODO 如果更改了预算,回来时要更新
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1){
			if(data!=null){
				int position = data.getIntExtra("position", 0);
				if(position!=0){
					double newbudget = data.getDoubleExtra("budget", 0);
					
					fatherList.get(position-1).budget = newbudget;
					budgetAdapter.notifyDataSetChanged();
					double budget = 0,expense=0;
					for (int i = 0; i < fatherList.size(); i++) {
						budget += fatherList.get(i).budget;
						expense += fatherList.get(i).expense;
					}
					tv_total.setText("¥ "+MyUtil.doubleFormate(budget));		//总预算
					tv_income.setText("¥ "+MyUtil.doubleFormate(expense));//已用
					tv_expense.setText("¥ "+MyUtil.doubleFormate(budget - expense));//可用
				}
			}
		}
	}
}
