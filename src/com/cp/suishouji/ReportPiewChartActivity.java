package com.cp.suishouji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.cp.piechart.ChartProp;
import com.cp.piechart.ChartPropChangeListener;
import com.cp.piechart.MyButton;
import com.cp.piechart.MyButtonClickListener;
import com.cp.piechart.PieView;
import com.cp.suishouji.dao.CategoryInfo;
import com.cp.suishouji.dao.ExpenseInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengSherlockActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 饼图报表
 * @author cp
 *
 */
public class ReportPiewChartActivity extends UmengSherlockActivity implements OnClickListener{

	private TextView tv_title;
	//piechart
	private PieView pieView;
	private MyButton myButton;
	private TextView textView;
	private TextView textView2;
	
	//数据
	private ArrayList<ExpenseInfo> expenseList;//主页查询传来的消费记录
	private HashMap<Integer, ArrayList<CategoryInfo>> childlistMap; //
	private ArrayList<CategoryInfo> fatherList;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_piechart);
		initActionbar();
		//读取数据
		Intent data = getIntent();
		expenseList = data.getParcelableArrayListExtra("expenseList");
		if(expenseList!=null){
			Collections.sort(expenseList);//排序,提高效率
		}else{
			Toast.makeText(this, "请先记账", Toast.LENGTH_SHORT).show();
		}
		initData();
		initView();
	}
	  @Override
	public void onResume() {
		super.onResume();
		pieView.rotateEnable();
	}

	public void onPause() {
	        super.onPause();
	      
	        if(pieView != null){
	        	pieView.rotateDisable();
	        }
	    }
	protected void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_report, null);
		
		inflate.findViewById(R.id.title).setOnClickListener(this);
		tv_title = (TextView) inflate.findViewById(R.id.tv_title);
		tv_title.setText("分类支出");
		actionBar.setCustomView(inflate,layout);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title:
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * Description:初始化界面元素
	 * 
	 */
	
	public void initView() {

		pieView = (PieView) this.findViewById(R.id.lotteryView);
		myButton=(MyButton)this.findViewById(R.id.MyBt);
        textView=(TextView)this.findViewById(R.id.MyTV);
        textView2=(TextView)this.findViewById(R.id.MyTV2);
        MyButtonClickListener listener = new MyButtonClickListener() {
			
			@Override
			public void onclick() {
				ChartProp currentChartProp = pieView.getCurrentChartProp();
				if(currentChartProp==null){
					Toast.makeText(ReportPiewChartActivity.this, "暂无数据,请先记账!", 0).show();
					return;
				}
				int categoryPOID = currentChartProp.getCategoryPOID();
				ArrayList<CategoryInfo> childList = childlistMap.get(categoryPOID);//去掉headview
				Intent intent = new Intent(ReportPiewChartActivity.this,ReportPieSecActivity.class);
				Log.e("categoryPOID", categoryPOID+"");
//				Log.e("childList", childList.toString());
				if(childList.get(0).getCategoryPOID()!=categoryPOID){
					for (int i = 0; i < fatherList.size(); i++) {
						if(fatherList.get(i).getCategoryPOID() == categoryPOID){
							childList.add(0, fatherList.get(i));
							break;
						}
					}
				}
				intent.putParcelableArrayListExtra("categoryList", childList);
				startActivityForResult(intent, 0);//TODO 如果更改了预算,回来时要更新				
			}
		};
		myButton.setMyButtonClickListener(listener );
 
        
		initItem();
		 
		
		Message msg = new Message();
		msg.obj = pieView.getCurrentChartProp();
		handler.sendMessage(msg);
		
         
	 
		//textView=(TextView) this.findViewById(R.id.textView);
		pieView.setChartPropChangeListener(new ChartPropChangeListener() {
			
			@Override
			public void getChartProp(ChartProp chartProp) {
				Message msg = new Message();
				msg.obj = chartProp;
				handler.sendMessage(msg);
				
			}
		});
	
	
		 pieView.start();

	}
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ChartProp chartProp=(ChartProp) msg.obj;
			if(chartProp == null){
				return;
			}
		 myButton.setBackgroundPaintColor(chartProp.getColor());
		 textView.setText(chartProp.getName());
		 textView.setTextColor(chartProp.getColor());
		 textView2.setText(chartProp.getMsecName());
			 
		};
	};
	/**
	 * 
	 * Description:初始化转盘的颜色，文字
	 * 
	 */
	public void initItem() {
//		Log.e("fatherList.toString()", fatherList.toString());
		if(fatherList ==null || fatherList.size() ==0){
			return ;
		}
		double totalmoney = 0;
		ArrayList<CategoryInfo> arrayList = new ArrayList<CategoryInfo>();
		//取出非0值
		for (int i = 0; i < fatherList.size(); i++) {
			CategoryInfo categoryInfo = fatherList.get(i);
			if (categoryInfo.expense >0) {
				totalmoney +=categoryInfo.expense;
				arrayList.add(categoryInfo);
			}
		}
//		Log.e("fatherList.toString()", fatherList.toString());
		double[] expenses = new double[arrayList.size()];
		float[] percents = new float[arrayList.size()];
		for (int i = 0; i < expenses.length; i++) {
			expenses[i] = arrayList.get(i).expense;
		}
		percents = MyUtil.getPercent(expenses);
//		for (int i = 0; i < percents.length; i++) {
//			Log.e("percents[i]", percents[i]+"");
//		}
		int color[] = new int[] { getResources().getColor(R.color.chartcolor1),getResources().getColor(R.color.chartcolor2),
				getResources().getColor(R.color.chartcolor3),getResources().getColor(R.color.chartcolor4),
				getResources().getColor(R.color.chartcolor5),getResources().getColor(R.color.chartcolor6),
				getResources().getColor(R.color.chartcolor7),getResources().getColor(R.color.chartcolor8),
				getResources().getColor(R.color.chartcolor9),getResources().getColor(R.color.chartcolor10),
				getResources().getColor(R.color.chartcolor11),getResources().getColor(R.color.chartcolor12),
				getResources().getColor(R.color.chartcolor13),getResources().getColor(R.color.chartcolor14),
				getResources().getColor(R.color.chartcolor15)};//颜色
//CP
		ArrayList<ChartProp> acps = pieView.createCharts(arrayList.size());
		int size = acps.size();
		for (int i = 0; i < size; i++) {
		 ChartProp chartProp = acps.get(i);
			chartProp.setColor(color[i%color.length]);
			chartProp.setPercent(percents[i]);
			chartProp.setName(arrayList.get(i).getName());
			chartProp.setCategoryPOID(arrayList.get(i).getCategoryPOID());
			chartProp.setMsecName(MyUtil.double2(percents[i]*100)+"%   "+"¥ "+MyUtil.doubleFormate(arrayList.get(i).expense));
		}
		 pieView.setMoney(totalmoney);
		 pieView.initPercents() ;
	}
	
	private void initData(){
		/**
		 * 与expenseList结合,计算出所有分类花费;
		 */
		SQLiteDatabase db = DataBaseUtil.getDb();
		//TODO 读取一级分类信息,二级分类
		Cursor query_category = db.query("t_category", null, "type=?",
				new String[]{String.valueOf(0)}, null, null, "categoryPOID");//消费类
		boolean toLast = query_category.moveToLast();
		ArrayList<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();
		fatherList = new ArrayList<CategoryInfo>();
		ArrayList<CategoryInfo> childList = new ArrayList<CategoryInfo>();
		childlistMap = new HashMap<Integer, ArrayList<CategoryInfo>>();//二级分类列表图
		while(toLast){
			String name = query_category.getString(query_category.getColumnIndex("name"));
			String _tempIconName = query_category.getString(query_category.getColumnIndex("_tempIconName"));
			int categoryPOID = query_category.getInt(query_category.getColumnIndex("categoryPOID"));
			int parentCategoryPOID = query_category.getInt(query_category.getColumnIndex("parentCategoryPOID"));
			int ordered = query_category.getInt(query_category.getColumnIndex("ordered"));
			CategoryInfo info = new CategoryInfo(name, _tempIconName, categoryPOID, parentCategoryPOID, ordered);
			categoryList.add(info);
			if(parentCategoryPOID == -1 ){
				fatherList.add(info);
			}else{
				childList.add(info);
			}
			toLast = query_category.moveToPrevious();
		}

		/**
		 * 加入消费信息
		 */
		if(expenseList!=null){
			int count_expense = 0;
			int count_budget = 0;
			for (int i = 0; i < categoryList.size(); i++) {
				CategoryInfo categoryInfo = categoryList.get(i);
				for (int j = count_expense; j < expenseList.size(); j++) {
					if(expenseList.get(j).sellerCategoryPOID == categoryInfo.getCategoryPOID()){
						count_expense ++;
						categoryInfo.expense +=expenseList.get(j).buyermoney;//汇总花费
					}
				}
			}
		}

//		Log.e("expenseList.toString()", expenseList.toString());
//		Log.e("childList.toString()", childList.toString());
//		Log.e("categoryList", categoryList.toString());
		/**
		 * 二级分类花费汇总到一级类,并加入map
		 */
		Collections.sort(fatherList, new Comparator<CategoryInfo>() {

			@Override
			public int compare(CategoryInfo lhs, CategoryInfo rhs) {
				return rhs.getCategoryPOID() - lhs.getCategoryPOID();
			}
		} );
		Collections.sort(childList, new Comparator<CategoryInfo>() {
			
			@Override
			public int compare(CategoryInfo lhs, CategoryInfo rhs) {
				return rhs.getParentCategoryPOID() - lhs.getParentCategoryPOID();
			}
		} );
		int count_child = 0;
		for (int i = 0; i < fatherList.size(); i++) {
			CategoryInfo infoFather = fatherList.get(i);
			CategoryInfo infoChild;
			ArrayList<CategoryInfo> infoList = new ArrayList<CategoryInfo>();
			for (int j = count_child; j < childList.size(); j++) {
				infoChild = childList.get(j);
				if(infoFather.getCategoryPOID() == infoChild.getParentCategoryPOID()){
					count_child ++;
					infoFather.expense += infoChild.expense;
					infoList.add(infoChild);
				}
			}
			childlistMap.put(infoFather.getCategoryPOID(), infoList);
		}
	}
	
}
