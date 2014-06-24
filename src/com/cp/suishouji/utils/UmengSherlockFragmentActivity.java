package com.cp.suishouji.utils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public abstract class UmengSherlockFragmentActivity extends SherlockFragmentActivity{
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
