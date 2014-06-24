package com.cp.suishouji;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class PsNoteActivity extends UmengSherlockActivity implements OnClickListener{

	private String ps;
	private String category;
	private String buyermoney;
	private EditText edt_ps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ps_note);
		Intent intent = getIntent();
		ps = intent.getStringExtra("ps");
		category = intent.getStringExtra("category");
		buyermoney = intent.getStringExtra("buyermoney");
		TextView tv_category = (TextView) findViewById(R.id.tv_category);
		TextView tv_total = (TextView) findViewById(R.id.tv_total);
		edt_ps = (EditText) findViewById(R.id.edt_ps);
		tv_category.setText(category.split(">")[1]);
		tv_total.setText("金额:¥ "+buyermoney);
		if(!"请输入备注信息".equals(ps)){
			edt_ps.setText(ps);
		}
		initActionbar();
	}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_ps_titlebar, null);
		inflate.findViewById(R.id.img_ok).setOnClickListener(this);
		inflate.findViewById(R.id.title).setOnClickListener(this);
//		TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			finish();
			break;
		case R.id.img_ok:
			Intent data = new Intent();
			data.putExtra("ps", edt_ps.getText().toString());
			setResult(Constants.PSNOTE, data);
			finish();
			break;

		default:
			break;
		}
	}

}
