package com.cp.suishouji;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.actionbarsherlock.app.ActionBar;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.dao.TemplateInfo;
import com.cp.suishouji.dao.TransactionInfo;
import com.cp.suishouji.fragment.BaseFragment;
import com.cp.suishouji.fragment.BuyerAccountFragment;
import com.cp.suishouji.fragment.CalculateFragment;
import com.cp.suishouji.fragment.CalendarFragment;
import com.cp.suishouji.fragment.CategoryFragment;
import com.cp.suishouji.fragment.ItemFragment;
import com.cp.suishouji.fragment.LocationFragment;
import com.cp.suishouji.fragment.MemberFragment;
import com.cp.suishouji.fragment.TransactionSwitchFragment;
import com.cp.suishouji.utils.DataBaseUtil;
import com.cp.suishouji.utils.MyUtil;
import com.zbar.lib.CaptureActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddOrEditTransActivity extends BaseFragmentActivity implements OnClickListener
{

	private ImageView mimg_indicate;
	private RotateAnimation rotateAnimation1;
	private RotateAnimation rotateAnimation2;
	private long durationMillis = 500;
	private boolean isUp;
	private View flowview;
	private FragmentManager fm;
	//fragment
	private TransactionSwitchFragment transactionSwitchFragment;
	
	private BaseFragment basefragment;
	private CalculateFragment calculateFragment;
	private CategoryFragment categoryFragment;
	private BuyerAccountFragment buyerAccountFragment;
	private CalendarFragment calendarFragment;
	private MemberFragment memberFragment;
	private ItemFragment itemFragment;
	private LocationFragment locationFragment;
	//计算结果
	private TextView tv_calcuresult;
	
	private ArrayList<View> viewList = new ArrayList<View>();
	private HashMap<Integer, Integer> viewindexMap = new HashMap<Integer, Integer>();
	private HashMap<Integer, TextView> textviewMap = new HashMap<Integer, TextView>();
	private int viewindex;
//	private int textviewindex;
	private View frame_top;
	//信息
	private int type = 0;//默认支出
	private long mtransactionPOID = -1;
	private long sellerCategoryPOID = -13;//默认值
	private long buyerAccountPOID = -2;//默认值 现金
	private long tradetime; 
	private long member;
	private long location;
	private String item = "无项目";
	private String psstr = "请输入备注信息";
	private double result ;
	//记录上次显示的fragment
	private int lastshow = 0;
	private int lastId = R.id.btn_number;
	private TextView actionbar_title;
	
	//模式:0添加模式,1修改模式,2复制模式
	private int mode;
//	编辑状态,需要记录原始,以便对账户余额进行修改
	private TransactionInfo edit_info;
	/**
	 * getter,setter
	 */
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
		result = 0;//默认为0
		setContentView(R.layout.activity_add_or_edit_trans);
		initActionbar();
		initAnimation();//actionbar三角形动画
		findview();
		//判断是否由其他activity跳转
		fromEdit();
		//初始化显示状态
		initShowState();
		
		//默认弹出计算器
		fm = getSupportFragmentManager();
		//初始化状态
		calculateFragment = new CalculateFragment(tv_calcuresult,type,result);
		basefragment = calculateFragment;
		flowview.setVisibility(View.GONE);
		fm.beginTransaction().add(R.id.fragment_container, basefragment).commit();
		setSelectedState(viewindexMap.get(R.id.btn_number));
		showView(Constants.CALCULATE_FGM);
		
	}
	
	//初始化显示状态(适配添加模式,编辑模式,复制模式
	private void initShowState() {
		//类别
		changeState(type, Constants.CALCULATE_FGM,sellerCategoryPOID);
		//账户信息
		Cursor queryAccount = DataBaseUtil.getDb().query("t_account", null, "accountPOID=?", new String[]{String.valueOf(buyerAccountPOID)},
				null, null, null);
		queryAccount.moveToFirst();
		String accountName = queryAccount.getString(queryAccount.getColumnIndex("name"));
		queryAccount.close();
		textviewMap.get(R.id.tv_money).setText(accountName+"(CNY)");
		//日期
		Date date = new Date(tradetime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		((TextView)findViewById(R.id.tv_date)).setText(sdf.format(date));
		//备注
		textviewMap.get(R.id.tv_ps).setText(psstr);
	}
	//从编辑跳转
	private void fromEdit(){
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode",0);
		edit_info = intent.getParcelableExtra("transactioninfo");
		if(edit_info==null)return;
		//信息有
//		int type, int transactionPOID,int sellerCategoryPOID, int buyerAccountPOID,
//		long tradeTime,double buyerMoney, String memo
		//设置显示效果准备数据
		type = edit_info.type;
		if(mode==1){//复制模式后续自动生成交易ID
			mtransactionPOID = edit_info.transactionPOID;	//记录ID	ok
		}
		sellerCategoryPOID = edit_info.sellerCategoryPOID;//类别	ok
		buyerAccountPOID = edit_info.buyerAccountPOID;//默认值 现金
		tradetime = edit_info.tradeTime;//时间 ok
		 result = edit_info.buyerMoney;	//计算器结果 ok
		 //TODO 成员,项目,商家
		if(edit_info.memo!=null){
			psstr = edit_info.memo;
		}
	}
	public void loadTemple(TemplateInfo templateInfo){
		type = templateInfo.type;
		mtransactionPOID = templateInfo.transactionPOID;	//记录ID	ok
		sellerCategoryPOID = templateInfo.sellerCategoryPOID;//类别	ok
		buyerAccountPOID = templateInfo.buyerAccountPOID;//默认值 现金
		 result = templateInfo.buyerMoney;	//计算器结果 ok
		 tv_calcuresult.setText(MyUtil.doubleFormate(result));
		 //TODO 成员,项目,商家
		if(templateInfo.memo!=null){
			psstr = templateInfo.memo;
		}
			initShowState();
			showTransactionSwitch();
	}
	//切换支出,收入状态
	public void changeState(int state,int from){
		if(state==0){
			changeState(state, from, -13);
		}else{
			changeState(state, from, -58);
		}
	}
	public void changeState(int state,int from,long sellerCategory){
		type=state;
		//TODO 通知fragment
		if(calculateFragment!=null){
			if(from!=Constants.CALCULATE_FGM){
				calculateFragment.setType(type);
			}
		}
		if(categoryFragment!=null){
			categoryFragment.setType(type);
		}
		this.sellerCategoryPOID = sellerCategory;
		SQLiteDatabase db = DataBaseUtil.getDb();
		Cursor query1 = db.query("t_category", null, "categoryPOID=?", new String[]{String.valueOf(sellerCategoryPOID)},
				null, null, null);
		query1.moveToFirst();
		String childname = query1.getString(query1.getColumnIndex("name"));
		long parentCategoryPOID = query1.getLong(query1.getColumnIndex("parentCategoryPOID"));
		query1.close();
		Cursor query2 = db.query("t_category", null, "categoryPOID=?", new String[]{String.valueOf(parentCategoryPOID)},
				null, null, null);
		query2.moveToFirst();
		String parentname = query2.getString(query2.getColumnIndex("name"));
		query2.close();
		textviewMap.get(R.id.tv_category).setText(parentname+">"+childname);
		
		switch (state) {
		case 0://支出 状态
			//TODO 改变类别信息,从数据库度
			tv_calcuresult.setTextColor(getResources().getColor(R.color.green));//绿色为支出
			break;
		case 1://收入 状态
			tv_calcuresult.setTextColor(getResources().getColor(R.color.red));//红色为收入
			
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(flowview.getVisibility()==View.VISIBLE){
				flowview.setVisibility(View.GONE);
				return true;
			}
			if(frame_top.getVisibility()==View.VISIBLE){
				frame_top.setVisibility(View.GONE);
				return true;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	private void findview() {
		//actionbar
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.title).setOnClickListener(this);
		mimg_indicate = (ImageView) findViewById(R.id.img_indicate);
		frame_top = findViewById(R.id.frame_top);
		//摄像头
//		ImageView img_camera = (ImageView) findViewById(R.id.img_camera);
		findViewById(R.id.img_camera).setOnClickListener(this);
		//选项
		findAddView(R.id.btn_number);//找到button并加入点击列表
		findAddView(R.id.btn_category);//类别
		findViewById(R.id.tv_category);
		findAddTextView(R.id.tv_category);//找到textview并加入更新列表
		findAddView(R.id.btn_account);//账户
		findAddTextView(R.id.tv_money);
		findAddView(R.id.btn_tradetime);//交易时间
		findAddTextView(R.id.tv_date);
		findAddView(R.id.btn_buyer);//成员
		findAddTextView(R.id.tv_member);
		findAddView(R.id.btn_item);//项目
		findAddTextView(R.id.btn_item);
		findAddView(R.id.btn_location);//地点商家
		findAddTextView(R.id.btn_location);
		findAddView(R.id.btn_ps);//备注
		findAddTextView(R.id.tv_ps);//备注
		findViewById(R.id.btn_save01).setOnClickListener(this);//保存
		findViewById(R.id.btn_save02).setOnClickListener(this);//保存模板
		findViewById(R.id.btn_save03).setOnClickListener(this);//保存再记
//		tv_ps = (TextView) findViewById(R.id.tv_ps);
//		setSelectedState(0);//初始化button选中状态
		tv_calcuresult = (TextView) findViewById(R.id.tv_calcuresult);
		//底部fragment
//		container = findViewById(R.id.fragment_container);
		flowview = findViewById(R.id.flowview);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		
//		//显示文字初始化
		tradetime = System.currentTimeMillis();
	}
	private void findAddTextView(int resID){
		TextView view = (TextView) findViewById(resID);
		textviewMap.put(resID, view);
	}
	private void findAddView(int resID){
		View view = findViewById(resID);
		view.setOnClickListener(this);
		viewList.add(view);
		viewindexMap.put(resID, viewindex++);
	}
	private void setSelectedState(int i){
		for (int j = 0; j < viewList.size(); j++) {
			View view = viewList.get(j);
			if(j==i){
				if(view.isSelected()){
					view.setSelected(false);
				}else{
					view.setSelected(true);
				}
			}else{
				view.setSelected(false);
			}
		}
	}
	//actionbar三角形旋转动画
	private void initAnimation() {
		rotateAnimation1 = new RotateAnimation(0, -180,10, 8);
		rotateAnimation1.setFillAfter(true);
		rotateAnimation1.setDuration(durationMillis);
		rotateAnimation2 = new RotateAnimation(-180, 0,10, 8);
		rotateAnimation2.setFillAfter(true);
		rotateAnimation2.setDuration(durationMillis);
	}

	private void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View inflate = getLayoutInflater().inflate(R.layout.actionbar_addoredittrans, null);
		actionbar_title = (TextView) inflate.findViewById(R.id.tv_title);
		inflate.findViewById(R.id.press_save).setOnClickListener(this);
		actionBar.setCustomView(inflate,layout);
	}
	public void setActionbarTitle(String title){
		actionbar_title.setText(title);
	}
	public void setType(int type){
		this.type = type;
	}
	
	/**
	 * 按键点击事件咯
	 */
	@Override
	public void onClick(View v) {
		lastId = v.getId();
		Intent intent = null;
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.img_camera:
			intent = new Intent(this,CaptureActivity.class);
			startActivityForResult(intent, 111);
			break;
		case R.id.press_save:
			save();
			break;
		case R.id.btn_save01://保存
			save();
			break;
		case R.id.btn_save02://保存模板
			save(1);
			break;
		case R.id.btn_save03://再记一笔
			save(2);
			break;
		case R.id.title:
			showTransactionSwitch();
			break;
		case R.id.btn_number:
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
		case R.id.btn_tradetime:
			showView(Constants.TRADETIME_FGM);
			setSelectedState(viewindexMap.get(v.getId()));
			break;
		case R.id.btn_buyer:
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
		case R.id.btn_ps:
			intent = new Intent(this,PsNoteActivity.class);
			intent.putExtra("ps",textviewMap.get(R.id.tv_ps).getText().toString());
			intent.putExtra("category", textviewMap.get(R.id.tv_category).getText().toString());
			intent.putExtra("buyermoney", tv_calcuresult.getText().toString());
			startActivityForResult(intent, Constants.PSNOTE);
			break;
		case R.id.btn_ok:
			ok();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1://Constants.PSNOTE	备注
			if(resultCode==Constants.PSNOTE){
				psstr = data.getStringExtra("ps");
				textviewMap.get(R.id.tv_ps).setText(psstr);
			}
			break;
		case 111:
			if(resultCode==111){
				psstr = data.getStringExtra("scanresult");
				textviewMap.get(R.id.tv_ps).setText(psstr);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void ok() {
		basefragment.btnok();
		showView(lastshow);
		setSelectedState(-1);
	}
	/**
	 * 顶部fragment显示隐藏
	 */
	public void showTransactionSwitch() {
		if(isUp){
			mimg_indicate.startAnimation(rotateAnimation2);//三角动画
			//页面动画
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, 0, -frame_top.getHeight());
			translateAnimation.setDuration(500);
			frame_top.startAnimation(translateAnimation);
			frame_top.setVisibility(View.GONE);
//			fm.beginTransaction().remove(transactionSwitchFragment);
		}else{
			if(transactionSwitchFragment==null){
				transactionSwitchFragment = new TransactionSwitchFragment(type);
			}else{
//				transactionSwitchFragment.selectTab(type);
			}
			fm.beginTransaction().replace(R.id.frame_top, transactionSwitchFragment).commit();
			mimg_indicate.startAnimation(rotateAnimation1);
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, -frame_top.getHeight(), 0 );
			translateAnimation.setDuration(500);
			frame_top.startAnimation(translateAnimation);
			flowview.setVisibility(View.GONE);
			frame_top.setVisibility(View.VISIBLE);
			setSelectedState(-1);
		}
		isUp = !isUp;
		//CP TEST
//			type = 1;
		if(categoryFragment!=null){
			categoryFragment=null;
		}
	}
/**
 * 保存交易记录
 */
	private void save(){
		save(0);
	}
	private void save(int savemode) {
		//计算结果-->double
		String calcuresult = tv_calcuresult.getText().toString();
		Double buyerMoney = MyUtil.String2double(calcuresult);
		if(buyerMoney==0f){
			Toast.makeText(this, "请输入非0的值", Toast.LENGTH_LONG).show();
			return;
		}else if(savemode==0){
			Toast.makeText(this, "保存成功!", Toast.LENGTH_LONG).show();
			finish();
		}else if(savemode==1){
			Toast.makeText(this, "保存模板!", Toast.LENGTH_LONG).show();
		}else if(savemode==2){
			Toast.makeText(this, "保存成功,再记一笔!", Toast.LENGTH_LONG).show();
			
			tv_calcuresult.setText("0.00");
			if(calculateFragment!=null){
				calculateFragment.clear();
			}
		}
//		Log.e("valueofresult", ""+buyerMoney);
		SQLiteDatabase db = DataBaseUtil.getDb();
		ContentValues values = new ContentValues();
		//transactionPOID,createdTime,modifiedTime,tradeTime,type不能为空
		//buyerMoney,sellerCategoryPOID,memo备注
		int transactionPOID;
		if(mtransactionPOID == -1){
			//查询transactionPOID
			Cursor queryid = db.query("t_transaction", null, null, null, null, null, "transactionPOID");
			boolean toFirst = queryid.moveToFirst();
			if(toFirst){
				transactionPOID = queryid.getInt(queryid.getColumnIndex("transactionPOID"));
				queryid.close();
			}else{
				transactionPOID = 0;
			}
		}else{
			transactionPOID = (int) mtransactionPOID;
		}
		//TODO CP 严格按数据库存储,分别存入t_category,"t_transaction_projectcategory_map"
		values.put("transactionPOID", --transactionPOID);
		values.put("buyerMoney", buyerMoney);
		values.put("type", type);//0支出,1收入
		values.put("createdTime", System.currentTimeMillis());
		values.put("modifiedTime", System.currentTimeMillis());
		values.put("sellerCategoryPOID", sellerCategoryPOID);//获取 类别		表t_category 
		values.put("buyerAccountPOID", buyerAccountPOID);// 	账户 现金..	表t_account_group 
		values.put("tradeTime", tradetime);// 获取 交易时间	
		//这两项其实是保存在表"t_transaction_projectcategory_map"
		values.put("buyerCategoryPOID", member);//	成员.. 	表"t_tag"
		values.put("relation", item);// 项目		表"t_tag"
		values.put("relationUnitPOID", location);// 商家地点	t_tradingEntity
		values.put("memo", psstr);// 获取 备注
		
		switch (mode) {
		case 0://添加模式
			db.insert("t_transaction", null, values );
			break;
		case 1://编辑模式
			values.remove("transactionPOID");
			db.update("t_transaction", values, "transactionPOID=?", new String[]{String.valueOf(mtransactionPOID)});
			break;
		case 2:
			db.insert("t_transaction", null, values );
			break;

		default:
			break;
		}
		
/**
 * 信息录入账户
 */
		if(this.type != 1){
			buyerMoney = -buyerMoney;//支出为负,收入为正
		}
		/**
		 * 编辑模式特殊处理,需要先扣除原始
		 * 添加模式则与复制模式一样
		 */	
		if(mode==1){
			if(edit_info.type!=1){//支出
//				edit_info.buyerMoney//扣除支出,+
				DataBaseUtil.modify(edit_info.buyerMoney, edit_info.buyerAccountPOID);
			}else{
				DataBaseUtil.modify(-edit_info.buyerMoney, edit_info.buyerAccountPOID);
				
			}
		}
		DataBaseUtil.modify(buyerMoney, buyerAccountPOID);
	}
	//fragment内容选择改变时,会调用此方法
	public void setText(int resId,String str,long categoryPOID){
			TextView textview = textviewMap.get(resId);
//			TextView textview = textviewList.get(textviewMap.get(resId));
			textview.setText(str);
			this.sellerCategoryPOID = categoryPOID; 
	}
	//fragment内容选择改变时,会调用此方法
	public void setText(int resId,String str){
		TextView textview = textviewMap.get(resId);
		textview.setText(str);
	}
	//CP 可以改用simpledateformate
	public void setText(int resId,int year, int month, int day ,int hour,int minute){
		TextView textview = textviewMap.get(resId);
		tradetime = new Date(year-1900, month-1, day-1, hour, minute).getTime();
//		Log.e("tradetime", tradetime+":"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(tradetime)));
		String monthstr,daystr,hourstr,minutestr;
		if(month<10){
			monthstr = "0"+month;
		}else{
			monthstr = String.valueOf(month);
		}
		if(day<10){
			daystr = "0"+day;
		}else{
			daystr = String.valueOf(day);
		}
		if(hour<10){
			hourstr = "0"+hour;
		}else{
			hourstr = String.valueOf(hour);
		}
		if(minute<10){
			minutestr = "0"+minute;
		}else{
			minutestr = String.valueOf(minute);
		}
		textview.setText(year+"-"+monthstr+"-"+daystr+" "+hourstr+":"+minutestr);
	}
	/**
	 * 显示隐藏fragment
	 * 点击同一项时隐藏,点击不同项时显示
	 * @param whitch
	 */
	private void showView(int whitch) {
		lastshow = whitch;
		boolean isVisiable = false;
		if(flowview.getVisibility()==View.VISIBLE){
			flowview.setVisibility(View.GONE);
			isVisiable = true;
		}
			
			switch (whitch) {
			case 0://Constants.CALCULATE
				if(basefragment==calculateFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(calculateFragment==null){
						calculateFragment = new CalculateFragment(tv_calcuresult,type);
					}
				basefragment = calculateFragment;
				break;
			case 1://Constants.CATEGORY_FGM
				if(basefragment==categoryFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(categoryFragment==null){
					categoryFragment = new CategoryFragment(R.id.tv_category,type);
				}
				basefragment = categoryFragment;
				break;
			case 2://Constants.ACCOUNT_FGM
				if(basefragment==buyerAccountFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(buyerAccountFragment==null){
					buyerAccountFragment = new BuyerAccountFragment(R.id.tv_money,type);
				}
				basefragment = buyerAccountFragment;
				break;
			case 3://Constants.ACCOUNT_FGM
				if(basefragment==calendarFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(calendarFragment==null){
					calendarFragment = new CalendarFragment(R.id.tv_date);
				}
				basefragment = calendarFragment;
				break;
			case 4://Constants.MEMBER_FGM
				if(basefragment==memberFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(memberFragment==null){
					memberFragment = new MemberFragment(R.id.tv_member);
				}
				basefragment = memberFragment;
				break;
			case 5://Constants.ITEM_FGM
				if(basefragment==itemFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(itemFragment==null){
					itemFragment = new ItemFragment(R.id.btn_item);
				}
				basefragment = itemFragment;
				break;
			case 6://Constants.LOCATION_FGM
				if(basefragment==locationFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(locationFragment==null){
					locationFragment = new LocationFragment(R.id.btn_location);
				}
				basefragment = locationFragment;
				break;
			
			default:
//				return;
				if(basefragment==categoryFragment){
					if(isVisiable){
						return;
					}else{
						break;
					}
				}else if(categoryFragment==null){
					categoryFragment = new CategoryFragment(R.id.tv_category,type);
				}
				basefragment = categoryFragment;
				break;
			}
			// 保存fragment状态
			//ft.addtobackstack(null)不行
			fm.beginTransaction().replace(R.id.fragment_container, basefragment).commit();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, Math.max(MyUtil.dip2px(this, 240), flowview.getHeight()), 0);
			translateAnimation.setDuration(500);
			flowview.setVisibility(View.VISIBLE);
			flowview.startAnimation(translateAnimation);
//			Log.e("flowview.getHeight()", flowview.getHeight()+"");
	}
	
	
}
