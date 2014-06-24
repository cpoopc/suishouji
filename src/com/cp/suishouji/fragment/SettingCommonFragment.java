package com.cp.suishouji.fragment;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.R.layout;
import com.cp.suishouji.adapter.SettingAdapter;
import com.cp.suishouji.dao.SettingItemInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class SettingCommonFragment extends Fragment {

	private ListView mListView;
	private ArrayList<SettingItemInfo> infoList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_common_use, container, false);
		mListView = (ListView) layout.findViewById(R.id.listView1);
		infoList = new ArrayList<SettingItemInfo>();
		infoList.add(new SettingItemInfo("自动记账"));
		infoList.add(new SettingItemInfo(R.drawable.icon_sms,"短信自动记账(卡牛)"));
		infoList.add(new SettingItemInfo(R.drawable.icon_dkkj,"中心动卡空间自动记账"));
		infoList.add(new SettingItemInfo("分类,商家,项目与成员"));
		infoList.add(new SettingItemInfo(R.drawable.icon_category,"分类管理"));
		infoList.add(new SettingItemInfo(R.drawable.icon_corp_tag,"商家/项目/成员管理"));
		infoList.add(new SettingItemInfo("借贷与报销"));
		infoList.add(new SettingItemInfo(R.drawable.icon_creditor,"借贷中心"));
		infoList.add(new SettingItemInfo(R.drawable.icon_reimburse,"报销中心"));
		
		mListView.setAdapter(new SettingAdapter(getActivity(), infoList, 2));
		return layout;
	}

}
