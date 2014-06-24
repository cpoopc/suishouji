package com.cp.suishouji.widgt;

import com.cp.suishouji.R;
import com.cp.suishouji.utils.bvn;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ScrollIndicatorButton extends Button
{
  private Drawable arrow;
  private Drawable circle;
  private Paint paintLine;
  private int strokeWidth;
  private int e;
  private float degrees;
  private boolean hasnew;
  private int width;
  private int height;
  private Paint paintCircle;//有新提示的时候右上角的小圆点
  private float circleR;
  private SlidingMenu slidingMenu;
  private boolean hasSet;
  public ScrollIndicatorButton(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public ScrollIndicatorButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  public ScrollIndicatorButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  private void init(Context paramContext)
  {
    Resources localResources = getResources();
    this.arrow = localResources.getDrawable(R.drawable.main_nav_circle);
    this.circle = localResources.getDrawable(R.drawable.main_nav_arrow);
    this.width = bvn.a(paramContext, 15.0F);
    this.height = bvn.a(paramContext, 13.0F);
    this.e = bvn.a(paramContext, 35.0F);
    this.strokeWidth = bvn.a(paramContext, 3.0F);
    this.degrees = 0.0F;
    this.paintLine = new Paint(1);
    this.paintLine.setStrokeWidth(this.strokeWidth);
//    this.paintLine.setColor(localResources.getColor(R.color.main_indicate));
    this.hasnew = false;
    this.paintCircle = new Paint(1);
    this.paintCircle.setStrokeWidth(bvn.a(paramContext, 3.0F));
    this.paintCircle.setColor(-182753);
    this.circleR = bvn.a(paramContext, 3.0F);
  }

  private Rect getRect(int paramInt)
  {
    int m = (getWidth() - paramInt) / 2;
    int n = (getHeight() - paramInt) / 2;
    return new Rect(m, n, m + paramInt, n + paramInt);
  }
  //旋转百分比(180度)
  public void rotatePercent(float paramFloat)
  {
    this.degrees = (180.0F * paramFloat);
    invalidate();
  }

  public void setLineColor(int paramInt)
  {
//    this.paintLine.setColor(getResources().getColor(paramInt));
    this.paintLine.setColor(paramInt);
    invalidate();
  }

  public void setHasNews(boolean paramBoolean)
  {
    this.hasnew = paramBoolean;
    invalidate();
  }
  public void setSlidding(SlidingMenu slidingMenu){
	  this.slidingMenu = slidingMenu;
  }
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int scrollX = getScrollX();
    int scrollY = getScrollY();
    float rotate_x = getWidth() / 2.0F;
    float rotate_y = getHeight() / 2.0F;
    float f3 = getHeight() - this.strokeWidth / 2.0F;
    this.arrow.setBounds(getRect(this.e));
    int i1 = (getWidth() - this.width) / 2;
    int i2 = (getHeight() - this.height) / 2;
    int i3 = i1 + this.width;
    int i4 = i2 + this.height;
    this.circle.setBounds(i1, i2, i3, i4);
    paramCanvas.save();
    paramCanvas.translate(scrollX, scrollY);
    this.arrow.draw(paramCanvas);
    paramCanvas.drawLine(0.0F, f3, getRight(), f3, this.paintLine);
    if(slidingMenu!=null){
    	if(!hasSet){
    		//设置slidingmenu的宽
    		hasSet = true;
    		slidingMenu.setBehindOffset(getRight());
    	}
    }
//    Log.e("getright", getRight()+"");
//    paramCanvas.drawLine(startX, startY, stopX, stopY, paint);
    if (this.hasnew)
    {
      Rect localRect = this.arrow.getBounds();
      paramCanvas.drawCircle(localRect.right - this.circleR, localRect.top + this.circleR, this.circleR, this.paintCircle);
    }
    paramCanvas.rotate(this.degrees, rotate_x, rotate_y);
    this.circle.draw(paramCanvas);
    paramCanvas.restore();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    System.out.println("mode:" + m + ",size:" + n);
  }
}
