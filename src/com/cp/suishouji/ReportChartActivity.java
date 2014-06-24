package com.cp.suishouji;

import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class ReportChartActivity extends UmengSherlockActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_chart);
		
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
