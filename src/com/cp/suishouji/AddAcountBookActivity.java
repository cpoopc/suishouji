package com.cp.suishouji;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.AbstractAdapter;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.app.ActivityManager;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddAcountBookActivity extends UmengSherlockActivity implements OnClickListener{
	private TextView tv_title;
	private String names[] = new String[]{"标准理财","生意账本","旅游账本","装修账本","结婚账本","汽车账本","宝宝账本"};
	private String memos[] = new String[]{"标准账本,分类较全","帮你轻松打理生意流水账","适合出游,精心定义旅行分类","装修必备,贴心为装修场景打造"
			,"走入神圣殿堂钱,记一记更幸福","轻松记录爱车消费","记一笔,拍个照,给宝宝留一段难忘的回忆"};
	private int imgResid[] = new int[]{R.drawable.acc_book_template_bzlc,R.drawable.acc_book_template_syz,
			R.drawable.acc_book_template_lyz,R.drawable.acc_book_template_zxz,
			R.drawable.acc_book_template_jhz,R.drawable.acc_book_template_qcz,R.drawable.acc_book_template_bbz};
	private ArrayList<Info> infoList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_acount_book);
		initActionbar();
		ListView listView = initData();
		listView.setAdapter(new MyAdapter());
		listView.setOnItemClickListener(new MyListener());
	}
	class MyListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			startActivity(new Intent(AddAcountBookActivity.this,AddAccountBookSecActivity.class));
			finish();
		}
		
	}
	private ListView initData() {
		infoList = new ArrayList<AddAcountBookActivity.Info>();
		ListView listView = (ListView) findViewById(R.id.listView1);
		for (int i = 0; i < names.length; i++) {
			infoList.add(new Info(names[i], memos[i], imgResid[i]));
		}
		return listView;
	}
	class Info{
		String name;
		String memo;
		int imgRes;
		public Info(String name, String memo, int imgRes) {
			super();
			this.name = name;
			this.memo = memo;
			this.imgRes = imgRes;
		}
		
	}
	class MyAdapter extends AbstractAdapter{

		@Override
		public int getCount() {
			return infoList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(R.layout.item_account_book, null);
			Info info = infoList.get(position);
			ImageView imageView = (ImageView) layout.findViewById(R.id.imageView1);
			TextView textView1 = (TextView) layout.findViewById(R.id.textView1);
			TextView textView2 = (TextView) layout.findViewById(R.id.textView2);
			imageView.setImageResource(info.imgRes);
			textView1.setText(info.name);
			textView2.setText(info.memo);
			return layout;
		}}
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
		tv_title.setText("添加账本");
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
