package com.jkdroid.johnny.matchbox.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by mwqi on 2014/7/11.
 */
public class BaseApplication extends Application {
	/** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
	private static BaseApplication mInstance;
	/** 主线程ID */
	private static int mMainThreadId = -1;
	/** 主线程ID */
	private static Thread mMainThread;
	/** 主线程Handler */
	private static Handler mMainThreadHandler;
	/** 主线程Looper */
	private static Looper mMainLooper;

	@Override
	public void onCreate() {
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		mInstance = this;
		super.onCreate();
		// 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
//		CrashReport.initCrashReport(this, "900010189", false);
//
//		//GalleryFinal配置
//		//设置主题
//		ThemeConfig theme = ThemeConfig.DEFAULT;
//        //配置功能
//        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableCamera(true)
//                .setEnableEdit(false)//启用编辑
//                .setEnableCrop(false)//启用剪裁
//                .setEnableRotate(false)//启用旋转
//                .setCropSquare(true)//剪裁成正方形
//                .setEnablePreview(true)//启用照相预览
////		        .setMutiSelectMaxSize(8)//设置多选最大张数
//                .build();
//        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
//                .setDebug(BuildConfig.DEBUG)
//                .setFunctionConfig(functionConfig)
////				.setTakePhotoFolder()//设置拍照保存目录，默认是/sdcard/DICM/GalleryFinal/
////                .setPauseOnScrollListener(new GlidePauseOnScrollListener(true, true))
//				.setNoAnimcation(true)
//				.build();
//        GalleryFinal.init(coreConfig);
	}

	public static BaseApplication getApplication() {
		return mInstance;
	}

	/** 获取主线程ID */
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	/** 获取主线程 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程的handler */
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	/** 获取主线程的looper */
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}
}
