package com.cp.suishouji.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerFragmentAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentList;
	public PagerFragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
