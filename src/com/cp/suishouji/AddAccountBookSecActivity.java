package com.cp.suishouji;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.suishouji.adapter.AbstractAdapter;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AddAccountBookSecActivity extends UmengSherlockActivity implements OnClickListener, OnItemClickListener {

	private TextView tv_title;
	private String curName;
	private String curImgName;
	private int defaultno;
	private long clientID;
//	private int imgRes[] = new int[]{R.drawable.suite_bg_for_car,R.drawable.suite_bg_for_common_1,R.drawable.suite_bg_for_fitment,
//			R.drawable.suite_bg_for_marry,R.drawable.suite_bg_for_standard,R.drawable.suite_bg_for_travel};
	private String[] imgNames = new String[]{"suite_bg_for_standard","suite_bg_for_car","suite_bg_for_common_1",
			"suite_bg_for_fitment","suite_bg_for_marry","suite_bg_for_travel"};
	private ArrayList<Info> infoList;
	private MyAdapter myAdapter; 
	private EditText editText;
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_account_book_sec);
		initActionbar();
		editText = (EditText) findViewById(R.id.editText1);
		imageView = (ImageView) findViewById(R.id.img_book);
		
		//判断是否从编辑选项跳转过来
		clientID = -1;
		Intent intent = getIntent();
		String imgName = intent.getStringExtra("imgName");
		String name = intent.getStringExtra("name");
		clientID = intent.getLongExtra("clientID",-1);
		if(clientID!=-1){
			//从编辑选项跳转过来
			editText.setText(name);
			for (int i = 0; i < imgNames.length; i++) {
				if(imgNames[i].contains(imgName)){
					defaultno = i;
					break;
				}
			}
		}
		
		
		initData();
		GridView gridView = (GridView) findViewById(R.id.gridView1);
		myAdapter = new MyAdapter();
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(myAdapter);
//		gridView.seta
	}
	private void initData() {
		infoList = new ArrayList<AddAccountBookSecActivity.Info>();
		for (int i = 0; i < imgNames.length; i++) {
			if(i==defaultno){
				curImgName = imgNames[i];
				imageView.setImageResource(getResources().getIdentifier(curImgName, "drawable", getPackageName()));
				infoList.add(new Info(curImgName, true));
			}else{
				infoList.add(new Info(imgNames[i], false));
			}
		}
	}
	class Info{
		String imgName;
		boolean isChecked;
		public Info(String imgName, boolean isChecked) {
			super();
			this.imgName = imgName;
			this.isChecked = isChecked;
		}
		
	}
	class MyAdapter extends AbstractAdapter{

		@Override
		public int getCount() {
			return infoList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(R.layout.item_accountbook_add, null);
			Info info = infoList.get(position);
			ImageView img_book = (ImageView) layout.findViewById(R.id.img_book);
			ImageView img_check = (ImageView) layout.findViewById(R.id.img_check);
			img_book.setImageResource(getResources().getIdentifier(info.imgName, "drawable", getPackageName()));
			if(!info.isChecked){
				img_check.setVisibility(View.INVISIBLE);
			}else{
				layout.setBackgroundColor(getResources().getColor(R.color.oringe_string));
			}
			return layout;
		}}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_account_titlebar, null);
		inflate.findViewById(R.id.img_back).setOnClickListener(this);
		inflate.findViewById(R.id.img_ok).setOnClickListener(this);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		tv_title.setText("添加账本");
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			setResult(0);
			finish();
			break;
		case R.id.img_ok:
			save();
			finish();
			break;

		default:
			break;
		}
	}
	private void save() {
		SQLiteDatabase db = DataBaseUtil.getDb();
		ContentValues values = new ContentValues();
		curName = editText.getText().toString().trim();
		values.put("name", curName);
		values.put("imgName", curImgName);
		//若是编辑状态,不需要获取
		if(clientID==-1){
			Cursor query = db.query("t_account_book", null, null, null, null, null, "clientID");
			query.moveToLast();
			clientID = query.getLong(query.getColumnIndex("clientID"))+1;
			values.put("clientID", clientID);
			db.insert("t_account_book", null, values);
		}else{
			values.put("clientID", clientID);
			db.update("t_account_book", values, "clientID=?", new String[]{String.valueOf(clientID)});
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		for (int i = 0; i < infoList.size(); i++) {
			if(position==i){
				curImgName = infoList.get(i).imgName;
				infoList.get(i).isChecked = true;
				imageView.setImageResource(getResources().getIdentifier(infoList.get(i).imgName, "drawable", getPackageName()));
			}else{
				infoList.get(i).isChecked = false;
			}
		}
		myAdapter.notifyDataSetChanged();
	}
}
