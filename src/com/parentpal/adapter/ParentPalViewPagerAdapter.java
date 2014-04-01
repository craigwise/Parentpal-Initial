/**
 * 
 */
package com.parentpal.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.parentpal.app.android.R;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 * 
 *         Parentpal Jan 12, 2014 3:52:54 PM
 * 
 */
public class ParentPalViewPagerAdapter extends PagerAdapter {

	private int[] welcomeImageArray ;

	public ParentPalViewPagerAdapter(Context mContext) {

		welcomeImageArray = new int[] { R.drawable.splash_screen1,
				R.drawable.splash_screen2, R.drawable.splash_screen3,
				R.drawable.splash_screen4 };
		
	}

	public int getCount() {
		return welcomeImageArray.length;
	}

	public Object instantiateItem(View collection, final int position) {

		LayoutInflater inflater = (LayoutInflater) collection.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.viewpager_parentpal_welcome, null);			

		LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.viewPagerRootLayout);
		rootLayout.setBackgroundResource(welcomeImageArray[position]);
		
		((ViewPager) collection).addView(view);
		return view;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);

	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}
}
