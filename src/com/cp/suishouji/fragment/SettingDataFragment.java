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
public class SettingDataFragment extends Fragment {

	private ListView mListView;
	private ArrayList<SettingItemInfo> infoList;

	public SettingDataFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_common_use, container,false);
		mListView = (ListView) layout.findViewById(R.id.listView1);
		infoList = new ArrayList<SettingItemInfo>();
		infoList.add(new SettingItemInfo("常用数据处理"));
		infoList.add(new SettingItemInfo(R.drawable.icon_security,"密码保护"));
		infoList.add(new SettingItemInfo(R.drawable.icon_bat_delete,"账单批量删除"));
		infoList.add(new SettingItemInfo(R.drawable.icon_import_account_book,"导入其他账本账单"));
		infoList.add(new SettingItemInfo(R.drawable.icon_backup,"备份恢复"));
		infoList.add(new SettingItemInfo(R.drawable.cardniu_duplicate,"卡牛重复账单清理"));
		infoList.add(new SettingItemInfo("兼容性处理"));
		infoList.add(new SettingItemInfo(R.drawable.icon_project_to_member,"项目转成员"));
		infoList.add(new SettingItemInfo(R.drawable.icon_merge_accout,"账户合并"));
		infoList.add(new SettingItemInfo(R.drawable.icon_migrate_out,"迁移未结清借贷账单"));
		infoList.add(new SettingItemInfo(R.drawable.icon_migrate_in,"从借贷中心迁出账单"));
		infoList.add(new SettingItemInfo(R.drawable.icon_import_category,"导入场景账本分类"));
		infoList.add(new SettingItemInfo("数据同步设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_auto_sync,"自动同步"));
		infoList.add(new SettingItemInfo(R.drawable.icon_sync_only_wifi,"仅在WIFI下自动同步"));
		
		mListView.setAdapter(new SettingAdapter(getActivity(), infoList, 2));
		return layout;
	}

}
