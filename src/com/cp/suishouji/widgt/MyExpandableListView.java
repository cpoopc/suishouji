package com.cp.suishouji.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MyExpandableListView extends ExpandableListView {

	public MyExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyExpandableListView(Context context) {
		super(context);
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//	}

}
