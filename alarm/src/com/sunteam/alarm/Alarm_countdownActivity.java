package com.sunteam.alarm;

import java.util.Timer;
import java.util.TimerTask;

import com.iflytek.thridparty.r;
import com.sunteam.alarm.utils.Global;
import com.sunteam.common.menu.BaseActivity;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.ConfirmDialog;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.Tools;
import com.sunteam.common.utils.dialog.ConfirmListener;
import com.sunteam.common.utils.dialog.PromptListener;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class Alarm_countdownActivity extends BaseActivity {

	TextView mTitle = null;
	TextView mTv1 = null;
	TextView mTv2 = null;
	TextView mTv3 = null;
	TextView mTv4 = null;
	TextView mTv5 = null;
	View mLine = null;
	
	//private int gSelectID = 0;  // 选择是哪个
	private int gtime_len = 0;  // 到计时长度
	
	private int START_COUNTDOWN = 1;  // 倒计时开始
	private int STOP_COUNTDOWN = 0;  // 倒计时结束
	private int PAUSE_COUNTDOWN = 2;  // 倒计时暂停
	
	private int gCountDown_falg = 0;  // 选择是哪个

		
	Timer timer = new Timer();  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_countdown);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTv1 = (TextView)findViewById(R.id.tv1);
		mTv2 = (TextView)findViewById(R.id.tv2);
		mTv3 = (TextView)findViewById(R.id.tv3);
		
		mTv4 = (TextView)findViewById(R.id.tv4);
		mTv5 = (TextView)findViewById(R.id.tv5);
		
		mLine = this.findViewById(R.id.line); // 获取
		
		mTitle.setText(getResources().getString(R.string.countdown));
		
		Tools mTools = new Tools(this);
		
		this.getWindow().setBackgroundDrawable(new ColorDrawable(mTools.getBackgroundColor()));
		
		int fontSize = mTools.getFontSize();
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize); // 设置title字号
		mTitle.setHeight(fontSize);
		mLine.setBackgroundColor(mTools.getFontColor()); // 设置分割线的背景色
		
		mTitle.setTextColor(mTools.getFontColor()); //  设置字体颜色
		mTv1.setTextColor(mTools.getFontColor()); //  设置字体颜色
		mTv2.setTextColor(mTools.getFontColor()); // 设置字体颜色
		mTv3.setTextColor(mTools.getFontColor()); //  设置字体颜色
		mTv4.setTextColor(mTools.getFontColor()); // 设置字体颜色
		mTv5.setTextColor(mTools.getFontColor()); //  设置字体颜色
		
		 
		timer.schedule(task, 1000, 1000);       // timeTask  
		 
		Intent intent=getIntent();	//获取Intent
		Bundle bundle=intent.getExtras();	//获取 Bundle
			
		gtime_len  = bundle.getInt("TIMELEN");  // 获取 反显位置
		
      	int hour = gtime_len/60/60;
    	int min = gtime_len/60%60;
    	int sec = gtime_len%60;
    	String temp = null;
    	
    	temp = "";
    	if(hour < 10){
    		temp += "0";
    	}
    	mTv1.setText(temp + hour);
    	
    	temp = "";
    	if(min < 10){
    		temp += "0";
    	}
    	mTv3.setText(temp + min);
    	
    	temp = "";
    	if(sec < 10){
    		temp += "0";
    	}
    	mTv5.setText(temp + sec);
    	
    	TtsUtils.getInstance().speak(getResources().getString(R.string.countdown_start));
    	gCountDown_falg = START_COUNTDOWN;
