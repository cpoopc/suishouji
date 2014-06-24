package com.cp.suishouji.widgt;

import com.cp.suishouji.R;
import com.cp.suishouji.utils.bvn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class MainToolButton extends Button
{
  private Drawable drawable;
  private int b;
  private int c;
  private int d;
  private int e;

  public MainToolButton(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }

  public MainToolButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }

  public MainToolButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    this.c = bvn.a(paramContext, 29.0F);//宽 29dp?
    this.d = this.c;//图标的宽,高?
    this.e = bvn.a(paramContext, 5.0F);//
  }

  public void setImageDrawable(int rsId)
  {
	  setImageDrawable(rsId, this.e);
  }

  protected void setImageDrawable(int rsId, int paramInt2)
  {
    if ((rsId != 0) && (rsId == this.b))
      return;
    this.b = rsId;
    if (rsId != 0);
    for (Drawable localDrawable = getResources().getDrawable(rsId); ; localDrawable = null)
    {
      a(localDrawable, paramInt2);
      return;
    }
  }

  public void a(Drawable paramDrawable)
  {
    a(paramDrawable, this.e);
  }

  protected void a(Drawable paramDrawable, int paramInt)
  {
    this.drawable = paramDrawable;
    this.e = paramInt;
    invalidate();
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int n;
    int i1;
    if (this.drawable != null)
    {
      int i = (getWidth() - this.c) / 2;//CP 中心点?
      int j = this.e;
      int k = i + this.c;
      int m = j + this.d;
      this.drawable.setBounds(i, j, k, m);
      //Parameters:
//      left 
//      top 
//      right 
//      bottom
      n = getScrollX();
      i1 = getScrollY();
      if ((n | i1) == 0)
        this.drawable.draw(paramCanvas);
    }
    else
    {
      return;
    }
    paramCanvas.translate(n, i1);
    this.drawable.draw(paramCanvas);
    paramCanvas.translate(-n, -i1);
  }
}

