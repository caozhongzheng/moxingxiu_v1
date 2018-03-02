package com.moinapp.wuliao.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> fragmentList;

	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragmentList = fragments;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}
}