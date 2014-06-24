package com.cp.suishouji.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cp.suishouji.R;
import com.cp.suishouji.dao.TemplateInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TemplateAdapter extends AbstractAdapter{
	private Context context;
	private ArrayList<TemplateInfo> templateList;
	private HashMap<Integer, String> accountMap;
	private int mode;
	public TemplateAdapter(Context context, ArrayList<TemplateInfo> templateList,int mode) {
		super();
		this.context = context;
		this.templateList = templateList;
		this.mode = mode;
		accountMap = new HashMap<Integer, String>();
		Cursor queryAccount = DataBaseUtil.getDb().query("t_account", null, null, null, null, null, null);
		boolean toFirst = queryAccount.moveToFirst();
		while(toFirst){
			long accountPOID = queryAccount.getLong(queryAccount.getColumnIndex("accountPOID"));
			String accountName = queryAccount.getString(queryAccount
					.getColumnIndex("name"));
			accountMap.put((int)accountPOID, accountName+"(CNY)");
			toFirst = queryAccount.moveToNext();
		}
		queryAccount.close();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return templateList.size();
	}
	@Override
	public TemplateInfo getItem(int position) {
		return templateList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout;
		if(mode==0){
			layout = LayoutInflater.from(context).inflate(R.layout.item_listview_temple0, null);
		}else{
			layout = LayoutInflater.from(context).inflate(R.layout.item_listview_temple1, null);
		}
		TemplateInfo info = templateList.get(position);
		ImageView imageView1 = (ImageView) layout.findViewById(R.id.imageView1);
		TextView tv_templateName = (TextView) layout.findViewById(R.id.tv_templateName);
		TextView tv_type = (TextView) layout.findViewById(R.id.tv_type);
		TextView tv_category = (TextView) layout.findViewById(R.id.tv_category);
		TextView tv_account = (TextView) layout.findViewById(R.id.tv_account);
		TextView tv_money = (TextView) layout.findViewById(R.id.tv_money);
		imageView1.setImageResource(info.imgResId);
		tv_templateName.setText(info.templateName);
		tv_type.setText(info.type==0?"支出":"收入");
		tv_category.setText(info.name);
		tv_account.setText(accountMap.get(info.buyerAccountPOID));
//		Log.e("accountMap", "accountMap"+accountMap.get(-2));
		tv_money.setText(MyUtil.doubleFormate(info.buyerMoney));
		return layout;
	}

}
