package com.cp.suishouji.widgt;


import com.cp.suishouji.R;
import com.cp.suishouji.utils.MyUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
/**
 * 传入预算值,预算余额
 * 动画
 * @author cp
 *
 */
public class BudgetBattery extends View{
	Drawable battery;
	Drawable cover;
	Drawable battery_low;
	int total;
	int x;
	int whitch;		//
	double percent_start = 0;//起始百分比
	boolean animaend;//动画结束
	boolean cancel;
	private Resources res;
	Context context;
	CpButtonClickListener listener;//点击监听
	public BudgetBattery(Context context) {
		super(context);
		init(context);
	}

	public BudgetBattery(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public BudgetBattery(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public void setMyTouchListener(CpButtonClickListener listener){
		this.listener = listener;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			cancel = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if(event.getX()<0 ||event.getX() > getLayoutParams().width || event.getY()<0|| event.getY()> getLayoutParams().height)
			{
				cancel = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(listener!=null && !cancel){
				listener.onclick(this);
			}
			break;
		default:
			break;
		}
		return true;
//		return super.onTouchEvent(event);
	}
	
	//如果budget_ex<0;不显示;budget_ex/budget<0.1 剩余预算小于10%,显示红色(Batterylow);其余显示黄色
	//对比初始数据,如果小于初始数据,下降动画
	public void setBudgetData(double budget,double budget_ex){
		double percent = budget_ex/budget;
		if(percent_start == percent)return;
		if(budget_ex<0 || budget ==0){
			//没变化直接返回
//			return;
			total = 0; 
		}else{
			LayoutParams layoutParams = getLayoutParams();
//		Log.e("layoutParams.height", layoutParams.height+","+layoutParams.width);
			total = (int) (percent*(getLayoutParams().height - 12-14));
		}
		if(x<total){
			this.postDelayed(new Runnable() {
				
				
				@Override
				public void run() {
					if(x<total){
						x+=2;
						invalidate();
						postDelayed(this, 50);
					}else{
						animaend = true;
						invalidate();
					}
				}
			}, 50);
		}else{
			this.postDelayed(new Runnable() {
				
				
				@Override
				public void run() {
					if(x>total){
						x-=2;
						invalidate();
						postDelayed(this, 50);
					}else{
						animaend = true;
						invalidate();
					}
				}
			}, 50);
		}
		animaend = false;
		percent_start = percent;
		
	}
	private void init(Context context){
		this.context =context; 
		res = context.getResources();
		if(res!=null){
			battery = res.getDrawable(R.drawable.widget_battery);
			cover = res.getDrawable(R.drawable.widget_battery_cover);
			battery_low = res.getDrawable(R.drawable.widget_battery_low);
		}
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		LayoutParams layoutParams = getLayoutParams();
		if(battery!=null){
			battery.setBounds(12, layoutParams.height - x -12, layoutParams.width-12, layoutParams.height -12);
			battery.draw(canvas);
		}
		if(percent_start<=0.1 && animaend && battery_low!=null){
			battery_low.setBounds(12, layoutParams.height - x -12, layoutParams.width-12, layoutParams.height -12);
			battery_low.draw(canvas);
		}
		if(cover!=null){
			Rect bounds = new Rect(0, 0, layoutParams.width, layoutParams.height);
			cover.setBounds(bounds);
			cover.draw(canvas);
		}
		super.onDraw(canvas);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MyUtil.measureWidth(widthMeasureSpec);
		int measureHeight = MyUtil.measureHeight(heightMeasureSpec);
//		Log.e("measure结果", "measureWidth:"+measureWidth+",measureHeight:"+measureHeight);
		setMeasuredDimension(measureWidth, measureHeight);
	}
}
