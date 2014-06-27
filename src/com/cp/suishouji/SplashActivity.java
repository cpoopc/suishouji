package com.cp.suishouji;

import java.io.IOException;

import com.cp.suishouji.dao.MapStatic;
import com.cp.suishouji.utils.DataBaseUtil;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	private long durationMillis = 2000;
	private boolean ready2Start = false;
	@Override
	protected void onResume() {
		MobclickAgent.onResume(this, MapStatic.YOU_MENG_APPK, MapStatic.ChannelId);
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1.0f);
		alphaAnimation.setDuration(durationMillis);
		ImageView img_logo = (ImageView) findViewById(R.id.img_logo);
		img_logo.startAnimation(alphaAnimation);
		img_logo.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(ready2Start){
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
					finish();
				}
				ready2Start = true;
			}
		}, durationMillis);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				copyDataBaseToPhone();
				//初始化数据库
				SharedPreferences sp = getSharedPreferences("infos", 0);
				long clientID = sp.getLong("clientID", 0);
				DataBaseUtil.initAccountBookDb(clientID);	
				if(ready2Start){
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
					finish();
				}
				ready2Start = true;
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	private void copyDataBaseToPhone() {
		DataBaseUtil util = new DataBaseUtil(this);
		// 判断数据库是否存在
		boolean dbExist = util.checkDataBase();

		if (dbExist) {
			Log.i("tag", "The database is exist.");
		} else {// 不存在就把raw里的数据库写入手机
			try {
				util.copyDataBase(R.raw.accountbook,"accountbook.db");//账户信息
				util.copyDataBase(R.raw.template,"0示例账本.db");//示例模板
				util.copyDataBase(R.raw.mymoney,"mymoney.db");//空模板
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

}
