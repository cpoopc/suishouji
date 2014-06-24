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
public class SettingAboutFragment extends Fragment {

	private ListView mListView;
	private ArrayList<SettingItemInfo> infoList;

	public SettingAboutFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_common_use, container,false);
		mListView = (ListView) layout.findViewById(R.id.listView1);
		infoList = new ArrayList<SettingItemInfo>();
		infoList.add(new SettingItemInfo(R.drawable.share_with_friend,"分享给好友"));
		infoList.add(new SettingItemInfo(R.drawable.icon_feedback,"意见反馈"));
		infoList.add(new SettingItemInfo(R.drawable.icon_help,"入门帮助"));
		infoList.add(new SettingItemInfo(R.drawable.icon_bbs,"理财社区"));
		infoList.add(new SettingItemInfo(R.drawable.icon_app_recommend,"精彩应用推荐"));
		infoList.add(new SettingItemInfo(R.drawable.icon_about,"关于随手记"));
		
		mListView.setAdapter(new SettingAdapter(getActivity(), infoList, 1));
		return layout;
	}

}
