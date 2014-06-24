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
public class MemberFragment extends BaseFragment {
	private int whitchTv;
	private WheelView wheelView1;
	private int wheel1item;
	private String[] members = new String[]{"无成员","本人","老公","老婆","子女","父母","家庭公用"};
	public MemberFragment() {
		// Required empty public constructor
	}

	public MemberFragment(int whitchTv) {
		this.whitchTv = whitchTv;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_members, container, false);
		wheelView1 = (WheelView) layout.findViewById(R.id.wheelView1);
			
		wheelView1.setVisibleItems(3);
		wheelView1.setViewAdapter(new MyAdapter(getActivity(),R.layout.item_wheelview_simpletext,0,members));
		
		wheelView1.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				wheel1item = newValue;
				setText();
			}
		});
		//虽然destroy了,fragment实例还在
		wheelView1.setCurrentItem(wheel1item);
		setText();
		return layout;
	}
	//更新activity的textview内容
	private void setText(){
		FragmentActivity activity = getActivity();
		if(activity instanceof BaseFragmentActivity){
			((BaseFragmentActivity)activity).setText(whitchTv, members[wheel1item]);
			((BaseFragmentActivity)activity).setMember(wheel1item);
		}
	};
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
		// TODO Auto-generated method stub
		
	}
	
	//CP TODO 耗时操作
}
