package com.example.homework9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;


public class Slidingtabs extends ActionBarActivity {

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	Intent intent;
	String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidingtabs);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Results from Zillow");
		
		intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.slidingtabs, menu);
	    return super.onCreateOptionsMenu(menu);
	    

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "BASIC INFO", "HISTORICAL ZESTIMATES" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
            case 0: return BasicFragment.newInstance(message);
            case 1: return ChartsFragment.newInstance(message); 
            default: return BasicFragment.newInstance(message);
			}
		}

	}

}