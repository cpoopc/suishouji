package com.cp.suishouji.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.BaseFragmentActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.utils.DataBaseUtil;

/**
 * 项目
 * 
 */
public class CategoryFragment extends BaseFragment {
	private WheelView wheelView2;
	private ArrayList<CategoryInfo> classify1List = new ArrayList<CategoryInfo>();
	private HashMap<Integer, ArrayList<CategoryInfo>> classify2Map = new HashMap<Integer, ArrayList<CategoryInfo>>();
	private boolean hadRead;
	private int whitchTv;
	private WheelView wheelView1;
	private int wheel1item;
	private int wheel2item;
	private int TYPE;
	public CategoryFragment() {
		// Required empty public constructor
	}

	public CategoryFragment(int whitchTv,int type) {
		this.whitchTv = whitchTv;
		this.TYPE = type;
	}
	
	public int getType() {
		return TYPE;
	}

	public void setType(int type) {
		TYPE = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if(!hadRead){
//			hadRead = true;
			readDb();
//		}
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_category, container, false);
		wheelView1 = (WheelView) layout.findViewById(R.id.wheelView1);
		wheelView2 = (WheelView) layout.findViewById(R.id.wheelView2);
			
		wheelView1.setVisibleItems(3);
		wheelView1.setViewAdapter(new MyAdapter(getActivity(),R.layout.item_wheelview,0,classify1List));
		wheelView2.setVisibleItems(5);
		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview, 0, classify2Map.get(classify1List.get(0).getCategoryPOID())));
		
		wheelView1.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				change(newValue);
				wheel1item = newValue;
				setText();
			}
		});
		wheelView2.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel2item = newValue;
				setText();
			}
		});
		//虽然destroy了,fragment实例还在
		wheelView1.setCurrentItem(wheel1item);
		wheelView2.setCurrentItem(wheel2item);
		setText();
		return layout;
	}
	//更新activity的textview内容
	private void setText(){
		FragmentActivity activity = getActivity();
		if(activity instanceof BaseFragmentActivity){
			CategoryInfo classify1Info = classify1List.get(wheelView1.getCurrentItem());
			ArrayList<CategoryInfo> list = classify2Map.get(classify1Info.getCategoryPOID());
			if(wheelView2.getCurrentItem()>=list.size()){
				return;
			}
			CategoryInfo classify2Info = list.get(wheelView2.getCurrentItem());
			String str1 = classify1Info.getName();
			String str2 = classify2Info.getName();
			int categoryPOID = classify2Info.getCategoryPOID();
			Log.e("categoryPOID", categoryPOID+"");
			((BaseFragmentActivity)activity).setText(whitchTv, str1 + ">" + str2,categoryPOID);
		}
	};
	private void change(int value){
//		Log.e("classify2Map.get(classify1List.get(0).getCategoryPOID())", classify2Map.get(classify1List.get(value).getCategoryPOID()).toString());
		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview, 0, classify2Map.get(classify1List.get(value).getCategoryPOID())));
	}
	class MyAdapter extends AbstractWheelTextAdapter{
		private ArrayList<CategoryInfo> classifyList;
		private String packagename;
		private Resources res;
		protected MyAdapter(Context context, int itemResource,
				int itemTextResource,ArrayList<CategoryInfo> classifyList) {
			super(context, itemResource , NO_RESOURCE);
			this.classifyList = classifyList;
			this.packagename = context.getPackageName();
			res = getResources();
		}

		@Override
		public int getItemsCount() {
			// TODO Auto-generated method stub
			return classifyList.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "hehe"+index;
		}
		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
			ImageView img = (ImageView) view.findViewById(R.id.imageView1);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			CategoryInfo info = classifyList.get(index);
			textView.setText(info.getName());
			img.setImageResource(res.getIdentifier(info.get_tempIconName(), "drawable", packagename));
			return view;
		}
	}
	@Override
	public void btnok() {
		// TODO Auto-generated method stub
		
	}
	public void changeType(int type){
		TYPE = type;
		wheel1item = 0;
		wheel2item = 0;//清除记录
		readDb();
	}
	
	//CP TODO 耗时操作
	private void readDb() {
		SQLiteDatabase db = DataBaseUtil.getDb();
		Cursor query = null;
		classify1List.clear();
		classify2Map.clear();
		switch (TYPE) {
		case 0:
			query = db.query("t_category", null, "parentCategoryPOID=?", new String[]{"-1"}, null, null, null);//支出项目
			
			break;
		case 1:
			query = db.query("t_category", null, "parentCategoryPOID=?", new String[]{"-56"}, null, null, null);//收入项目
			
			break;

		default:
			query = db.query("t_category", null, "parentCategoryPOID=?", new String[]{"-1"}, null, null, null);//CP 暂时支出项目
			break;
		}
		boolean toFirst = query.moveToFirst();
		while(toFirst){
			String name = query.getString(query.getColumnIndex("name"));
			String _tempIconName = query.getString(query.getColumnIndex("_tempIconName"));
			int categoryPOID = query.getInt(query.getColumnIndex("categoryPOID"));
			int parentCategoryPOID = query.getInt(query.getColumnIndex("parentCategoryPOID"));
			int ordered = query.getInt(query.getColumnIndex("ordered"));
			classify1List.add(new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered));
			toFirst  = query.moveToNext();
		}
			Collections.sort(classify1List);//排序
			for (int i = 0; i < classify1List.size(); i++) {
				ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
				CategoryInfo parent = classify1List.get(i);
				Cursor query2 = db.query("t_category", null, "parentCategoryPOID=?", new String[]{String.valueOf(parent.getCategoryPOID())}, null, null, null);
				boolean toFirst2 = query2.moveToFirst();
				while(toFirst2){
					String name = query2.getString(query.getColumnIndex("name"));
					String _tempIconName = query2.getString(query.getColumnIndex("_tempIconName"));
					int categoryPOID = query2.getInt(query.getColumnIndex("categoryPOID"));
					int parentCategoryPOID = query2.getInt(query.getColumnIndex("parentCategoryPOID"));
					int ordered = query2.getInt(query2.getColumnIndex("ordered"));
					list.add(new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered));
					toFirst2  = query2.moveToNext();
				}
//				Log.e("list.toString();", list.toString());
				classify2Map.put(parent.getCategoryPOID(), list);//添加二级分类
			}
	}
}
