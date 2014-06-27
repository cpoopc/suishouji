package com.cp.suishouji;

import java.io.IOException;
import java.util.ArrayList;
import com.cp.suishouji.adapter.AccountBookGridAdapter;
import com.cp.suishouji.dao.AccountBookInfo;
import com.cp.suishouji.dao.ExpenseInfo;
import com.cp.suishouji.dao.PeriodInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.utils.UmengActivity;
import com.cp.suishouji.widgt.BudgetBattery;
import com.cp.suishouji.widgt.CpButtonClickListener;
import com.cp.suishouji.widgt.MainBottomItem;
import com.cp.suishouji.widgt.MainMiddleItem;
import com.cp.suishouji.widgt.MainToolButton;
import com.cp.suishouji.widgt.MainTopItem;
import com.cp.suishouji.widgt.ScrollIndicatorButton;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面:
 * 		收入,支出,预算信息;
 * 		记一笔,小助手;
 * 		本日,本周,本月信息;
 * 		底部tab
 * 侧滑界面:
 * 		账户选择
 * @author cp
 * 
 */
public class MainActivity extends UmengActivity implements OnClickListener {

	String TAG = "MainActivity";
	private int requestCode;
	private SlidingMenu slidingMenu;
	private View menuView;
	
	//账户
	private GridView mgridView;
	private ArrayList<AccountBookInfo> accountbookList;
	private AccountBookGridAdapter gridAdapter;
	private AccountBookInfo mCurInfo;//当前账户信息
//	private ImageView img_book_small;
	private ImageView img_edit;
	private View rl1;
	private View rl2;
	private TranslateAnimation animation;
	
	// 消费信息列表
	private ArrayList<ExpenseInfo> expenseList;
	
	//支出,收入,预算余额
	private TextView tv_money_expense;
	private TextView tv_money_income;
	private TextView tv_money_budget;
	
	//本日,本周,本月流水账
	private MainTopItem todayItem;
	private MainMiddleItem weekItem;
	private MainMiddleItem monthItem;
	private MainBottomItem lasterItem;
	
	//预算槽
	private BudgetBattery budgetBattery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initSlidingMenu();
		initMenuView();
		initUI();
		// 读取界面需要的数据
		// readDb();
		new Thread(new ReadMainDataThread()).start();
	}

	@Override
	protected void onDestroy() {
		//关闭数据库
		DataBaseUtil.closeDb();
		super.onDestroy();
	}
