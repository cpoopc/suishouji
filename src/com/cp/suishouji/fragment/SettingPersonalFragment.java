package com.cp.suishouji.fragment;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.R.layout;
import com.cp.suishouji.adapter.SettingAdapter;
import com.cp.suishouji.dao.SettingItemInfo;

import android.content.SharedPreferences;
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
public class SettingPersonalFragment extends Fragment {

	private ListView mListView;
	private ArrayList<SettingItemInfo> infoList;

	public SettingPersonalFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_common_use, container,false);
		mListView = (ListView) layout.findViewById(R.id.listView1);
		infoList = new ArrayList<SettingItemInfo>();
		SharedPreferences sp = getActivity().getSharedPreferences("infos", 0);
		infoList.add(new SettingItemInfo("默认显示与多人记账"));
		infoList.add(new SettingItemInfo(R.drawable.accountbook_shortcut_car,"账本设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_account_share,"多人记账"));
		infoList.add(new SettingItemInfo(R.drawable.icon_custom_toolbar,"定制导航"));
		infoList.add(new SettingItemInfo(R.drawable.icon_trans_default,"定制记一笔"));
		infoList.add(new SettingItemInfo(R.drawable.icon_loan_setting,"借贷/报销设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_report,"图表设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_create_shortcut,"发射当前账本到桌面"));
		infoList.add(new SettingItemInfo("提醒与本位币"));
		infoList.add(new SettingItemInfo(R.drawable.icon_create_shortcut,"提醒设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_exchange,"本位币/汇率"));
		infoList.add(new SettingItemInfo("记账增强"));
		infoList.add(new SettingItemInfo(R.drawable.icon_exchange,"附近商家"));
		infoList.add(new SettingItemInfo(R.drawable.icon_corp_project,"商家项目选择设置"));
		boolean voice_check = sp.getBoolean("voice_check", true);
		boolean transtime_check = sp.getBoolean("transtime_check", false);
		boolean wochacha_check = sp.getBoolean("wochacha_check", true);
		boolean showfeed_check = sp.getBoolean("showfeed_check", true);
		infoList.add(new SettingItemInfo(R.drawable.icon_voice_memo,"启用语音备注", voice_check));
		infoList.add(new SettingItemInfo(R.drawable.icon_trans_time,"启用账单时刻",transtime_check));
		infoList.add(new SettingItemInfo(R.drawable.icon_wochacha,"启用扫描记账",wochacha_check));
		infoList.add(new SettingItemInfo(R.drawable.icon_show_feed,"首屏显示近期动态",showfeed_check));
		infoList.add(new SettingItemInfo(R.drawable.accountbook_shortcut_car,"账本动态"));
		infoList.add(new SettingItemInfo("统计时间起始"));
		infoList.add(new SettingItemInfo(R.drawable.icon_week_start,"周开始于"));
		infoList.add(new SettingItemInfo(R.drawable.icon_month_start,"月开始于"));
		infoList.add(new SettingItemInfo("账单图片"));
		infoList.add(new SettingItemInfo(R.drawable.icon_image_quality,"图片质量设置"));
		infoList.add(new SettingItemInfo(R.drawable.icon_mobile_upload_image,"移动网络下同步图片"));
		
		mListView.setAdapter(new SettingAdapter(getActivity(), infoList, 4));
		return layout;
	}

}
