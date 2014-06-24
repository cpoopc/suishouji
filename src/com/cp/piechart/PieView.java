package com.cp.piechart;

import java.util.ArrayList;

import com.cp.suishouji.R;
import com.cp.suishouji.utils.MyUtil;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

import android.graphics.Point;
import android.graphics.RectF;

import android.graphics.Paint;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.view.View;

public class PieView extends View implements View.OnTouchListener, Runnable {

	private float m_x = 0;
	private float m_y = 0;

	private static ArrayList<ChartProp> mChartProps;
	private float screenHight, screenWidth;// 屏幕的宽和高

	private float radius;// 绘制圆的半径
	private float startAngle;// 开始角度
	private float sweepAngle = 0.0f; // 扫过的角度
	private int itemCount;// 选项个数
	private float startSpeed = 0;

	private boolean firstEnter = true; // 程序是否已经退出
	private boolean rotateEnabled = false;
	private boolean tounched = false;
	private boolean done = false;
	private Paint mPaint;

	private Paint textPaint;
	private Canvas mCanvas;

	private ChartPropChangeListener listener;
	private static ChartProp mChartProp;
	private static ChartProp tChartProp;
	private static float[] percents;

	private Point centerPoint;
	private Bitmap myBg;

	private Thread myThread;
	//CP
	private String money = "¥ 0.00";
	private Paint smalltextPaint;
	private boolean accelerated;
	private boolean needRefresh;
	private float speed;
	private double shift;
	private float sign;
	private float start_x;
	private float start_y;
	public void setMoney(double money) {
		this.money = "¥ "+MyUtil.doubleFormate(money);
		invalidate();
	}
	public void setBackgroundPaintColor(int color) {

		invalidate();
	}

	private void writeLog(String string) {
//		Log.e("caizh", string);
	}

	public void destory() {
		done = true;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		
		if (firstEnter) {
			firstInit();
		} else {
			if (!tounched) {
				//当前扇形中心角度
				float middleAngle = ((mChartProp.getEndAngle() + mChartProp
						.getStartAngle()) / 2);
				//与90+360n的夹角,调整角度
				float distance = (middleAngle - getBorderAngle());

				startAngle -= (distance / 2);
			}

		}

		mCanvas = canvas;

		float f1 = centerPoint.x;
		float f2 = centerPoint.y;
		// 填充背景色
		/*
		 * mCanvas.drawColor(0xff639EC3); mCanvas.save();
		 */

		// *********************************确定参考区域*************************************
		float f3 = f1 - radius;// X轴 - 左
		float f4 = f2 - radius; // Y轴 - 上
		float f5 = f1 + radius; // X轴 - 右
		float f6 = f2 + radius; // Y轴 - 下
		RectF rectF = new RectF(f3, f4, f5, f6);

		// *********************************画每个区域的颜色块*********************************
		drawItem(rectF);
		// *********************************画边上渐变的圆环出来*******************************
		drawableCenterCircle(f1, f2);
		// 解锁Canvas，并渲染当前图像
		
		//当当前选中的chartprop改变时,回调
		if (mChartProp!=null &&!mChartProp.equals(tChartProp)) {
			
			listener.getChartProp(mChartProp);
			tChartProp = mChartProp;
		}
	}

	public PieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PieView(Context context) {
		super(context);
		init();
	}

	private void init() {
		needRefresh = true;
		myThread = new Thread(this);
		// 创建一个新的SurfaceHolder， 并分配这个类作为它的回调(callback)

		mChartProps = new ArrayList<ChartProp>();

		// 图像画笔
		mPaint = new Paint();

		mPaint.setAntiAlias(true);
		// 大文字画笔
		textPaint = new Paint();
		textPaint.setTextSize(28);
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
		// 小文字画笔
		smalltextPaint = new Paint();
		smalltextPaint.setTextSize(22);
		smalltextPaint.setColor(Color.WHITE);
		smalltextPaint.setAntiAlias(true);

		// 半径

		startAngle = 90f;

		myBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.mask_piechartformymoney);

