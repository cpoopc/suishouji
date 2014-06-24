package com.cp.suishouji;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.dao.TemplateInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.fragment.BaseFragment;
import com.cp.suishouji.fragment.BuyerAccountFragment;
import com.cp.suishouji.fragment.CalculateSimpleFragment;
import com.cp.suishouji.fragment.CategoryFragment;
import com.cp.suishouji.fragment.ItemFragment;
import com.cp.suishouji.fragment.LocationFragment;
import com.cp.suishouji.fragment.MemberFragment;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;

import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTempleActivity extends BaseFragmentActivity implements
		OnClickListener {

	private TextView actionbar_title;
	private TextView tv_calculate;
	private EditText edt_templename;
	// private TextView tv_calculate;
	// private TextView tv_category;
	// private TextView tv_account;
	// private TextView tv_member;
	// private TextView tv_item;
	// private TextView tv_location;
	private EditText tv_ps;

	private ArrayList<View> viewList = new ArrayList<View>();
	private HashMap<Integer, Integer> viewindexMap = new HashMap<Integer, Integer>();
	private HashMap<Integer, TextView> textviewMap;
	private int viewindex;
	// 信息
	private int type = 0;// 默认支出
	private long mtransactionPOID = -1;
	private long sellerCategoryPOID = -13;// 默认值
	private long buyerAccountPOID = -2;// 默认值 现金
	private long tradetime;
	private long member;
	private long location;
	private String item = "无项目";
	private String psstr = "请输入备注信息";
	private double result;
	private TemplateInfo edit_info;
	private int mode;

	// fragment相关
	private int lastshow;
	private View flowview;
	private FragmentManager fm;
	private BaseFragment basefragment = new CategoryFragment();
	private CalculateSimpleFragment calculateFragment;
	private CategoryFragment categoryFragment;
	private BuyerAccountFragment buyerAccountFragment;
	private MemberFragment memberFragment;
	private ItemFragment itemFragment;
	private LocationFragment locationFragment;

	public void setPsStr(String psstr) {
		this.psstr = psstr;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setAccount(long buyerAccountPOID) {
		this.buyerAccountPOID = buyerAccountPOID;
	}

	public void setLocation(long location) {
		this.location = location;
	}

	public void setMember(long member) {
		this.member = member;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_temple);
		initActionbar();
		findview();
		// 判断是否由其他activity跳转
		fromEdit();
		// 初始化显示状态
		initShowState();
	}

	// 从编辑跳转
	private void fromEdit() {
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", 0);
		edit_info = intent.getParcelableExtra("templateInfo");
		if (edit_info == null)
			return;
		// 设置显示效果准备数据
		type = edit_info.type;
		if (mode == 1) {// 复制模式后续自动生成交易ID
			mtransactionPOID = edit_info.transactionPOID; // 记录ID ok
		}
		sellerCategoryPOID = edit_info.sellerCategoryPOID;// 类别 ok
		buyerAccountPOID = edit_info.buyerAccountPOID;// 默认值 现金
		// tradetime = edit_info.tradeTime;//时间 ok
		result = edit_info.buyerMoney; // 计算器结果 ok
		tv_calculate.setText(MyUtil.doubleFormate(result));
		if (edit_info.memo != null) {
			psstr = edit_info.memo;
		}
	}

	// 初始化显示状态(适配添加模式,编辑模式,复制模式
	private void initShowState() {
		// 类别
		changeState(type, Constants.CALCULATE_FGM, sellerCategoryPOID);
		// 账户信息
		Cursor queryAccount = DataBaseUtil.getDb().query("t_account", null,
				"accountPOID=?",
				new String[] { String.valueOf(buyerAccountPOID) }, null, null,
				null);
		queryAccount.moveToFirst();
		String accountName = queryAccount.getString(queryAccount
				.getColumnIndex("name"));
		textviewMap.get(R.id.tv_account).setText(accountName + "(CNY)");
		// 备注
		// textviewMap.get(R.id.tv_ps).setText(psstr);
		tv_ps.setText(psstr);
	}

	public void changeState(int state, int from, long sellerCategory) {
		type = state;
		// TODO 通知fragment
		if (categoryFragment != null) {
			categoryFragment.setType(type);
		}
		this.sellerCategoryPOID = sellerCategory;
		SQLiteDatabase db = DataBaseUtil.getDb();
		Cursor query1 = db.query("t_category", null, "categoryPOID=?",
				new String[] { String.valueOf(sellerCategoryPOID) }, null,
				null, null);
		query1.moveToFirst();
		String childname = query1.getString(query1.getColumnIndex("name"));
		long parentCategoryPOID = query1.getLong(query1
				.getColumnIndex("parentCategoryPOID"));
		Cursor query2 = db.query("t_category", null, "categoryPOID=?",
				new String[] { String.valueOf(parentCategoryPOID) }, null,
				null, null);
		query2.moveToFirst();
		String parentname = query2.getString(query2.getColumnIndex("name"));
		textviewMap.get(R.id.tv_category).setText(parentname + ">" + childname);

		switch (state) {
		case 0:// 支出 状态
				// TODO 改变类别信息,从数据库度
			tv_calculate.setTextColor(getResources().getColor(R.color.green));// 绿色为支出
			break;
		case 1:// 收入 状态
			tv_calculate.setTextColor(getResources().getColor(R.color.red));// 红色为收入

			break;

		default:
			break;
		}
	}

	private void setSelectedState(int i) {
		for (int j = 0; j < viewList.size(); j++) {
			View view = viewList.get(j);
			if (j == i) {
				if (view.isSelected()) {
					view.setSelected(false);
				} else {
					view.setSelected(true);
				}
			} else {
				view.setSelected(false);
			}
		}
	}

	private void findview() {
		// 找到每行,并加入点击列表
		viewindex = 0;
		findAddView(R.id.btn_templename);
		findAddView(R.id.btn_calculate);
		findAddView(R.id.btn_category);
		findAddView(R.id.btn_account);
		findAddView(R.id.btn_member);
		findAddView(R.id.btn_item);
		findAddView(R.id.btn_location);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		// findViewById(R.id.btn_ps).setOnClickListener(this);
		edt_templename = (EditText) findViewById(R.id.tv_templename);
		// 找到每行的textview
		textviewMap = new HashMap<Integer, TextView>();
		tv_calculate = (TextView) findViewById(R.id.tv_calculate);
		findAddTextView(R.id.tv_calculate);
		findAddTextView(R.id.tv_category);
		findAddTextView(R.id.tv_account);
		findAddTextView(R.id.tv_member);
		findAddTextView(R.id.tv_item);
		findAddTextView(R.id.tv_location);
		tv_ps = (EditText) findViewById(R.id.tv_ps);
		// flowview
		flowview = findViewById(R.id.flowview);
		fm = getSupportFragmentManager();
	}

	private void findAddTextView(int resID) {
		TextView view = (TextView) findViewById(resID);
		textviewMap.put(resID, view);
	}

	private void findAddView(int resID) {
		View view = findViewById(resID);
		view.setOnClickListener(this);
		viewList.add(view);
		viewindexMap.put(resID, viewindex++);
	}

	private void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(
				R.layout.actionbar_addoredittrans, null);
		actionbar_title = (TextView) inflate.findViewById(R.id.tv_title);
		inflate.findViewById(R.id.img_back).setOnClickListener(this);
		inflate.findViewById(R.id.press_save).setOnClickListener(this);
		inflate.findViewById(R.id.title).setOnClickListener(this);
		actionBar.setCustomView(inflate, layout);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.press_save:
			save();
			setResult(0);
			finish();
			Toast.makeText(this, "保存成功!", 0).show();
			break;
		case R.id.title:
			// showTransactionSwitch();
			break;
		case R.id.btn_templename:
			break;
		case R.id.btn_calculate:
			showView(Constants.CALCULATE_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_category:
			showView(Constants.CATEGORY_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_account:
			showView(Constants.ACCOUNT_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_member:
			showView(Constants.BUYER_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_item:
			showView(Constants.ITEM_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_location:
			showView(Constants.LOCATION_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_ok:
			ok();
		default:
			break;
		}
	}

	// fragment内容选择改变时,会调用此方法
	public void setText(int resId, String str, long categoryPOID) {
		TextView textview = textviewMap.get(resId);
		// TextView textview = textviewList.get(textviewMap.get(resId));
		textview.setText(str);
		this.sellerCategoryPOID = categoryPOID;
		Log.e("addoredit", "" + sellerCategoryPOID);
	}

	// fragment内容选择改变时,会调用此方法
	public void setText(int resId, String str) {
		TextView textview = textviewMap.get(resId);
		textview.setText(str);
	}

	private void ok() {
		basefragment.btnok();
		showView(lastshow);
		setSelectedState(-1);
	}

	private void save() {
		// 信息
//		Log.e("编辑模板", "类别categoryPOID:"+sellerCategoryPOID+", 账户buyerAccountPOID:"+buyerAccountPOID+
//				", 成员member:"+member+", 项目:item"+item+", 商家:location"+location+", 备注:"+psstr);
		//TODO 保存到t_transaction_template
		SQLiteDatabase db = DataBaseUtil.getDb();
		if(mode!=1){
			Cursor query = db.query("t_transaction_template", null, null, null, null, null, "transactionTemplatePOID");
			if(query.moveToFirst()){
				mtransactionPOID = query.getLong(query.getColumnIndex("transactionTemplatePOID")) - 1;
			}else{
				mtransactionPOID = 0;
			}
			query.close();
		}
		ContentValues values = new ContentValues();
		values.put("transactionTemplatePOID", mtransactionPOID);
		values.put("name", edt_templename.getText().toString().trim());
		values.put("type", type);
		values.put("buyerMoney", MyUtil.String2double(tv_calculate.getText().toString()));
		values.put("sellerCategoryPOID", sellerCategoryPOID);//获取 类别		表t_category 
		values.put("buyerAccountPOID", buyerAccountPOID);// 	账户 现金..	表t_account_group 
		values.put("buyerCategoryPOID", member);//	成员.. 	表"t_tag"
		values.put("relation", item);// 项目		表"t_tag"
		values.put("relationUnitPOID", location);// 商家地点	t_tradingEntity
		if(!"请输入备注信息".equals(tv_ps.getText().toString())){
			psstr = tv_ps.getText().toString();
			values.put("memo", psstr);// 获取 备注
		}
//		values.
		if(mode==1){//编辑模式
			values.remove("transactionTemplatePOID");
			db.update("t_transaction_template", values, "transactionTemplatePOID=?", new String[]{String.valueOf(mtransactionPOID)});
		}else{
			db.insert("t_transaction_template", null, values);
		}
	}

	private void showView(int whitch) {
		lastshow = whitch;
		boolean isVisiable = false;
		if (flowview.getVisibility() == View.VISIBLE) {
			flowview.setVisibility(View.GONE);
			isVisiable = true;
		}

		switch (whitch) {
		case 0:// Constants.CALCULATE
			if (basefragment == calculateFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (calculateFragment == null) {
				calculateFragment = new CalculateSimpleFragment(tv_calculate);
			}
			basefragment = calculateFragment;
			break;
		case 1:// Constants.CATEGORY_FGM
			if (basefragment == categoryFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (categoryFragment == null) {
				categoryFragment = new CategoryFragment(R.id.tv_category, type);
			}
			basefragment = categoryFragment;
			break;
		case 2:// Constants.ACCOUNT_FGM
			if (basefragment == buyerAccountFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (buyerAccountFragment == null) {
				buyerAccountFragment = new BuyerAccountFragment(
						R.id.tv_account, type);
			}
			basefragment = buyerAccountFragment;
			break;
		case 3:// Constants.ACCOUNT_FGM
			break;
		case 4:// Constants.MEMBER_FGM
			if (basefragment == memberFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (memberFragment == null) {
				memberFragment = new MemberFragment(R.id.tv_member);
			}
			basefragment = memberFragment;
			break;
		case 5:// Constants.ITEM_FGM
			if (basefragment == itemFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (itemFragment == null) {
				itemFragment = new ItemFragment(R.id.tv_item);
			}
			basefragment = itemFragment;
			break;
		case 6:// Constants.LOCATION_FGM
			if (basefragment == locationFragment) {
				if (isVisiable) {
					return;
				} else {
					break;
				}
			} else if (locationFragment == null) {
				locationFragment = new LocationFragment(R.id.tv_location);
			}
			basefragment = locationFragment;
			break;

		default:
			break;
		}
		// 保存fragment状态
		fm.beginTransaction().replace(R.id.fragment_container, basefragment)
				.commit();
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
				Math.max(MyUtil.dip2px(this, 240), flowview.getHeight()), 0);
		translateAnimation.setDuration(500);
		flowview.setVisibility(View.VISIBLE);
		flowview.startAnimation(translateAnimation);
	}

}