/**
 * 线程读取主界面信息
 * @author cp
 *
 */
	class ReadMainDataThread implements Runnable {

		private double costmoney;
		private double incomemoney;
		private double budget;
		private PeriodInfo todayInfo;
		private PeriodInfo weekInfo;
		private PeriodInfo monthInfo;
		private TransactionInfo lastinfo;

		/**
		 *
		 */
		@Override
		public void run() {

			SQLiteDatabase db = DataBaseUtil.getDb();
//			long clientID = getSharedPreferences("infos", 0).getLong(
//					"clientID", 0);
			Cursor queryo = db.query("t_transaction", null, "type=?",
					new String[] { "0" }, null, null, null);// 查询支出
			boolean toFirst = queryo.moveToFirst();
			costmoney = 0;
			expenseList = new ArrayList<ExpenseInfo>();
			while (toFirst) {
				expenseList.add(new ExpenseInfo(queryo.getLong(queryo
						.getColumnIndex("sellerCategoryPOID")), queryo
						.getDouble(queryo.getColumnIndex("buyerMoney"))));
				costmoney += queryo.getDouble(queryo
						.getColumnIndex("buyerMoney"));
				toFirst = queryo.moveToNext();
			}
			queryo.close();

			Cursor queryi = db.query("t_transaction", null, "type=?",
					new String[] { "1" }, null, null, null);// 查询收入
			boolean toFirst2 = queryi.moveToFirst();
			incomemoney = 0;
			while (toFirst2) {
				incomemoney += queryi.getDouble(queryi
						.getColumnIndex("buyerMoney"));
				toFirst2 = queryi.moveToNext();
			}
			queryi.close();

			// TODO查询预算
			Cursor query_budget = db.query("t_budget_item", null, null, null,
					null, null, null);
			boolean toFirst_budget = query_budget.moveToFirst();
			budget = 0;
			while (toFirst_budget) {
				budget += query_budget.getDouble(query_budget
						.getColumnIndex("amount"));
				toFirst_budget = query_budget.moveToNext();
			}
			query_budget.close();

			// 查询今天 select * from t_transaction where tradetime>今天的ms
			long todayMillis = MyUtil.getDayMillis(0);
			Cursor queryToday = db.query("t_transaction", null, "tradetime>?",
					new String[] { String.valueOf(todayMillis) }, null, null,
					null);
			todayInfo = new PeriodInfo();
			boolean toFirstToday = queryToday.moveToFirst();
			while (toFirstToday) {
				int type = queryToday.getInt(queryToday.getColumnIndex("type"));
				double buyerMoney = queryToday.getDouble(queryToday
						.getColumnIndex("buyerMoney"));
				todayInfo.insert(buyerMoney, type);
				toFirstToday = queryToday.moveToNext();
			}
			queryToday.close();

			// 查询本周 select * from t_transaction where tradetime>本周的ms
			long weekMillis = MyUtil.getWeekMillis(0);
			Cursor queryWeek = db.query("t_transaction", null, "tradetime>?",
					new String[] { String.valueOf(weekMillis) }, null, null,
					null);
			weekInfo = new PeriodInfo();
			boolean toFirstWeek = queryWeek.moveToFirst();
			while (toFirstWeek) {
				int type = queryWeek.getInt(queryToday.getColumnIndex("type"));
				double buyerMoney = queryWeek.getDouble(queryToday
						.getColumnIndex("buyerMoney"));
				weekInfo.insert(buyerMoney, type);
				toFirstWeek = queryWeek.moveToNext();
			}
			queryWeek.close();

			// 查询本月 select * from t_transaction where tradetime>本月的ms
			long monthMillis = MyUtil.getMonthMillis(0);
			Cursor queryMonth = db.query("t_transaction", null, "tradetime>?",
					new String[] { String.valueOf(monthMillis) }, null, null,
					null);
			monthInfo = new PeriodInfo();
			boolean toFirstMonth = queryMonth.moveToFirst();
			while (toFirstMonth) {
				int type = queryMonth.getInt(queryToday.getColumnIndex("type"));
				double buyerMoney = queryMonth.getDouble(queryToday
						.getColumnIndex("buyerMoney"));
				monthInfo.insert(buyerMoney, type);
				toFirstMonth = queryMonth.moveToNext();
			}
			queryMonth.close();

			// TODO查询最近一笔 movetolast 待添加时都有种类信息再测
			Cursor queryLast = db.query("t_transaction", null, null, null,
					null, null, null);
			if (queryLast.moveToLast()) {
				int sellerCategoryPOID = queryLast.getInt(queryLast
						.getColumnIndex("sellerCategoryPOID"));
				int type = queryLast.getInt(queryLast.getColumnIndex("type"));
				long tradeTime = queryLast.getLong(queryLast
						.getColumnIndex("tradeTime"));
				double buyerMoney = queryLast.getDouble(queryLast
						.getColumnIndex("buyerMoney"));
				queryLast.close();
				// sellerCategoryPOID找图片,标题;tradeTime计算日期;type判断支出收入
				Cursor queryCategory = db.query("t_category", null,
						"categoryPOID=?",
						new String[] { String.valueOf(sellerCategoryPOID) },
						null, null, null);
				if (queryCategory.moveToFirst()) {
					// Log.e("mainactivy", "最近");
					String name = queryCategory.getString(queryCategory
							.getColumnIndex("name"));
					String _tempIconName = queryCategory
							.getString(queryCategory
									.getColumnIndex("_tempIconName"));
					queryCategory.close();
					int imgResId = getResources().getIdentifier(_tempIconName,
							"drawable", getPackageName());
					lastinfo = new TransactionInfo();
					lastinfo.type = type;
					lastinfo.tradeTime = tradeTime;
					lastinfo.buyerMoney = buyerMoney;
					lastinfo.name = name;
					lastinfo.imgResId = imgResId;
				}
			}
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
//						MyUtil.logTime("子线程结束");
//						DecimalFormat df = new DecimalFormat("#0.00");
//						tv_money_expense.setText("¥ "+ String.valueOf(df.format(costmoney)));
//						tv_money_income.setText("¥ "+ String.valueOf(df.format(incomemoney)));
					if(tv_money_expense==null) return;
					tv_money_expense.setText("¥ "+ MyUtil.doubleFormate(costmoney));
					tv_money_income.setText("¥ "+ MyUtil.doubleFormate(incomemoney));
					budgetBattery.setBudgetData(budget, budget - costmoney);
					tv_money_budget.setText("¥ "+ MyUtil.doubleFormate(budget - costmoney));
					todayItem.setPeriodInfo(todayInfo);
					weekItem.setPeriodInfo(weekInfo);
					monthItem.setPeriodInfo(monthInfo);
					if (lastinfo != null) {
						lasterItem.setTransactionInfo(lastinfo);
					}else{
						lasterItem.restore();
					}
				}
			});

		}
	}


	/**
	 * 侧滑菜单界面
	 */
	private void initMenuView() {
		menuView = getLayoutInflater().inflate(R.layout.slidingmenu_menu, null);
		slidingMenu.setMenu(menuView);
		mgridView = (GridView) menuView.findViewById(R.id.gridView1);
		menuView.findViewById(R.id.add_accountbook).setOnClickListener(this);
//		img_book_small = (ImageView) menuView.findViewById(R.id.img_book_small);
		img_edit = (ImageView) menuView.findViewById(R.id.img_edit);
		rl1 = menuView.findViewById(R.id.rl1);
		rl2 = menuView.findViewById(R.id.rl2);

		img_edit.setOnClickListener(this);
		initMenuViewData();
		mgridView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * 点击切换账本 n
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this, "item click", 0).show();
				for (int i = 0; i < accountbookList.size(); i++) {
					if (i == position) {
						accountbookList.get(i).setChoise(true);
						// 设置显示当前账本[切换账本时滚动动画]
						setCurAccountBook(accountbookList.get(i));
						// 保存当前账本信息,
						getSharedPreferences("infos", 0)
								.edit()
								.putLong("clientID",accountbookList.get(i).getClintID())
								.commit();
					} else {
						accountbookList.get(i).setChoise(false);
					}
				}
				gridAdapter.notifyDataSetChanged();
				//TODO切换数据库
				DataBaseUtil.closeDb();
				DataBaseUtil.setCurBook(position+accountbookList.get(position).getName());
				//更换数据库