		this.setOnTouchListener(this);

	}

	public void start() {
		myThread.start();
	}
	@Override
	protected void onDetachedFromWindow() {
		done = true;
		super.onDetachedFromWindow();
	}
	//CP important!pieview实现runnable接口,
	@Override
	public void run() {

		while (!done) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (rotateEnabled && needRefresh) {

				postInvalidate();
			}
		}

	}

	private Bitmap createBitmap(Bitmap b, float x, float y) {
		int w = b.getWidth();
		int h = b.getHeight();

		float sx = (float) x / w;
		float sy = (float) y / h;
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

	/**
	 * create charts' property 创建饼状图的属性
	 * 
	 * @param chartsNum
	 *            charts' number 饼状图的个数
	 * @return charts' property's list 饼状图属性的list
	 */
	public ArrayList<ChartProp> createCharts(int itemCount) {
		this.itemCount = itemCount;
		percents = new float[itemCount];
		mChartProps.clear();
		createChartProp(itemCount);

		return mChartProps;
	}

	// 重新初始化
	public void reInit() {
		startAngle = 90f;
		firstEnter = true; // 程序是否已经退出
	}

	// 第一次参数进行改变后的初始化调用
	public void initPercents() {

		for (int i = 0; i < itemCount; i++) {

			percents[i] = mChartProps.get(i).getPercent();
			mChartProps.get(i).setPercent(0.01f);

		}

	}

	public void setChartPropChangeListener(ChartPropChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * actually create chartProp objects. 真正创建扇形属性的方法
	 * 
	 * @param chartsNum
	 *            charts' number 饼状图的个数
	 */
	private void createChartProp(int chartsNum) {
		for (int i = 0; i < chartsNum; i++) {
			ChartProp chartProp = new ChartProp(this);
			chartProp.setId(i);
			mChartProps.add(chartProp);
		}
	}

	private void initResoure() {
		float y = myBg.getHeight();
		float x = myBg.getWidth();
		float r;

		if (screenHight > screenWidth) {
			r = screenWidth;
			myBg = createBitmap(myBg, screenWidth, (screenWidth * y) / x);
		} else {
			r = screenHight;
			myBg = createBitmap(myBg, screenHight, (screenHight * y) / x);
		}

		centerPoint = new Point();

		centerPoint.x = (int) (r / 2);
		centerPoint.y = (int) ((r * 19) / 32);

		radius = (centerPoint.x * 0.8843f);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 高度
		screenHight =  (float) PiewController.measureWidth(heightMeasureSpec);
		

		screenWidth = (float) PiewController.measureHeight(widthMeasureSpec);
//		Log.e("caizh", "screenWidth01="+screenWidth);
		initResoure();
		setMeasuredDimension((int) screenWidth, (int) screenHight);
	}

//	private int measureWidth(int pWidthMeasureSpec) {
//		int result = 0;
//		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
//		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸
//
//		switch (widthMode) {
//		/**
//		 * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
//		 * MeasureSpec.AT_MOST。
//		 * 
//		 * 
//		 * MeasureSpec.EXACTLY是精确尺寸，
//		 * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
//		 * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
//		 * 
//		 * 
//		 * MeasureSpec.AT_MOST是最大尺寸，
//		 * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
//		 * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
//		 * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
//		 * 
//		 * 
//		 * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
//		 * 通过measure方法传入的模式。
//		 */
//		case MeasureSpec.AT_MOST:
//		case MeasureSpec.EXACTLY:
//			result = widthSize;
//			break;
//		}
//		return result;
//	}
//
//	private int measureHeight(int pHeightMeasureSpec) {
//		int result = 0;
//
//		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
//		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
//
//		switch (heightMode) {
//		case MeasureSpec.AT_MOST:
//		case MeasureSpec.EXACTLY:
//			result = heightSize;
//			break;
//		}
//		return result;
//	}

	//变大效果是
	private void firstInit() {
		startSpeed += 0.05f;
		if (startSpeed >= 1) {
			startSpeed = 0.9f;
		}
		float total = 0f;

		for (int i = 0; i < itemCount; i++) {
			float percent = mChartProps.get(i).getPercent();
			// writeLog("percent="+percent);
			float distancePercent = (percents[i] - percent);

			if (distancePercent < 0.0001) {
				mChartProps.get(i).setPercent(percents[i]);

			} else {

				mChartProps.get(i).setPercent(
						percent + (distancePercent * startSpeed));
			}

			total = total + mChartProps.get(i).getPercent();
		}
		if (total >= 1) {
			firstEnter = false;
		}

	}
//CP 如果90度线在当前块中,当前块就是
	public void getCurrentChartProp(ChartProp chartProp, float f) {

		if ((chartProp.getStartAngle() <= f) && (chartProp.getEndAngle() > f)) {

			mChartProp = chartProp;

		}
	}

	public void drawableCenterCircle(float x, float y) {
		/*
		 * Paint centerCirclePaint = new Paint();
		 * centerCirclePaint.setColor(Color.BLACK);
		 * 
		 * centerCirclePaint.setAlpha(150); mCanvas.drawCircle(x, y, radius / 3,
		 * centerCirclePaint);
		 */

		// Paint localPaint = new Paint();
		// 设置取消锯齿效果
		// localPaint.setAntiAlias(true);
		// 风格为圆环
		/*
		 * localPaint.setStyle(Paint.Style.STROKE); // 圆环宽度
		 * localPaint.setStrokeWidth(circleRadius);
		 */
		// 圆环背景

		mCanvas.drawBitmap(myBg, 0, 0, null);
		mCanvas.save();

		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(x/10);
		
		smalltextPaint.setColor(Color.WHITE);
		smalltextPaint.setTextAlign(Paint.Align.CENTER);
		smalltextPaint.setTextSize(x/15);
		//可设置显示文字
		mCanvas.drawText("总计:", x, y-x/30, textPaint);
//		mCanvas.drawText("总计", x, y+x/10, textPaint);
		mCanvas.drawText(money, x, y+x/10, smalltextPaint);
		mCanvas.save();
	}

	private float getBorderAngle() {
		float borderAngle;
		int circleCount = 0;
		if (startAngle >= 0) {
			circleCount = (int) (startAngle / 360);
			if ((startAngle % 360) > (90)) {
//				borderAngle = (float) (90 + 360 * (circleCount ));
				borderAngle = (float) (90 + 360 * (circleCount + 1));
			} else {
				borderAngle = (float) (90 + 360 * circleCount);
			}

		} else {
			circleCount = (int) (startAngle / 360);
			if ((startAngle % 360) < (-270)) {
				borderAngle = (float) (90 + 360 * (circleCount - 1));
			} else {
				borderAngle = (float) (90 + 360 * circleCount);
			}

		}

		return borderAngle;

	}
//初始化时要获得当前块时用
	public ChartProp getCurrentChartProp() {
		if (mChartProp != null) {
			return mChartProp;
		}
		float temp = startAngle;
		float borderAngle = getBorderAngle();

		for (int i = 0; i < itemCount; i++) {

			sweepAngle = mChartProps.get(i).getSweepAngle();
			mChartProps.get(i).setStartAngle(temp);

			temp += sweepAngle;
			mChartProps.get(i).setEndAngle(temp);

			getCurrentChartProp(mChartProps.get(i), borderAngle);
		}

		return mChartProp;
	}

	// *********************************画每个扇形*********************************
	public void drawItem(RectF localRectf) {
		float temp = startAngle;
		float borderAngle = getBorderAngle();

		for (int i = 0; i < itemCount; i++) {
			mPaint.setColor(mChartProps.get(i).getColor());
			// startAngle为每次移动的角度大小
			sweepAngle = mChartProps.get(i).getSweepAngle();
			mChartProps.get(i).setStartAngle(temp);
//			Log.e("temp", "temp:"+temp+",;mChartProps.get(i).getName()"+mChartProps.get(i).getName());
			/*
			 * oval：圆弧所在的椭圆对象。 startAngle：圆弧的起始角度。sweepAngle：圆弧的角度。
			 * useCenter：是否显示半径连线，true表示显示圆弧与圆心的半径连线，false表示不显示。 paint：绘制时所使用的画笔
			 */
			mCanvas.drawArc(localRectf, temp, sweepAngle, true, mPaint);
			mCanvas.save();
			/*
			 * drawText(localRectf, temp, sweepAngle, mChartProps.get(i)
			 * .getName());
			 */
			temp += sweepAngle;
			mChartProps.get(i).setEndAngle(temp);

			getCurrentChartProp(mChartProps.get(i), borderAngle);
		}
	}

	public boolean isRotateEnabled() {
		return rotateEnabled;
	}

	public void setRotateEnabled(boolean rotateEnabled) {
		this.rotateEnabled = rotateEnabled;
	}

	public void rotateEnable() {
		rotateEnabled = true;
	}

	public void rotateDisable() {
		rotateEnabled = false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		needRefresh = true;
		boolean enable = true;
		float x = centerPoint.x;
		float y = centerPoint.y;
		float y1 = event.getY();
		float x1 = event.getX();

		float x2 = v.getLeft();
		float y2 = v.getTop();

		float x3 = x1 - x2;
		float y3 = y1 - y2;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.e("ACTION_DOWN", "ACTION_DOWN");
			float d = (float) Math.sqrt((Math.pow(Math.abs((x - x3)), 2) + Math
					.pow(Math.abs((y - y3)), 2)));
			if (d < radius) {
				m_x = x3;
				m_y = y3;
				start_x = x3;
				start_y = y3;
				tounched = true;
			}

		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
//			float d = (float) Math.sqrt((Math.pow(Math.abs((x - x3)), 2) + Math
//					.pow(Math.abs((y - y3)), 2)));
//			if (d > radius) {
//				m_x = 0;
//				m_y = 0;
//				enable = false;
//				tounched = false;
//				return true;
//			}
			float d2 = (float) Math.sqrt((Math.pow(Math.abs((start_x - x3)), 2) + Math
					.pow(Math.abs((start_y - y3)), 2)));
			if(d2<4){
				//点击事件
				float angle = getAngle(start_x-centerPoint.x, start_y-centerPoint.y);
//				Log.e("点", "点:angle:"+angle);
				for (int i = 0; i < mChartProps.size(); i++) {
					ChartProp prop = mChartProps.get(i);
//					Log.e("prop.getStartAngle()", ":"+prop.getStartAngle()%360+"--"+prop.getEndAngle());
					if(prop.getStartAngle()%360>prop.getEndAngle()%360){
						if(angle>prop.getStartAngle()%360 || angle<prop.getEndAngle()%360){
							startAngle += (-prop.getStartAngle() - prop.getEndAngle()
									+ mChartProp.getStartAngle() + mChartProp.getEndAngle())/2;
							if(startAngle<0){
								startAngle +=360;
							}
							mChartProp = prop;
							mChartProp.setStartAngle(getBorderAngle()-mChartProp.getSweepAngle()/2);
							mChartProp.setEndAngle(getBorderAngle()+mChartProp.getSweepAngle()/2);
//							Log.e("就是他", "呵呵");
							break;
						}
					}else if(angle>prop.getStartAngle()%360 && angle<prop.getEndAngle()%360){
//						Log.e("就是他", "呵呵");

						startAngle += (-prop.getStartAngle() - prop.getEndAngle()
								+ mChartProp.getStartAngle() + mChartProp.getEndAngle())/2;
						if(startAngle<0){
							startAngle +=360;
						}
						mChartProp = prop;

						mChartProp.setStartAngle(getBorderAngle()-mChartProp.getSweepAngle()/2);
						mChartProp.setEndAngle(getBorderAngle()+mChartProp.getSweepAngle()/2);
						break;
					}
				}
//				Log.e("startAngle", ""+startAngle);
//				Log.e("mChartProp.startAngle", ""+mChartProp.getStartAngle());
				start_x = 0;
				start_y = 0;
				tounched = false;
				invalidate();
				return true;
			}
			start_x = 0;
			start_y = 0;
			m_x = 0;
			m_y = 0;
			if(accelerated){
				needRefresh = false;
				Log.e("up", "启动cp加速引擎!");
				if(speed<0){
					speed = -speed;
					sign = -1;
				}else{
					sign = 1;
				}
				shift = 0;
				if(speed>50){
					speed = 50;
				}
				final double s = 50*speed;
				this.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(accelerated && shift<s && speed>0){
							if(speed>5){
								shift +=  5;
								speed -= speed/5;
								startAngle += shift*sign;
								invalidate();
								Log.e("speed", ""+speed);
								postDelayed(this, (long) (200f/speed));
							}else{
								shift +=speed;
								speed -= 1;
								startAngle += shift*sign;
								invalidate();
								Log.e("speed", ""+speed);
								postDelayed(this, 100);
							}
						}
						else{
							needRefresh = true;
							accelerated = false;
							tounched = false;
							Log.e("", "cp加速引擎停止");
						}
					}
				}, 50);
			}else{
				tounched = false;
			}

		}


			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (enable && tounched) {
				float ddd = (float) Math.sqrt((Math.pow(Math.abs((start_x - x3)), 2) + Math
						.pow(Math.abs((start_y - y3)), 2)));
				if(ddd<4){
					return true;
				}
				if ((m_x == 0) && (m_y == 0)) {

					enable = false;
				}
					float a = (float) Math
							.sqrt((Math.pow(Math.abs(m_x - x), 2) + Math.pow(
									Math.abs((m_y - y)), 2)));//上次按下点与中心点距离
					float b = (float) Math
						.sqrt((Math.pow(Math.abs(x3 - x), 2) + Math.pow(
								Math.abs((y3 - y)), 2)));//滑动点与中心点距离
					float c = (float) Math
							.sqrt((Math.pow(Math.abs((m_x - x3)), 2) + Math.pow(
									Math.abs((m_y - y3)), 2)));//与上次滑动距离

					float cosc = (float) (Math.pow(a, 2) + Math.pow(b, 2) - Math
							.pow(c, 2)) / (2 * a * b);//滑动角度

					float m_angle = getAngle((m_x - x), (m_y - y));//起点与中心点角度,水平夹角,下方为正

					float angle3 = getAngle((x3 - x), (y3 - y));//滑动点与中心点角度
					if ((90 > m_angle) && (angle3 > 270)) {
						m_angle = m_angle + (float) 360;
					}
					if ((90 > angle3) && (m_angle > 270)) {
						angle3 = angle3 + (float) 360;
					}
					if (angle3 - m_angle > 0) {
						if(angle3 - m_angle > 7){
//							Log.e("加速滚动", "启动加速滚动"); CP
							speed = angle3 - m_angle;
							accelerated = true;
						}else{
							accelerated = false;
						}
						startAngle = (float) (startAngle + Math.acos(cosc) * 180
								/ Math.PI);
					} else {
						if(angle3 - m_angle < -7){
//							Log.e("加速滚动", "启动加速滚动");
							speed = angle3 - m_angle;
							accelerated = true;
						}else{
							accelerated = false;
						}
						startAngle = (float) (startAngle - Math.acos(cosc) * 180
								/ Math.PI);
					}
					m_x = x3;
					m_y = y3;
				}
			}

		return true;
	}

	private float getAngle(float x, float y) {

		if ((x == 0) && (y == 0)) {
			return 0;
		}

		float angle = (float) (Math.atan(y / x) * 180 / Math.PI);

		if (x == 0) {
			if (y > 0) {
				return 90;
			} else {
				return 270;
			}
		}

		if (x > 0) {
			if (y < 0) {
				return (float) 360 + angle;
			}
		}
		if (x < 0) {
			if (y > 0) {
				return (float) 180 + angle;
			} else if (y == 0) {
				return 180;
			} else if (y < 0) {
				return (float) 180 + angle;
			}

		}

		return angle;

	}

}
