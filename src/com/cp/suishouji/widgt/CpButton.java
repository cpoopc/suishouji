package com.cp.suishouji.widgt;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;

public class CpButton extends Button {

	private Paint paint1;
	private Paint paint2;
	private Bitmap bm_normal;
	private Bitmap bm_selector;
	private int imgres_normal;
	private int imgres_pressed;
	private String str = "cp";
	private int textsize;
	public CpButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CpButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Mybutton);
		imgres_normal = ta.getResourceId(R.styleable.Mybutton_img_normal, com.cp.suishouji.R.drawable.go_add_income_btn_normal);
		imgres_pressed = ta.getResourceId(R.styleable.Mybutton_img_pressed, com.cp.suishouji.R.drawable.go_add_income_btn_selected);
		str = ta.getString(R.styleable.Mybutton_string);
		ta.recycle();
		init(context);
	}

	public CpButton(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		textsize = (int) MyUtil.dip2px(getContext(), 16);
		paint1 = new Paint();
		paint1.setTextSize(textsize);
		paint1.setColor(Color.WHITE);
		paint1.setAntiAlias(true);
		paint2 = new Paint();
		paint2.setTextSize(textsize);
		paint2.setColor(getResources().getColor(R.color.oringe_string));
		paint2.setAntiAlias(true);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		boolean selected = false;
		boolean press = false;
		Resources resources = getResources();
		int height = (int) MyUtil.dip2px(getContext(), 23);
		if(bm_normal==null){
			bm_normal = BitmapFactory.decodeResource(resources, imgres_normal);
			bm_selector = BitmapFactory.decodeResource(resources, imgres_pressed);
			bm_normal = Bitmap.createScaledBitmap(bm_normal, height, height, true);
			bm_selector = Bitmap.createScaledBitmap(bm_selector, height, height, true);
		}
		int[] state = getBackground().getState();
//		if(bm_selector!=null){
//			height = bm_selector.getHeight();
//		}
		for (int i = 0; i < state.length; i++) {
//			Log.e("getBackground().getState();", state[i]+"");
			if(state[i]==android.R.attr.state_pressed){
				press = true;
//				Log.e("getBackground().getState();", "state_pressed");
			}else if(state[i]==android.R.attr.state_selected){
//				Log.e("getBackground().getState();", "state_selected");
				selected = true;
			}
			
		}
		if(press){
			canvas.drawBitmap(bm_selector, (getWidth()-height)/2, (float) ((getHeight()-1.4*height)/2), null);
		}else{
			canvas.drawBitmap(bm_normal, (getWidth()-height)/2, (float) ((getHeight()-1.4*height)/2), null);
		}
		if(selected){
			canvas.drawBitmap(bm_selector, (getWidth()-height)/2, (float) ((getHeight()-1.4*height)/2), null);
//			paint2.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText(str, ((float)getWidth())/2, ((float) getHeight())*5/6, paint2);
			canvas.drawText(str, (getWidth()-2*textsize)/2, (float) ((getHeight()+0.6*height)/2+textsize), paint2);
		}else{
//			canvas.drawBitmap(bm_normal, (getWidth()-height)/2, (getHeight()-height)/2, null);
			if(str!=null){
//				paint1.setTextAlign(Paint.Align.CENTER);
//				canvas.drawText(str, ((float)getWidth())/2, ((float) getHeight())*5/6, paint1);
				canvas.drawText(str, (getWidth()-2*textsize)/2, (float) ((getHeight()+0.6*height)/2+textsize), paint1);
			}
		}
	}
}
