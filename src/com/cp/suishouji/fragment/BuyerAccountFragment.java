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

import com.cp.suishouji.AccountDetailActivity;
import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.BaseFragmentActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.AccountInfo;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;

/**
 *  账户
 * t_account
 * t_account_group
 */
public class BuyerAccountFragment extends BaseFragment {
	private WheelView wheelView2;
	private boolean hadRead;
	private int whitchTv;
	private WheelView wheelView1;
	private int wheel1item;
	private int wheel2item;
	private int TYPE;
	private ArrayList<AccountInfo> accountList;
	private ArrayList<String> accountNames1;
	private ArrayList<ArrayList<String>> accountNames2;
	private ArrayList<ArrayList<String>> balance;
	private ArrayList<ArrayList<Long>> accountIds;
	public BuyerAccountFragment() {
		// Required empty public constructor
	}

	public BuyerAccountFragment(int whitchTv,int type) {
		this.whitchTv = whitchTv;
		this.TYPE = type;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(!hadRead){
			hadRead = true;
			readDb();
		}
		//accountList已经排序好了
		accountList = AccountDetailActivity.readDb();
		accountNames1 = new ArrayList<String>();
		accountNames2 = new ArrayList<ArrayList<String>>();
		balance = new ArrayList<ArrayList<String>>();
		accountIds = new ArrayList<ArrayList<Long>>();
		int count = 0;
		int temp_count = 0;
		for (int i = 0; i < accountList.size(); i++) {
			AccountInfo temp = accountList.get(i);
			if(temp.parentAccountGroupPOID == 1){
				//主分组
				accountNames1.add(temp.name);
				count ++;
			}else{
				//子分组
				if(count>temp_count){
					temp_count ++;
					accountNames2.add(new ArrayList<String>());
					balance.add(new ArrayList<String>());
					accountIds.add(new ArrayList<Long>());
				}
				accountNames2.get(count-1).add(temp.name + "(CNY)");
				accountIds.get(count-1).add(temp.accountPOID);
				if(temp.type == 0){
					balance.get(count-1).add("￥ "+MyUtil.doubleFormate(temp.balance));
				}else if(temp.type == 1){
					balance.get(count-1).add("￥ "+MyUtil.doubleFormate(temp.amountOfLiability));
				}else{
					balance.get(count-1).add("￥ "+MyUtil.doubleFormate(temp.amountOfCredit));
				}
			}
		}
		Log.e("accountNames1", accountNames1.toString());
		Log.e("accountNames2", accountNames2.toString());
		Log.e("accountIds", accountIds.toString());
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_category, container, false);
		wheelView1 = (WheelView) layout.findViewById(R.id.wheelView1);
		wheelView2 = (WheelView) layout.findViewById(R.id.wheelView2);
			
		wheelView1.setVisibleItems(3);
		wheelView1.setViewAdapter(new MyAdapter(getActivity(),R.layout.item_wheelview_simpletext,0,accountNames1));
		wheelView2.setVisibleItems(5);
		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview_account, 0, 
				accountNames2.get(wheelView1.getCurrentItem()),balance.get(wheelView1.getCurrentItem())));
		
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
			wheel2item %=accountNames2.get(wheel1item).size();
			wheelView2.setCurrentItem(wheel2item);
			((BaseFragmentActivity)activity).setText(whitchTv,accountNames2.get(wheel1item).get(wheel2item));
			((BaseFragmentActivity)activity).setAccount(accountIds.get(wheel1item).get(wheel2item));
		}
		wheelView2.setViewAdapter(new MyAdapter(getActivity(), R.layout.item_wheelview_account, 0, 
				accountNames2.get(wheelView1.getCurrentItem()),balance.get(wheelView1.getCurrentItem())));
	};
	private void change(int value){
	}
	class MyAdapter extends AbstractWheelTextAdapter{
//		private String[] info;
		private ArrayList<String> info;
		private ArrayList<String> money;
		private String packagename;
		private Resources res;
		protected MyAdapter(Context context, int itemResource,
				int itemTextResource,ArrayList<String> info) {
			super(context, itemResource , NO_RESOURCE);
			this.info  = info;
			this.packagename = context.getPackageName();
			res = getResources();
		}
		protected MyAdapter(Context context, int itemResource,
				int itemTextResource,ArrayList<String> info,ArrayList<String> money) {
			super(context, itemResource , NO_RESOURCE);
			this.info  = info;
			this.money  = money;
			this.packagename = context.getPackageName();
			res = getResources();
		}

		@Override
		public int getItemsCount() {
			// TODO Auto-generated method stub
			return info.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "hehe"+index;
		}
		@Override
		public View getItem(int index, View convertView, ViewGroup parent) {
			View view = super.getItem(index, convertView, parent);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			textView.setText(info.get(index));
			if(money!=null){
				TextView tv_money = (TextView) view.findViewById(R.id.textView2);
				tv_money.setText(money.get(index));
			}
			return view;
		}
	}
	@Override
	public void btnok() {
		
	}
	//CP TODO 耗时操作
	private void readDb() {
		
	}
}
