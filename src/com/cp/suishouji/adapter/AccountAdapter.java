package com.cp.suishouji.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.AccountInfo;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountAdapter extends AbstractAdapter{
	private Context context;
	private ArrayList<AccountInfo> infoList;//排序后再传进来,parent后面跟着child
	private HashMap<Integer, String> nameMap;
	private int[] colors = new int[]{R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5,
			R.color.color6,R.color.color7,R.color.color8,R.color.color9,R.color.color10,
			R.color.color11,R.color.color12,R.color.color13};
	public AccountAdapter(Context context, ArrayList<AccountInfo> infoList) {
		super();
		this.context = context;
		this.infoList = infoList;
		//现金,银行卡,公交卡,饭卡,支付宝,信用卡,应付款项,应收款项,公司报销
//		nameMap.put(-2, value)
	}
	@Override
	public int getCount() {
		return infoList.size();
	}
	@Override
	public int getItemViewType(int position) {
		long parentAccountGroupPOID = getItem(position).parentAccountGroupPOID;
		if(parentAccountGroupPOID == 1){
			return 0;//parent
		}else{
			return 1;//child
		}
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public AccountInfo getItem(int position) {
		return infoList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout;
		AccountInfo info = getItem(position);
		if(getItemViewType(position)==0){
			//一级分类
			layout = LayoutInflater.from(context).inflate(R.layout.item_account_parent, null);
			TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
			TextView tv_money = (TextView) layout.findViewById(R.id.tv_money);
			tv_name.setText(info.name);
			if(info.type==0){
				tv_money.setText(MyUtil.doubleFormate(info.balance));
			}else if(info.type==1){
				tv_money.setText(MyUtil.doubleFormate(info.amountOfLiability));
			}else if(info.type ==2){
				tv_money.setText(MyUtil.doubleFormate(info.amountOfCredit));
			}
		}else{
			layout = LayoutInflater.from(context).inflate(R.layout.item_account_child, null);
			TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
			TextView tv_money = (TextView) layout.findViewById(R.id.tv_money);
			TextView tv_rmb = (TextView) layout.findViewById(R.id.tv_rmb);
			tv_name.setText(info.name);
			if(info.type==0){
				tv_money.setText(MyUtil.doubleFormate(info.balance));
			}else if(info.type==1){
				tv_money.setText(MyUtil.doubleFormate(info.amountOfLiability));
			}else if(info.type ==2){
				tv_money.setText(MyUtil.doubleFormate(info.amountOfCredit));
			}
//			tv_money.setText(MyUtil.doubleFormate(info.balance));
			if(info.secgroupname!=null){
				tv_rmb.setText("人民币|"+info.secgroupname);
			}
		}
		TextView leftline = (TextView) layout.findViewById(R.id.leftline);
		leftline.setBackgroundColor(context.getResources().getColor(colors[position%13]));
		return layout;
	}

}
