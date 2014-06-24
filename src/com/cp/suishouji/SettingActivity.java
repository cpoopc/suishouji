package com.cp.suishouji;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cp.suishouji.adapter.PagerFragmentAdapter;
import com.cp.suishouji.fragment.SettingAboutFragment;
import com.cp.suishouji.fragment.SettingCommonFragment;
import com.cp.suishouji.fragment.SettingDataFragment;
import com.cp.suishouji.fragment.SettingPersonalFragment;
import com.cp.suishouji.utils.UmengSherlockFragmentActivity;
import com.cp.suishouji.widgt.LineButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class SettingActivity extends UmengSherlockFragmentActivity implements OnClickListener, OnPageChangeListener {

	private TextView tv_title;
	private ArrayList<LineButton> buttonList;
	private LineButtonClickListener buttonlistener;
	private FragmentManager fm;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initActionbar();
		initLineButton();
		initFragmentViewPager();
	}
	/**
	 * 添加相对应的按钮与fragment
	 * @param lineButtonId
	 * @param fragment
	 */
//	private void addFragmentPage(int lineButtonId,Fragment fragment){
//		findAddButton(lineButtonId);
//		fragmentList.add(fragment);
//		
//	}
	private void initFragmentViewPager() {
		fm = getSupportFragmentManager();
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new SettingCommonFragment());
		fragmentList.add(new SettingPersonalFragment());
		fragmentList.add(new SettingDataFragment());
		fragmentList.add(new SettingAboutFragment());
		mPager.setAdapter(new PagerFragmentAdapter(fm, fragmentList));
		mPager.setOnPageChangeListener(this);
		mPager.setOffscreenPageLimit(3);
	}
	private void initLineButton() {
		buttonList = new ArrayList<LineButton>();
		buttonlistener = new LineButtonClickListener();
		findAddButton(R.id.linebutton1);
		findAddButton(R.id.linebutton2);
		findAddButton(R.id.linebutton3);
		findAddButton(R.id.linebutton4);
		choiseButton(0);
	}
	private void findAddButton(int linebuttonid){
		LineButton lineButton = (LineButton) findViewById(linebuttonid);
		lineButton.setOnClickListener(buttonlistener);
		buttonList.add(lineButton);
	}
	private void choiseButton(int i){
		if(mPager!=null){
			if(mPager.getCurrentItem()!=i){
				mPager.setCurrentItem(i);
			}
		}
		for (int j = 0; j < buttonList.size(); j++) {
			LineButton lineButton = buttonList.get(j);
			if(i==j){
				lineButton.setChoise(true);
			}else{
				lineButton.setChoise(false);
			}
		}
		
	}
	class LineButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.linebutton1:
				choiseButton(0);
				break;
			case R.id.linebutton2:
				choiseButton(1);
				break;
			case R.id.linebutton3:
				choiseButton(2);
				break;
			case R.id.linebutton4:
				choiseButton(3);
				break;

			default:
				break;
			}
		}
	}
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_setting, null);
		inflate.findViewById(R.id.title).setOnClickListener(this);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		tv_title.setText("更多");
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
		choiseButton(arg0);
	}
}
