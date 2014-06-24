package com.cp.suishouji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cp.suishouji.adapter.AbstractAdapter;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengActivity;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 分类管理,可以编辑一级,二级分类
 * @author cp
 *
 */
//﻿categoryPOID	name	parentCategoryPOID	path	depth	lastUpdateTime	userTradingEntityPOID	_tempIconName	usedCount	type	ordered	clientID

public class CategoryManagerActivity extends UmengActivity {
	int[] classify_1 = Constants.classify_1;
	int[][] classify_2 = Constants.classify_2;
	ArrayList<CategoryInfo> classify1List = new ArrayList<CategoryInfo>();
	private ListView listView;
	private TranslateAnimation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify_manager);
		listView = (ListView) findViewById(R.id.listView1);
		readDb();
		listView.setAdapter(new MyAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(animation!=null){
					animation.cancel();
					animation=null;
					Toast.makeText(CategoryManagerActivity.this, "取消动画", 0).show();
				}else{
					Toast.makeText(CategoryManagerActivity.this, "单击事件", 0).show();
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(CategoryManagerActivity.this, "长按事件", 0).show();
				initScrollAnimation();
				view.startAnimation(animation);
				return true;
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Toast.makeText(CategoryManagerActivity.this, "touchEvent", 0).show();
		Log.e("touch", event.getAction()+"");
		return true;
	}
	private void initScrollAnimation(){
		if(animation==null){
			animation = new TranslateAnimation(0, -MyUtil.dip2px(this, 80), 0, 0);
			animation.setDuration(500);
			animation.setFillAfter(true);
		}
	}
	class MyAdapter extends AbstractAdapter{
		
		@Override
		public int getCount() {
			return classify1List.size();
		}
		@Override
		public CategoryInfo getItem(int position) {
			return classify1List.get(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(R.layout.item_wheelview, null);
			ImageView imageView = (ImageView) layout.findViewById(R.id.imageView1);
			TextView textView = (TextView) layout.findViewById(R.id.textView1);
			CategoryInfo info = classify1List.get(position);
			textView.setText(info.getName());
			imageView.setImageResource(getResources().getIdentifier(info.get_tempIconName(), "drawable", getPackageName()));
			return layout;
		}}
	private void readDb() {
		HashMap<Integer,CategoryInfo> categoryMap = CategoryInfo.getCategoryMap(this);
		Iterator iter = categoryMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer,CategoryInfo> entry= (Entry<Integer, CategoryInfo>) iter.next();
			CategoryInfo val = entry.getValue();
			if (val.getParentCategoryPOID()==-1) {
				classify1List.add(val);
			}
//			Log.e("CategoryInfo", val.toString());
		}
		Collections.sort(classify1List);
	}
//	private void readDb() {
//		SQLiteDatabase db = DataBaseUtil.getDb();
//		Cursor query = db.query("t_category", null, "parentCategoryPOID=?", new String[]{"-1"}, null, null, null);
//		boolean toFirst = query.moveToFirst();
//		while(toFirst){
//			String name = query.getString(query.getColumnIndex("name"));
//			String _tempIconName = query.getString(query.getColumnIndex("_tempIconName"));
//			int categoryPOID = query.getInt(query.getColumnIndex("categoryPOID"));
//			int parentCategoryPOID = query.getInt(query.getColumnIndex("parentCategoryPOID"));
//			int ordered = query.getInt(query.getColumnIndex("ordered"));
//			classify1List.add(new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered));
//			toFirst  = query.moveToNext();
//		}
//		Collections.sort(classify1List);
//		Log.e("classlist", classify1List.toString());
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.classified_manager, menu);
		return true;
	}

}