//				DataBaseUtil.setCurBook("cpoopc");
//				DataBaseUtil.closeDb();
				//更换账本后刷新主页面信息
				new Thread(new ReadMainDataThread()).start();
			}
		});
	}

	/**
	 * 垂直滚动animation
	 */
	private void initScrollAnimation() {
		if (animation == null) {
			animation = new TranslateAnimation(0, 0, 0,
					-MyUtil.dip2px(this, 80));
			animation.setDuration(500);
			animation.setFillAfter(true);
		}
	}

	/**
	 * 设置当前账本;(第一次设置时无动画,切换账本时滚动显示)
	 * mCurInfo 当前账户
	 * @param newInfo	切换后的账户
	 */
	public void setCurAccountBook(AccountBookInfo newInfo) {
		if (mCurInfo.getClintID() == newInfo.getClintID()) {// 不需要滚动,第一次设置,rl1
			((ImageView) rl1.findViewById(R.id.img_book_small))
					.setImageResource(getResources().getIdentifier(
							mCurInfo.getImgName(), "drawable", getPackageName()));
			((TextView) rl1.findViewById(R.id.tv_book_name)).setText(mCurInfo
					.getName());
		} else {
			initScrollAnimation();
			// rl1变2,clear,
			((ImageView) rl1.findViewById(R.id.img_book_small))
					.setImageResource(getResources().getIdentifier(
							mCurInfo.getImgName(), "drawable", getPackageName()));
			((TextView) rl1.findViewById(R.id.tv_book_name)).setText(mCurInfo
					.getName());
			rl1.clearAnimation();
			// rl2 clear,变新的,
			rl2.clearAnimation();
			((ImageView) rl2.findViewById(R.id.img_book_small))
					.setImageResource(getResources().getIdentifier(
							newInfo.getImgName(), "drawable", getPackageName()));
			((TextView) rl2.findViewById(R.id.tv_book_name)).setText(newInfo
					.getName());
			// startanimation
			rl1.startAnimation(animation);
			rl2.startAnimation(animation);
		}
		mCurInfo = newInfo;
	}

	private void initMenuViewData() {
		/**
		 * 查询账本信息
		 * 	CP 
		 */
		accountbookList = new ArrayList<AccountBookInfo>();
		SharedPreferences sp = getSharedPreferences("infos", 0);
		long curID = sp.getLong("clientID", 0);
		//查询有什么
//		SQLiteDatabase db = DataBaseUtil.getDb();
		SQLiteDatabase db = DataBaseUtil.getBookDb();
		Cursor query = db.query("t_account_book", null, null, null, null, null,
				"clientID");
		boolean toFirst = query.moveToFirst();
		while (toFirst) {
			long clientID = query.getLong(query.getColumnIndex("clientID"));
			String name = query.getString(query.getColumnIndex("name"));
			String imgName = query.getString(query.getColumnIndex("imgName"));
			AccountBookInfo info = new AccountBookInfo(clientID, name, imgName,
					curID == clientID ? true : false);
			if (curID == clientID) {
				mCurInfo = info;
			}
			accountbookList.add(info);
			// Log.e("curID==clientID? true:false", (curID==clientID?
			// true:false) +"");
			toFirst = query.moveToNext();
		}
		query.close();
		db.close();
		// 设置显示当前账本[第一次无滚动动画]
		setCurAccountBook(mCurInfo);
		gridAdapter = new AccountBookGridAdapter(this, accountbookList);
		mgridView.setAdapter(gridAdapter);
	}

	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);
		slidingMenu.attachToActivity(this, SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindScrollScale(0);
		// slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 如何确定? 把slidingmenu传给button,button初始化后设置
		// slidingMenu.setBehindOffsetRes(R.dimen.slidingwidth);
	}

	private void initUI() {
		// 旋转箭头指示器
		ScrollIndicatorButton scrollIndicatorButton = (ScrollIndicatorButton) findViewById(R.id.scrollIndicatorButton1);
		scrollIndicatorButton.setLineColor(getResources().getColor(
				R.color.main_indicate));
		MyCanvasTransformer canvasTransformer = new MyCanvasTransformer(
				scrollIndicatorButton);// 监听sliding
		slidingMenu.setBehindCanvasTransformer(canvasTransformer);
		scrollIndicatorButton.setSlidding(slidingMenu);
		// Log.e(TAG, scrollIndicatorButton.getRight()+"");
		// slidingMenu.setBehindOffset(scrollIndicatorButton.getButtonRight());
		scrollIndicatorButton.setOnClickListener(this);

		// 底部一排按钮,设置图片,TODO点击事件
		MainToolButton nav_yeartrans = (MainToolButton) findViewById(R.id.nav_yeartrans_btn);// 年流水账
		MainToolButton nav_account = (MainToolButton) findViewById(R.id.nav_account_btn);// 账户
		MainToolButton nav_budget = (MainToolButton) findViewById(R.id.nav_budget_btn);// 预算
		MainToolButton nav_report = (MainToolButton) findViewById(R.id.nav_report_btn);// 图表
		MainToolButton nav_setting = (MainToolButton) findViewById(R.id.nav_setting_btn);// 设置
		Button btn_add_expense_quickly = (Button) findViewById(R.id.btn_add_expense_quickly);// 记一笔
		ImageButton btn_assiatant = (ImageButton) findViewById(R.id.btn_assiatant);// 魔法棒
		nav_yeartrans.setImageDrawable(R.drawable.nav_year_trans);
		nav_account.setImageDrawable(R.drawable.nav_account);
		nav_budget.setImageDrawable(R.drawable.nav_budget);
		nav_report.setImageDrawable(R.drawable.nav_report);
		nav_setting.setImageDrawable(R.drawable.nav_setting);
		nav_yeartrans.setOnClickListener(this);
		nav_account.setOnClickListener(this);
		nav_budget.setOnClickListener(this);
		nav_report.setOnClickListener(this);
		nav_setting.setOnClickListener(this);
		btn_add_expense_quickly.setOnClickListener(this);
		btn_assiatant.setOnClickListener(this);
		// 支出,收入
		tv_money_expense = (TextView) findViewById(R.id.tv_money_expense);
		tv_money_income = (TextView) findViewById(R.id.tv_money_income);
		tv_money_budget = (TextView) findViewById(R.id.tv_money_budget);
		todayItem = (MainTopItem) findViewById(R.id.item_today);
		weekItem = (MainMiddleItem) findViewById(R.id.item_week);
		monthItem = (MainMiddleItem) findViewById(R.id.item_month);
		lasterItem = (MainBottomItem) findViewById(R.id.item_leaster);
		weekItem.setTitle("本周");
		monthItem.setTitle("本月");
		Cplistener mylistener = new Cplistener();
		monthItem.setOnCpButtonClickListener(mylistener);
		weekItem.setOnCpButtonClickListener(mylistener);
		todayItem.setOnCpButtonClickListener(mylistener);
		lasterItem.setOnCpButtonClickListener(mylistener);
		budgetBattery = (BudgetBattery) findViewById(R.id.budgetbattery);
		budgetBattery.setMyTouchListener(mylistener);
	}

	class Cplistener implements CpButtonClickListener {

		@Override
		public void onclick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.item_today:
				intent = new Intent(MainActivity.this,
						NavMonthTransactionActivity.class);
				intent.putExtra("date", "今天");
				startActivityForResult(intent, requestCode);
				break;
			case R.id.item_week:
				intent = new Intent(MainActivity.this,
						NavMonthTransactionActivity.class);
				intent.putExtra("date", "本周");
				startActivityForResult(intent, requestCode);
				break;
			case R.id.item_month:
				intent = new Intent(MainActivity.this,
						NavMonthTransactionActivity.class);
				intent.putExtra("date", "本月");
				startActivityForResult(intent, requestCode);
				break;
			case R.id.item_leaster:
				intent = new Intent(MainActivity.this,
						NavMonthTransactionActivity.class);
				intent.putExtra("date", "本月");
				startActivityForResult(intent, requestCode);
				break;
			case R.id.budgetbattery:
				intent = new Intent(MainActivity.this, BudgetActivity.class);
				intent.putParcelableArrayListExtra("expenseList", expenseList);
				startActivityForResult(intent, requestCode);
				// Toast.makeText(MainActivity.this, "预算槽点击事件", 0).show();
				break;

			default:
				break;
			}
		}
	}
