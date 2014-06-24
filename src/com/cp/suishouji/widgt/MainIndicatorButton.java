package com.cp.suishouji.widgt;

import com.cp.suishouji.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class MainIndicatorButton extends Button{


	private Paint paint;
	private Drawable circle;
	private Drawable arraw;
	public MainIndicatorButton(Context context) {
		super(context);
		init(context);
	}


	public MainIndicatorButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MainIndicatorButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		paint = new Paint();
		circle = getResources().getDrawable(R.drawable.main_nav_circle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		circle.draw(canvas);
	}
}
