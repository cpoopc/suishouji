package com.cp.suishouji.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
//px,dp转换
public class bvn
{
  private static DisplayMetrics displayMetrics;

  private static float a(Context paramContext)
  {
	  getDisplayMetrics(paramContext);
    return displayMetrics.widthPixels / 320.0F;
  }

  public static int a(Context paramContext, float paramFloat)
  {
    if ((paramFloat == 0.0F) || (paramFloat == -2.0F) || (paramFloat == -1.0F) || (paramFloat == -1.0F))
      return (int)paramFloat;
    float f = paramFloat * a(paramContext);
    if (f <= 1.0F)
      return 1;
    return (int)f;
  }

  public static int a(Context paramContext, int paramInt)
  {
    if ((paramInt == 0) || (paramInt == -2) || (paramInt == -1) || (paramInt == -1))
      return paramInt;
    getDisplayMetrics(paramContext);
    return a(paramContext, paramInt / displayMetrics.density);
  }

  private static void getDisplayMetrics(Context paramContext)
  {
    if (displayMetrics == null)
    	displayMetrics = paramContext.getResources().getDisplayMetrics();//获取屏幕参数
  }
}
