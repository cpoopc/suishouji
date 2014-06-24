package com.cp.suishouji.fragment;


import java.util.ArrayList;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.adapter.AbstractAdapter;
import com.cp.suishouji.adapter.TemplateAdapter;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.dao.TemplateInfo;
import com.cp.suishouji.dao.TransactionInfo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TransactionSwitchFragment extends Fragment implements OnTabChangeListener, OnClickListener {
	private ArrayList<TemplateInfo> templateList;
	private TabHost tabHost;
	private int type;
	private TemplateAdapter myAdapter;
	public TransactionSwitchFragment(int type) {
		this.type = type;
	}
	class TabFact implements TabContentFactory {
		@Override
		public View createTabContent(String tag) {
			
			TextView tv = new TextView(getActivity());
			return tv;
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		Log.e("顶部悬浮页面", "onCreateView:");
		View layout = inflater.inflate(R.layout.fragment_transaction_switch,container, false);
		layout.findViewById(R.id.imageView3).setOnClickListener(this);
		initTabHost(inflater, layout);
		initListView(layout);
		return layout;
	}
	private void initListView(View layout) {
		ListView listView = (ListView) layout.findViewById(R.id.listView1);
		templateList = TemplateInfo.query(getActivity());
		if(templateList==null){
			listView.setAdapter(new MyAdapter());
		}else{
			myAdapter = new TemplateAdapter(getActivity(), templateList, 1);
			listView.setAdapter(myAdapter);
			listView.setOnItemClickListener(new TempleClick());
		}
	}
	class TempleClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(getActivity() instanceof AddOrEditTransActivity){
				((AddOrEditTransActivity)getActivity()).loadTemple(templateList.get(position));
			}
			
		}}
	class MyAdapter extends AbstractAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout;
				layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_listview_temple_empty, null);
			
			return layout;
			
		}}
	public void selectTab(int type){
		tabHost.setCurrentTab(type);
	}
	private void initTabHost(LayoutInflater inflater, View layout) {
		tabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
		tabHost.setup();
		TabWidget tabWidget = tabHost.getTabWidget();
		Resources res = getResources();
		View view1 = inflater.inflate(R.layout.item_scrolltab, null);
		View view2 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view2.findViewById(R.id.textView1)).setText("收入");
		((ImageView)view2.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_income_selector));
		View view3 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view3.findViewById(R.id.textView1)).setText("转账");
		((ImageView)view3.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_transfer_selector));
		View view4 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view4.findViewById(R.id.textView1)).setText("贷款");
		((ImageView)view4.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_browing_selector));
		View view5 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view5.findViewById(R.id.textView1)).setText("报销");
		((ImageView)view5.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_reimburse_selector));
		View view6 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view6.findViewById(R.id.textView1)).setText("代付");
		((ImageView)view6.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_paythe_selector));
		View view7 = inflater.inflate(R.layout.item_scrolltab, null);
		((TextView)view7.findViewById(R.id.textView1)).setText("退款");
		((ImageView)view7.findViewById(R.id.imageView1)).setImageDrawable(res.getDrawable(R.drawable.drop_menu_drawback_selector));
		tabHost.addTab(tabHost.newTabSpec("支出").setIndicator(view1)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("收入").setIndicator(view2)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("转账").setIndicator(view3)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("汇款").setIndicator(view4)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("报销").setIndicator(view5)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("代付").setIndicator(view6)
				.setContent(new TabFact()));
		tabHost.addTab(tabHost.newTabSpec("退款").setIndicator(view7)
				.setContent(new TabFact()));
		tabHost.setCurrentTab(type);
		tabHost.setOnTabChangedListener(this);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			View childTabViewAt = tabWidget.getChildTabViewAt(i);
			childTabViewAt.getLayoutParams().height = (int)getResources().getDimension(R.dimen.dropmenu);
			childTabViewAt.getLayoutParams().width = (int)getResources().getDimension(R.dimen.dropmenu);
		}
	}
	@Override
	public void onTabChanged(String tabId) {
		AddOrEditTransActivity activity;
		int type = 0;
		if(getActivity() instanceof AddOrEditTransActivity){
			activity = (AddOrEditTransActivity)getActivity();
			if("支出".equals(tabId)){//支出
				type = 0;
			}else if("收入".equals(tabId)){//收入
				type = 1;
			}else if("转账".equals(tabId)){//收入
				type = 2;
			}else if("汇款".equals(tabId)){//收入
				type = 3;
			}else if("报销".equals(tabId)){//收入
				type = 4;
			}else if("代付".equals(tabId)){//收入
				type = 5;
			}else if("退款".equals(tabId)){//收入
				type = 6;
			}
//			Log.e("顶部页面", "ontabchange");
//			activity.setType(type);
			if(type<2){
				activity.changeState(type,Constants.FRAMETOP_FGM);
				activity.setActionbarTitle(tabId);
			}
			activity.showTransactionSwitch();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView3:
			if(getActivity() instanceof AddOrEditTransActivity){
				((AddOrEditTransActivity)getActivity()).showTransactionSwitch();
			}
			
			break;

		default:
			break;
		}
	}

}
