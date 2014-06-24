package com.cp.suishouji.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.content.Context;
import android.content.SharedPreferences;
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
 *  账户
 * t_account
 * t_account_group
 */
public class LocationFragment extends BaseFragment {
	private WheelView wheelView2;
	private int whitchTv;
	private WheelView wheelView1;
	private int wheel1item;
	private int wheel2item;
	private String[] w1 = new String[]{"最近使用","所有"};
	private String[][] w2 = new String[][]{
			new String[]{"无商家/地点"},
			new String[]{"无商家/地点","其它","饭堂","银行","商场","超市","公交"}
		};
	private int itemID;
	HashMap<String, Integer> map = new HashMap<String, Integer>();
	
//	String[] w1 = new String[]{"现金","银行卡","公交卡",};
	public LocationFragment() {
		// Required empty public constructor
	}

	public LocationFragment(int whitchTv) {
		this.whitchTv = whitchTv;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sp = getActivity().getSharedPreferences("infos", 0);
		itemID = sp.getInt("location", 1);
		ArrayList<String> str = new ArrayList<String>();
		for (int i = 0; i < w2[1].length; i++) {
			map.put(w2[1][i], i);
			if((itemID>>i&1)==1){
				str.add(w2[1][i]);
				}
		}
		String[] array = (String[]) str.toArray(new String[str.size()]);
		w2[0] = array;
		Log.e("array", array.toString());
//		w2[1] = 
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_category, container, false);
		wheelView1 = (WheelView) layout.findViewById(R.id.wheelView1);
		wheelView2 = (WheelView) layout.findViewById(R.id.wheelView2);
			
		wheelView1.setVisibleItems(3);
		wheelView1.setViewAdapter(new MyAdapter(getActivity(),R.layout.item_wheelview_simpletext,0,w1));
		wheelView2.setVisibleItems(5);
		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview_simpletext, 0, w2[wheelView1.getCurrentItem()]));
		
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
//			if(wheel2item>=w2[wheel1item].length){
				wheel2item %=w2[wheel1item].length;
//			} 
			wheelView2.setCurrentItem(wheel2item);
			((BaseFragmentActivity)activity).setText(whitchTv, w2[wheel1item][wheel2item]);
			wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview_simpletext, 0, w2[wheelView1.getCurrentItem()]));
			((BaseFragmentActivity)activity).setLocation(map.get(w2[wheel1item][wheel2item]));
		}
	};
	private void change(int value){
//		Log.e("classify2Map.get(classify1List.get(0).getCategoryPOID())", classify2Map.get(classify1List.get(value).getCategoryPOID()).toString());
//		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview, 0, classify2Map.get(classify1List.get(value).getCategoryPOID())));
	}
	class MyAdapter extends AbstractWheelTextAdapter{
		private String[] info;
		private String packagename;
		private Resources res;
		protected MyAdapter(Context context, int itemResource,
				int itemTextResource,String[] info) {
			super(context, itemResource , NO_RESOURCE);
			this.info  = info;
			this.packagename = context.getPackageName();
			res = getResources();
		}

		@Override
		public int getItemsCount() {
			// TODO Auto-generated method stub
			return info.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "hehe"+index;
		}
		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			textView.setText(info[index]);
			return view;
		}
	}
	@Override
	public void btnok() {
		if(wheel1item == 1){
			itemID = 1<<wheel2item;
			getActivity().getSharedPreferences("infos", 0).edit().putInt("location", itemID).commit();
		}
	}
	//CP TODO 耗时操作
	private void readDb() {
		
	}
}
