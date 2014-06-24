package com.cp.suishouji;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.BudgetAdapter;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class ReportPieSecActivity extends UmengSherlockActivity implements OnClickListener{
	private TextView tv_title;
	private ListView listView;
	private ArrayList<CategoryInfo> childList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_pie_sec);
		initActionbar();
		Intent data = getIntent();
		childList = data.getParcelableArrayListExtra("categoryList");//必须传来
		if(childList!=null){
			listView = (ListView) findViewById(R.id.listView1);
			BudgetAdapter budgetAdapter = new BudgetAdapter(this, childList ,true);
			listView.setAdapter(budgetAdapter);
		}
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
			finish();
			break;
		default:
			break;
		}
	}

}
