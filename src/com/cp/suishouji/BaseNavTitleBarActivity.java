package com.cp.suishouji;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.MySpinnerAdapter;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;

public  abstract class BaseNavTitleBarActivity extends UmengSherlockActivity implements OnClickListener, OnItemSelectedListener {

	private TextView tv_title;
	private Spinner spinner;
	private MySpinnerAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionbar();
	}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_base_navtitlebar, null);
//		inflate.findViewById(R.id.img_back).setOnClickListener(this);
		inflate.findViewById(R.id.img_add).setOnClickListener(this);
		inflate.findViewById(R.id.title).setOnClickListener(this);
		adapter = new MySpinnerAdapter(this,new String[]{"大帝国饭店","冻噶","大哥大好"});
		spinner = (Spinner) inflate.findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(adapter);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		actionBar.setCustomView(inflate,layout);
	}
	public void setSpinnerText(String[] str){
//		MySpinnerAdapter adapter = new MySpinnerAdapter(this, str);
//		spinner.setAdapter(adapter);
			adapter.setStr(str);
	}
	public void setActionbarTitle(int year){
		tv_title.setText(year+"年流水账");
	}
	public void setActionbarTitle(String str){
		tv_title.setText(str);
	}

}
