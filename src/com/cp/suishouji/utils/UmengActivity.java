package com.cp.suishouji.utils;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public abstract class UmengActivity extends Activity{
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
