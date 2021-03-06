package com.sunteam.alarm;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.sunteam.alarm.player.MyPlayer;
import com.sunteam.alarm.utils.Global;
import com.sunteam.alarm.utils.Pinyin4jUtils;
import com.sunteam.common.menu.MenuActivity;
import com.sunteam.common.tts.TtsCompletedListener;
import com.sunteam.common.tts.TtsUtils;
import com.sunteam.common.utils.PromptDialog;
import com.sunteam.common.utils.dialog.PromptListener;
import com.sunteam.receiver.Alarmpublic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

public class Alarm_SetInfoActivity extends MenuActivity {

	private int gSelectID = 0;

	private int gSetID = 0; // for 反显
	private int gInterfaceFlag = 0;
	private String gfileName = null;
	private int gType = 0; // for 反显
	private int gOnoff = 0; // for 反显

	private boolean gonFileFlag = false; // for 反显
	private MyPlayer myPlayer = null;
	private ArrayList<String> mTemp = new ArrayList<String>(); // 显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Global.debug("[Alarm_SetInfoActivity] ==     onCreate == \r\n");

		Intent intent = getIntent(); // 获取Intent
		Bundle bundle = intent.getExtras(); // 获取 Bundle

		gSelectID = bundle.getInt("ID"); // 获取 反显位置
		gInterfaceFlag = bundle.getInt("FLAG"); // 获取界面标志

		// Global.debug("Alarm_SetInfoActivity ==== gSelectID = " + gSelectID);
		// Global.debug("Alarm_SetInfoActivity ==== gInterfaceFlag = " +
		// gInterfaceFlag);

