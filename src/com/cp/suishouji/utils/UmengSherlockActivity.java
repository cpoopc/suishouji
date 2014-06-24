package com.cp.suishouji.utils;

import com.actionbarsherlock.app.SherlockActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public abstract class UmengSherlockActivity extends SherlockActivity{
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
