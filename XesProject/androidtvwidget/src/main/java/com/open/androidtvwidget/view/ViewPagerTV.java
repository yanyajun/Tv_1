package com.open.androidtvwidget.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class ViewPagerTV extends ViewPager {

	public ViewPagerTV(Context context) {
		super(context);
	}
	
	public ViewPagerTV(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean executeKeyEvent(KeyEvent event) {
		return false;
	}
}