/*    
    	PromptDialog mDialog = new PromptDialog(this, getResources().getString(R.string.countdown_start));
    	mDialog.show();
    	mDialog.setPromptListener(new PromptListener() {
			
			@Override
			public void onComplete() {
				// TODO 自动生成的方法存根
				gCountDown_falg = START_COUNTDOWN;
			}
		});*/
	}
	
	
	// 1秒钟定时
	TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
 
            runOnUiThread(new Runnable() {      // UI thread  
                @Override  
                public void run() { 
                	if(gCountDown_falg == START_COUNTDOWN){
	                	gtime_len--; 
	                	int hour = gtime_len/60/60;
	                	int min = gtime_len/60%60;
	                	int sec = gtime_len%60;
	                	String temp = null;
	                	
	                	temp = "";
	                	if(hour < 10){
	                		temp += "0";
	                	}
	                	mTv1.setText(temp + hour);
	                	
	                	temp = "";
	                	if(min < 10){
	                		temp += "0";
	                	}
	                	mTv3.setText(temp + min);
	                	
	                	temp = "";
	                	if(sec < 10){
	                		temp += "0";
	                	}
	                	mTv5.setText(temp + sec);
	                	
	                	if(gtime_len <= 0){
	                		timer.cancel();
	                		putMsg();
	                		Global.debug("gtime_len =========================="+ gtime_len);
	                		//PromptDialog mDialog = new PromptDialog(this, getResources().getString(R.string.countdown_end));
	                		//PromptDialog mDialog = new PromptDialog(this, getResources().getString(R.string.countdown_start));
	 /*               		PromptDialog mPromptDialog = new PromptDialog(getApplicationContext(), getResources().getString(R.string.countdown_end));
	                		mPromptDialog.show();
	                		mPromptDialog.setPromptListener(promptListener);
	                		//TtsUtils.getInstance().speak(getResources().getString(R.string.countdown_end));	
	                		 */
	                		 
	                	}
                	}
                }  
            });  
        }  
    };
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_ENTER ||
				keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			if(gCountDown_falg == START_COUNTDOWN){
				gCountDown_falg = PAUSE_COUNTDOWN;
				PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.countdown_Pause));
				mPromptDialog.show();
				mPromptDialog.setPromptListener(new PromptListener() {
					
					@Override
					public void onComplete() {
						// TODO 自动生成的方法存根
						TtsUtils.getInstance().speak(gtime_len/60/60 + getResources().getString(R.string.hour_time) + (gtime_len/60)%60 + getResources().getString(R.string.min_time));
					}
				});
			}
			else if(gCountDown_falg == PAUSE_COUNTDOWN){
				PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.countdown_resum));
				mPromptDialog.show();
				mPromptDialog.setPromptListener(new PromptListener() {
					
					@Override
					public void onComplete() {
						// TODO 自动生成的方法存根
						gCountDown_falg = START_COUNTDOWN;
						TtsUtils.getInstance().speak(gtime_len/60/60 + getResources().getString(R.string.hour_time) + (gtime_len/60)%60 + getResources().getString(R.string.min_time));
					}
				});
			}
		}
		else if(keyCode == KeyEvent.KEYCODE_BACK){ // 返回
			ConfirmDialog mConfirmDialog = new ConfirmDialog(this, getResources().getString(R.string.countdown_starting,
																	getResources().getString(R.string.ok),
																	getResources().getString(R.string.canel)));
			mConfirmDialog.show();
			mConfirmDialog.setConfirmListener(new ConfirmListener() {
				
				@Override
				public void doConfirm() {
					// TODO 自动生成的方法存根
					timer.cancel();
					gCountDown_falg = STOP_COUNTDOWN;
					finish();
				}
				
				@Override
				public void doCancel() {
					// TODO 自动生成的方法存根
					
				}
			});
			return true;
		}
		return super.onKeyUp(keyCode, event);
		
	}
    
    public PromptListener promptListener = new PromptListener() {
		
		@Override
		public void onComplete() {
			// TODO 自动生成的方法存根
			finish();
			Global.debug("PromptListener ========ssss=================="+ gtime_len);
		}
	};
	
	public void putMsg(){
		PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.countdown_end));
		mPromptDialog.show();
		mPromptDialog.setPromptListener(promptListener);
		//TtsUtils.getInstance().speak(getResources().getString(R.string.countdown_end));
	}
}
