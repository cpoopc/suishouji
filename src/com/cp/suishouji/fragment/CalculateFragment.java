package com.cp.suishouji.fragment;

import java.util.ArrayList;

import com.cp.suishouji.AddOrEditTransActivity;
import com.cp.suishouji.R;
import com.cp.suishouji.dao.Constants;
import com.cp.suishouji.utils.MyCalculator;
import com.cp.suishouji.utils.MyUtil;
import com.cp.suishouji.widgt.CpButton;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CalculateFragment extends BaseFragment implements OnClickListener {

	ArrayList<CpButton> mybuttonList;

	private TextView tv_calcuresult;
	private MyCalculator myCalculator;
	private int type = 0;
	private double result;//暂存结果
//	private int lasttype = 0;
	public CalculateFragment(TextView  tv_calcuresult,int type) {
		this.tv_calcuresult = tv_calcuresult;
		this.type = type;
//		lasttype = type;
		myCalculator = new MyCalculator(tv_calcuresult);
	}
	public CalculateFragment(TextView  tv_calcuresult,int type ,double result) {
		this.tv_calcuresult = tv_calcuresult;
		this.type = type;
		this.result = result;
		tv_calcuresult.setText(MyUtil.doubleFormate(result));
//		lasttype = type;
		myCalculator = new MyCalculator(tv_calcuresult,result);
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if(type==0){
			tv_line.setVisibility(View.VISIBLE);
			tv_line2.setVisibility(View.INVISIBLE);
			tv_line2.clearAnimation();
		}else{
			tv_line.setVisibility(View.INVISIBLE);
			tv_line2.setVisibility(View.VISIBLE);
			tv_line.clearAnimation();
		}
		choiseButton(type);
	}
	public void setResult(double result){
		this.result = result;
		myCalculator.clear();
		myCalculator.setResult(result);
		tv_calcuresult.setText(MyUtil.doubleFormate(result));
	}
	public void clear(){
		myCalculator.clear();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mybuttonList = new ArrayList<CpButton>();
		View layout = inflater.inflate(R.layout.fragment_calculate, container, false);
		initCalculatorKey(layout);
		
		CpButton btn_cost = (CpButton) layout.findViewById(R.id.btn_cost);
		CpButton btn_income = (CpButton) layout.findViewById(R.id.btn_income);
		CpButton btn_trans = (CpButton) layout.findViewById(R.id.btn_trans);
		btn_cost.setOnClickListener(this);
		btn_income.setOnClickListener(this);
		btn_trans.setOnClickListener(this);
		tv_line = (TextView) layout.findViewById(R.id.tv_line);
		tv_line2 = (TextView) layout.findViewById(R.id.tv_line2);
		if(type==0){
			tv_line.setVisibility(View.VISIBLE);
			tv_line2.setVisibility(View.INVISIBLE);
		}else{
			tv_line.setVisibility(View.INVISIBLE);
			tv_line2.setVisibility(View.VISIBLE);
		}
//		Log.e("VISIBLE", tv_line.getVisibility()+"]["+tv_line2.getVisibility());
//		Log.e("tv_line.getHeight();", "tv_line.getHeight()"+tv_line.getHeight());
		mybuttonList.add(btn_cost);
		mybuttonList.add(btn_income);
		mybuttonList.add(btn_trans);
		
		lastindex =-1;
//		Log.e("计算界面", "type:"+type);
		choiseButton(type);
		return layout;
	}

	
//	@Override
//	public void onDetach() {
//		type = lasttype;
//		Log.e("ondetech", "ons");
//		super.onDetach();
//	}
	int lastindex =-1;
	private void choiseButton(int index){
		if(lastindex == index){
			return;
		}
		lastindex = index;
		if(index<2 && type!=index){//改变时再通知
			type = index;
			if(getActivity() instanceof AddOrEditTransActivity){
				((AddOrEditTransActivity)getActivity()).changeState(index,Constants.CALCULATE_FGM);
				if(type==0){
					((AddOrEditTransActivity)getActivity()).setActionbarTitle("支出");
				}else{
					((AddOrEditTransActivity)getActivity()).setActionbarTitle("收入");
				}
			}
		}
		CpButton button;
//		Log.e("tv_line.getHeight();", "tv_line.getHeight()"+tv_line.getHeight());
		if(!mybuttonList.get(index).isSelected()){
			if(index == 0){
				if(tv_line.getVisibility()==View.VISIBLE){
					TranslateAnimation animation2 = new TranslateAnimation(0, 0, tv_line.getHeight(),0);
					animation2.setFillAfter(true);
					animation2.setDuration(300);
					tv_line.startAnimation(animation2);
				}else{
					TranslateAnimation animation2 = new TranslateAnimation(0, 0, 0,- tv_line2.getHeight());
					animation2.setFillAfter(true);
					animation2.setDuration(300);
					tv_line2.startAnimation(animation2);
				}
			}else if(index == 1){
				if(tv_line.getVisibility()==View.VISIBLE){
					TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0, tv_line.getHeight());
					animation1.setFillAfter(true);
					animation1.setDuration(300);
					tv_line.startAnimation(animation1);
				}else{
					TranslateAnimation animation1 = new TranslateAnimation(0, 0, - tv_line2.getHeight(),0);
					animation1.setFillAfter(true);
					animation1.setDuration(300);
					tv_line2.startAnimation(animation1);
				}
			}
		}
		for (int j = 0; j < mybuttonList.size(); j++) {
			button = mybuttonList.get(j);
			if(index!=j){
//				button.setClickable(true);
				button.setSelected(false);
			}else{
				button.setSelected(true);
//				button.setClickable(false);
			}
		}
	}
	int ADD = 1;
	int DEC = 2;
	int COUNT =3;
	private TextView tv_line;
	private TextView tv_line2;
	@Override
	public void onClick(View v) {
		FragmentActivity activity = getActivity();
			switch (v.getId()) {
			case R.id.btn_cost:
//				Log.e("点击", "多吃点");
				choiseButton(0);
				break;
			case R.id.btn_income:
				choiseButton(1);
				
				break;
			case R.id.btn_trans:
//				choiseButton(2);
				break;
			case R.id.button0:
				myCalculator.pressNum("0");
				break;
			case R.id.button1:
				myCalculator.pressNum("1");
				break;
			case R.id.button2:
				myCalculator.pressNum("2");
				break;
			case R.id.button3:
				myCalculator.pressNum("3");
				break;
			case R.id.button4:
				myCalculator.pressNum("4");
				break;
			case R.id.button5:
				myCalculator.pressNum("5");
				break;
			case R.id.button6:
				myCalculator.pressNum("6");
				break;
			case R.id.button7:
				myCalculator.pressNum("7");
				break;
			case R.id.button8:
				myCalculator.pressNum("8");
				break;
			case R.id.button9:
				myCalculator.pressNum("9");
				break;
			case R.id.buttonc:
				myCalculator.pressClear();
				break;
			case R.id.buttondot:
				myCalculator.pressDot();
				break;
			case R.id.buttonadd:
				myCalculator.count(ADD);
				break;
			case R.id.buttondec:
				myCalculator.count(DEC);
				break;
			case R.id.buttonok:
				myCalculator.count();
				break;
				
			default:
				break;
			}
			
	}
	
	private void initCalculatorKey(View layout) {
		Button button0 = (Button) layout.findViewById(R.id.button0);
		Button button1 = (Button) layout.findViewById(R.id.button1);
		Button button2 = (Button) layout.findViewById(R.id.button2);
		Button button3 = (Button) layout.findViewById(R.id.button3);
		Button button4 = (Button) layout.findViewById(R.id.button4);
		Button button5 = (Button) layout.findViewById(R.id.button5);
		Button button6 = (Button) layout.findViewById(R.id.button6);
		Button button7 = (Button) layout.findViewById(R.id.button7);
		Button button8 = (Button) layout.findViewById(R.id.button8);
		Button button9 = (Button) layout.findViewById(R.id.button9);
		Button buttonc = (Button) layout.findViewById(R.id.buttonc);
		Button buttondot = (Button) layout.findViewById(R.id.buttondot);
		Button buttonadd = (Button) layout.findViewById(R.id.buttonadd);
		Button buttondec = (Button) layout.findViewById(R.id.buttondec);
		Button buttonok = (Button) layout.findViewById(R.id.buttonok);
		button0.setOnClickListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		button7.setOnClickListener(this);
		button8.setOnClickListener(this);
		button9.setOnClickListener(this);
		buttonc.setOnClickListener(this);
		buttondot.setOnClickListener(this);
		buttonadd.setOnClickListener(this);
		buttondec.setOnClickListener(this);
		buttonok.setOnClickListener(this);
	}
	@Override
	public void btnok() {
		// TODO Auto-generated method stub
		
	}

}
