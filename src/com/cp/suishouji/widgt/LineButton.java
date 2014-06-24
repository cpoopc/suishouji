package com.cp.suishouji.widgt;

import java.util.ArrayList;

import com.cp.suishouji.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.Button;

public class LineButton extends Button {

	private Paint paint1;
	public LineButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LineButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LineButton(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		paint1 = new Paint();
		paint1.setTextSize(30);
		paint1.setColor(getResources().getColor(R.color.red2));
		paint1.setAntiAlias(true);
		paint1.setTextAlign(Align.CENTER);
		paint1.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isChoise()){
			setTextColor(getResources().getColor(R.color.red2));
			canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint1);
		}else{
			setTextColor(getResources().getColor(R.color.brownddd));
		}
	}
	private boolean isChoised;
	public boolean isChoise(){
		
		return isChoised;
	}
	public void setChoise(boolean choise){
		isChoised = choise;
		invalidate();
	}
}