		if (gInterfaceFlag == Global.ALARM_INFO_INTERFACE) // 闹钟 详情界面
		{
			if (gSelectID == Global.ALARM_SET_MUSIC) { // 铃声
				gfileName = bundle.getString("FILENAME");

				mTitle = getResources().getString(R.string.music_title);
				gonFileFlag = false;
				mTemp = getPathAudioList();
				if (mTemp.size() <= 0) {
					gonFileFlag = true;
				} else { // 有文件
					Collections.sort(mTemp, new UsernameComparator());
					gSetID = mTemp.indexOf(gfileName);
					if (gSetID < 0) {
						gSetID = 0;
					}
				}
			} else if (gSelectID == Global.ALARM_SET_TYPE) { // 闹钟类型
				mTitle = getResources().getString(R.string.type_title);
				gType = bundle.getInt("ALARMTYPE");
				mTemp.add(getResources().getString(R.string.type1));
				mTemp.add(getResources().getString(R.string.type2));
				mTemp.add(getResources().getString(R.string.type3));
				gSetID = gType;
			} else if (gSelectID == Global.ALARM_SET_ONOFF) { // 闹钟开关
				mTitle = getResources().getString(R.string.onoff_title);
				gOnoff = bundle.getInt("ONOFF");
				mTemp.add(getResources().getString(R.string.off));
				mTemp.add(getResources().getString(R.string.on));
				gSetID = gOnoff;
			}
		} else if (gInterfaceFlag == Global.ANNIVERSARY_INFO_INTERFACE) { // 纪念日详情界面
			if (gSelectID == Global.ANNIVERSARY_SET_MUSIC) { // 铃声
				gfileName = bundle.getString("FILENAME");

				mTitle = getResources().getString(R.string.music_title);
				gonFileFlag = false;
				mTemp = getPathAudioList();
				if (mTemp.size() <= 0) {
					gonFileFlag = true;
				} else {
					Collections.sort(mTemp, new UsernameComparator());
					gSetID = mTemp.indexOf(gfileName);
					if (gSetID < 0) {
						gSetID = 0;
					}
				}
			} else if (gSelectID == Global.ANNIVERSARY_SET_ONOFF) { //开关
				mTitle = getResources().getString(R.string.onoff_title);
				gOnoff = bundle.getInt("ONOFF");
				mTemp.add(getResources().getString(R.string.off));
				mTemp.add(getResources().getString(R.string.on));
				gSetID = gOnoff;
			}
		}
		mMenuList = mTemp;
		selectItem = gSetID;
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_alarm__set_info);
		
		myPlayer = MyPlayer.getInstance(this, null);
	}

	@Override
	protected void onResume() {
	
		super.onResume();
		TtsUtils.getInstance().setCompletedListener(mCompletedListener);
		
		if (true == gonFileFlag) {
			PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.no_file));
			mPromptDialog.show();
			mPromptDialog.setPromptListener(new PromptListener() {

				@Override
				public void onComplete() {
					
					 mHandler.sendEmptyMessage(Global.MSG_SETTING_FINISH);
				}
			});
		}
		
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		TtsUtils.getInstance().setCompletedListener(null);
		myPlayer.stopPlayback();	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMenuList.size() <= 0) {
				PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.no_file));
				mPromptDialog.show();

				return true;
			}
			myPlayer.stopPlayback();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	// 按键处理 抬起处理
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// 确认键
		if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (mMenuList.size() <= 0) {
				return true;
			}
			PromptDialog mPromptDialog = new PromptDialog(this, getResources().getString(R.string.set_ok));
			mPromptDialog.show();

			mPromptDialog.setPromptListener(new PromptListener() {

				@Override
				public void onComplete() {
					mHandler.sendEmptyMessage(Global.MSG_SETTING_BACK);
				}
			});
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMenuList.size() <= 0) {
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回

		}
		return super.onKeyUp(keyCode, event);
	}

	// 显示list
	private ArrayList<String> getPathAudioList() {
		ArrayList<String> temp = new ArrayList<String>();

		File mFile = new File(Alarmpublic.ALARM_PATH + getResources().getString(R.string.folder) + "/"); // 获取路径内容
		Global.debug(
				"can read === filePath =" + Alarmpublic.ALARM_PATH + getResources().getString(R.string.folder) + "/");
		if (mFile.canRead()) // 可读
		{
			if (mFile.isDirectory()) // 是文件夹
			{
				// 过虑条件
				FileFilter ff = new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						
						return !pathname.isHidden();// 过滤隐藏文件
					}
				};

				Global.debug("is Directory === ");
				// 获取文件夹内文件
				File[] mFiles = mFile.listFiles(ff);

				temp.clear();

				Global.debug("is Directory === length =" + mFiles.length);
				/* 获取文件列表 */
				// 获取文件列表
				for (File mCurrentFile : mFiles) {
					if (mCurrentFile.getName().equals("LOST.DIR")) // 去除LOST.DIR
					{
						continue;
					}
					File mFile2 = new File(mCurrentFile.getPath()); // 获取路径内容

					if (mFile2.isFile()) { // 是文件

						String prefix = getExtensionName(mCurrentFile.getName()); // 获取文件名
																					// 后缀
						if (prefix.equals("mp3") || prefix.equals("MP3") || prefix.equals("wav") || prefix.equals("WAV")
								|| prefix.equals("wma") || prefix.equals("WMA")) {
							temp.add(mCurrentFile.getName());
						}
					}
				}
			}
		}

		return temp;
	}

	/*
	 * 获取文件扩展名
	 * 
	 */
	public String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {

			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	// 文件排序 按照
	private class UsernameComparator implements Comparator<String> {
		public int compare(String entity1, String entity2) {

			String str1 = "";
			String str2 = "";

			String str10 = "";
			String str11 = "";
			String str20 = "";
			String str21 = "";

			try {
				str1 = Pinyin4jUtils.converterToSpell(entity1);
				str2 = Pinyin4jUtils.converterToSpell(entity2);

				for (int i = 0; i < str1.length(); i++) {
					String str = str1.substring(i, i + 1);
					if (isNumber(str)) {
						str10 += str;
					} else {
						str11 += str1.substring(i);
						break;
					}
				}

				for (int i = 0; i < str2.length(); i++) {
					String str = str2.substring(i, i + 1);
					if (isNumber(str)) {
						str20 += str;
					} else {
						str21 += str2.substring(i);
						break;
					}
				}

				if (!TextUtils.isEmpty(str10) && !TextUtils.isEmpty(str20)) {
					float f1 = Float.parseFloat(str10);
					float f2 = Float.parseFloat(str20);

					if (f1 > f2) {
						return 1;
					} else if (f1 < f2) {
						return -1;
					} else {
						return str11.compareToIgnoreCase(str21);
					}
				} else {
					return str1.compareToIgnoreCase(str2);
				}
			} catch (Exception e) {
				e.printStackTrace();

				return str1.compareToIgnoreCase(str2);
			}
		}

		private boolean isNumber(String str) {
			if ("0".equals(str) || "1".equals(str) || "2".equals(str) || "3".equals(str) || "4".equals(str)
					|| "5".equals(str) || "6".equals(str) || "7".equals(str) || "8".equals(str) || "9".equals(str)
					|| ".".equals(str)) {
				return true;
			}

			return false;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Global.debug("\r\n[Alarm_SettingActivity] handleMessage == msg.what = " + msg.what);
			if (msg.what == Global.MSG_SETTING_BACK) { // 音乐播放结束消息
				goBack();
			} else if (msg.what == Global.MSG_SETTING_FINISH) {
				finish();
			}

			super.handleMessage(msg);
		}
	};

	// 返回上一界面
	private void goBack() {

		Intent intent = new Intent(); // 新建 INtent
		Bundle bundle = new Bundle(); // 新建 bundle

		if (gInterfaceFlag == Global.ALARM_INFO_INTERFACE) // 闹钟
		{
			if (gSelectID == Global.ALARM_SET_MUSIC) {
				bundle.putString("FILENAME", mTemp.get(getSelectItem())); // 进界面时的界面
			} else if (gSelectID == Global.ALARM_SET_TYPE || gSelectID == Global.ALARM_SET_ONOFF) {
				bundle.putInt("ID", getSelectItem());
			}
		} else if (gInterfaceFlag == Global.ANNIVERSARY_INFO_INTERFACE) { // 纪念日
			if (gSelectID == Global.ANNIVERSARY_SET_MUSIC) {
				bundle.putString("FILENAME", mTemp.get(getSelectItem())); // 进界面时的界面
			} else if (gSelectID == Global.ANNIVERSARY_SET_ONOFF) {
				bundle.putInt("ID", getSelectItem());
			}
		}
		Global.debug("\r\n [onKeyUp] ==== entern \r\n");
		Global.debug("\r\n [onKeyUp] ==gInterfaceFlag == " + gInterfaceFlag);
		Global.debug("\r\n [onKeyUp] ==gSelectID == " + gSelectID);
		bundle.putInt("FLAG", gInterfaceFlag); // 进界面时的界面
		bundle.putInt("SELECTID", gSelectID); // 进界面时的修改项
		intent.putExtras(bundle); // 参数传递
		setResult(Global.FLAG_CODE_SET_LIST, intent); // 返回界面

		finish();
	}
	
	// 等待TTS 发音结束 播放
	TtsCompletedListener mCompletedListener = new TtsCompletedListener() {

		@Override
		public void onCompleted(String arg0) {

			Global.debug("\r\n tts 播放结束========mVolFlag =");

			if(gSelectID == Global.ANNIVERSARY_SET_MUSIC || gSelectID == Global.ALARM_SET_MUSIC){
				if (mMenuList.size() <= 0){
					return;
				}
					
				String path = Alarmpublic.ALARM_PATH + getResources().getString(R.string.folder) + "/" + getSelectItemContent(); 
				myPlayer.startPlayback(myPlayer.playProgress(), path, true);
			}
			else{
				myPlayer.stopPlayback();
			}
		}

	};
}
