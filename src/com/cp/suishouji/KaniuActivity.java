package com.cp.suishouji;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.utils.UmengSherlockActivity;
public class KaniuActivity extends UmengSherlockActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kaniu_install_sms);
		initActionbar();
	}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_kaniu, null);
		inflate.findViewById(R.id.title).setOnClickListener(this);
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