/**
 * 侧滑过程的回调接口
 * @author cp
 *
 */
	class MyCanvasTransformer implements CanvasTransformer {
		ScrollIndicatorButton scrollbutton;

		public MyCanvasTransformer(ScrollIndicatorButton scrollbutton) {
			super();
			this.scrollbutton = scrollbutton;
		}

		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			scrollbutton.rotatePercent(percentOpen);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.scrollIndicatorButton1:
			slidingMenu.toggle();
			break;
		//账本
		case R.id.add_accountbook://添加账本
			intent = new Intent(this, AddAcountBookActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.img_edit: // 编辑账本
			if (gridAdapter.isEdit()) {
				gridAdapter.setEdit(false);
				img_edit.setImageResource(R.drawable.nav_edit_btn_before);
			} else {
				gridAdapter.setEdit(true);
				img_edit.setImageResource(R.drawable.nav_edit_btn_after);
			}
			break;
		// 底排tab
		case R.id.nav_yeartrans_btn://年流水账
			intent = new Intent(this, NavYearTransactionActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.nav_account_btn://账户
			intent = new Intent(this, AccountDetailActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.nav_budget_btn://预算
			intent = new Intent(this, BudgetActivity.class);
			intent.putParcelableArrayListExtra("expenseList", expenseList);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.nav_report_btn://图表
			intent = new Intent(this, ReportPiewChartActivity.class);
			intent.putParcelableArrayListExtra("expenseList", expenseList);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.nav_setting_btn://设置
			intent = new Intent(this, SettingActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		// 记一笔
		case R.id.btn_add_expense_quickly:
			intent = new Intent(this, AddOrEditTransActivity.class);
			startActivityForResult(intent, requestCode);
			break;
		case R.id.btn_assiatant://小助手
			intent = new Intent(this, AssistantActivity.class);
			startActivityForResult(intent, requestCode);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		readDb();
		new Thread(new ReadMainDataThread()).start();
		initMenuViewData();
//		 Toast.makeText(this, TAG, 0).show();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
