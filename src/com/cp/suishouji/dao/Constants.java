package com.cp.suishouji.dao;

import com.cp.suishouji.R;

public class Constants {
	//请求码,结果码
	public static int PSNOTE =1;
	//
	public static int CALCULATE_FGM = 0;
	public static int CATEGORY_FGM = 1;
	public static int ACCOUNT_FGM = 2;
	public static int TRADETIME_FGM = 3;
	public static int BUYER_FGM = 4;
	public static int ITEM_FGM = 5;
	public static int LOCATION_FGM = 6;
	public static int PS_FGM = 7;
	public static int FRAMETOP_FGM = 8;
//	public static int NUMBER = 0;
	public static int[] classify_1 = new int[]{R.drawable.icon_jjwy,R.drawable.icon_jltx,R.drawable.icon_jrbx,//居家物业,交流通讯,金融保险
			R.drawable.icon_qtzx,R.drawable.icon_rqwl,//其他杂项,人情往来
			R.drawable.icon_spjs,R.drawable.icon_xcjt,R.drawable.icon_xxjx,R.drawable.icon_yfsp,//食品酒水,行车交通,学习进修,衣服饰品
			R.drawable.icon_xxyl,R.drawable.icon_ylbj//休闲娱乐,医疗保健
	};
//	,R.drawable.icon_zysr,R.drawable.icon_qtsr,R.drawable.icon_yhzc,R.drawable.icon_yyfy,R.drawable.icon_bbyp,//????
	public static int[][] classify_2 = new int[][]{
			new int[]{R.drawable.icon_jjwy_fz,R.drawable.icon_jjwy_rcyp,
					R.drawable.icon_jjwy_sdmq,R.drawable.icon_jjwy_swcc,R.drawable.icon_jjwy_wygl,R.drawable.icon_jjwy_yxby},//居家物业
			new int[]{R.drawable.icon_jltx_sjf,R.drawable.icon_jltx_swf,R.drawable.icon_jltx_yjf,R.drawable.icon_jltx_zjf},//交流通讯
			new int[]{R.drawable.icon_jrbx_ajhk,R.drawable.icon_jrbx_bxzc,R.drawable.icon_jrbx_pcfk,R.drawable.icon_jrbx_tzks,//
					R.drawable.icon_jrbx_xfss,R.drawable.icon_jrbx_yhsxf},//金融保险
			new int[]{R.drawable.icon_qtzx_bmf,R.drawable.icon_qtzx_lzss,R.drawable.icon_qtzx_qtzc,R.drawable.icon_qtzx_ywds},//其他杂项
			new int[]{R.drawable.icon_rqwl_csjz,R.drawable.icon_rqwl_hrqc,R.drawable.icon_rqwl_slqk,R.drawable.icon_rqwl_xjjz},//人情往来
			new int[]{R.drawable.icon_spjs_mc,R.drawable.icon_spjs_sgls,R.drawable.icon_spjs_wc,R.drawable.icon_spjs_yjc,
					R.drawable.icon_spjs_yl,R.drawable.icon_spjs_zc,R.drawable.icon_spjs_zwwc},//视频酒水
			new int[]{R.drawable.icon_xcjt_dczc,R.drawable.icon_xcjt_ggjt,R.drawable.icon_xcjt_jyf,
					R.drawable.icon_xcjt_sjcfy,R.drawable.icon_xcjt_tc},//行车交通
			new int[]{R.drawable.icon_xxjx_pxjx,R.drawable.icon_xxjx_sbzz,R.drawable.icon_xxjx_smzb},//学习进修
			new int[]{R.drawable.icon_yfsp_hzsp,R.drawable.icon_yfsp_xmbb,R.drawable.icon_yfsp_yfkz,},//,衣服饰品
			new int[]{R.drawable.icon_xxyl_cp,R.drawable.icon_xxyl_cwbb,R.drawable.icon_xxyl_fbjh,R.drawable.icon_xxyl_lydj,
					R.drawable.icon_xxyl_wg,R.drawable.icon_xxyl_xxwl,R.drawable.icon_xxyl_ydjs},//休闲娱乐
			new int[]{R.drawable.icon_ylbj_bjf,R.drawable.icon_ylbj_mrf,R.drawable.icon_ylbj_ypf,R.drawable.icon_ylbj_zlf}//医疗保健
			
	};
}
