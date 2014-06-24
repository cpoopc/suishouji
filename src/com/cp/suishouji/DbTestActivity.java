package com.cp.suishouji;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * TODO
 * 数据库安全开启关闭测试,
 * 多个数据库测试
 * @author Administrator
 *
 */
public class DbTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.db_test, menu);
		return true;
	}

}
