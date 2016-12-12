package com.sunteam.manage.utils;

import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class Global {
	public static final String TAG = "zbc";

	public static final int  MAIN_INTERFACE = 1;   // 主界面
	public static final int MENU_INTERFACE = 2;		// menu界面
	
	public static final int MENU_INTERFACE_FLAG = 0x100;		// menu界面
	
	public static int MAX_RANK = 128;// 最大级数

	public static final String ROOT_PATH = Environment.getExternalStorageDirectory().toString();// java.io.File.separator;
	public static final String USER_PATH = "/mnt/extsd"; // 外部 sd卡
	// private final String USB_PATH =
	// Environment.getExternalStorageDirectory().toString();;
	public static final String USB_PATH = "/mnt/usbhost1"; // 优盘
	public static final String ROOT_PATH_FLAG = "/root";

	//private boolean gMeunViewFlag = false; // menu 界面标志

	public static final int COPY_ID = 0; // 复制
	public static final int CUT_ID = 1; // 剪切
	public static final int DEl_ID = 2; // 删除
	public static final int PASTE_ID = 3; // 粘贴
	
	public static String gCopyPath_src = null; // 复制分原始目录

	public static String gCopyPath_desk = null; // 复制到目录
	public static String gCopyName = null; // 复制文件名
	
	public static String gPastName = null; // 粘贴文件名

	public static boolean gCopyFlag = false;
	public static boolean gCutFlag = false;
	public static int gtempID = 0;   // 记录选中的select
	
	// 全局保存文件名
	public static ArrayList<String> gFileName = null;
	// 全局保存路径
	public static ArrayList<String> gFilePaths = null;
	public static ArrayList<String> gPath = null;
	public static ArrayList<String> gName = null;
	public static int gPathNum = 0;
	
	public static void debug(String s) {
		Log.d(TAG, s);
	}
}
