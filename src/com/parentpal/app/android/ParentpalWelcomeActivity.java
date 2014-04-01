/**
 * 
 */
package com.parentpal.app.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.parentpal.adapter.ParentPalViewPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Jan 12, 2014 3:44:31 PM
 *
 */
public class ParentpalWelcomeActivity extends ParentpalBaseActivity implements OnClickListener {
	
	private ViewPager welcomePager;
	private ParentPalViewPagerAdapter pagerAdapter;
	private TextView lblLoginRegistration;
	
	/* (non-Javadoc)
	 * @see com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_parentpal_welcomescreen);
		welcomePager = (ViewPager) findViewById(R.id.viewpagerWelcome);
		
		lblLoginRegistration = (TextView) findViewById(R.id.lblLoginRegistration);
		lblLoginRegistration.setOnClickListener(this);
		
		pagerAdapter = new ParentPalViewPagerAdapter(ParentpalWelcomeActivity.this);
		
		welcomePager.setAdapter(pagerAdapter);
		
		CirclePageIndicator mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mCirclePageIndicator.setViewPager(welcomePager);
		
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.lblLoginRegistration:

			startActivity(new Intent(mContext, ParentpalActivityLogin.class));
			finish();

			break;

		default:
			break;
		}
	}

}
