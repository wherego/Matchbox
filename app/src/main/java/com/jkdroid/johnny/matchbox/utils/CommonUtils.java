package com.jkdroid.johnny.matchbox.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.jkdroid.johnny.matchbox.listener.AlertDialogListener;
import com.jkdroid.johnny.matchbox.listener.InfoDialogListener;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static void sendHandlerMessage(final Handler handler, final int id, final Object obj) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message message = Message.obtain();
                message.what = id;
                if (null != obj) {
                    message.obj = obj;
                }
                handler.sendMessage(message);
            }
        }.start();
    }

    public static void showProgerss(Context context, ProgressDialog progressDialog, String title, String message, int progress) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (null != title) {
            progressDialog.setTitle(title);
        }
        if (null != message) {
            progressDialog.setMessage(message);
        }
        progressDialog.setMax(100);
        progressDialog.setProgress(progress);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void showRoundProgerss(Context context, ProgressDialog progressDialog, String title, String message) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (null != title) {
            progressDialog.setTitle(title);
        }
        if (null != message) {
            progressDialog.setMessage(message);
        }
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 信息提示框
     *
     * @param context     上下文
     * @param message     提示信息
     * @param positiveStr 按钮文本
     */
    public static void showInfoDialog(Context context, String message, String positiveStr, final InfoDialogListener infoDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != infoDialogListener) {
                    infoDialogListener.onInfoPositive();
                }
            }
        });
        builder.show();
    }

    public static void showAlertDialog(Context context, String message, String positiveMsg, String negativeMsg, final AlertDialogListener alertDialogListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogListener.onPositive();
            }
        });
        builder.setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogListener.onNegative();
            }
        });
        builder.create().show();
    }

    public static void showToast(final Activity context, final String msg) {
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void saveImageToGallery(Activity context, Bitmap bitmap, String path) {
        // 首先保存图片
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveImageToGallery(context, file);
    }

    public static void saveImageToGallery(Activity context, File file) {
        // 首先保存图片
        if (file.exists()) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        } else {
            CommonUtils.showToast(context, "图片不存在");
        }
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置正方向图片(本地)
     *
     * @param imageView
     * @param path
     */
    public static void setPositivePicture(ImageView imageView, String path) {
        int degree = readPictureDegree(path);
        BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
        opts.inSampleSize = 2;
        Bitmap cbitmap = BitmapFactory.decodeFile(path, opts);
        Bitmap newbitmap = rotaingImageView(degree, cbitmap);//根据角度旋转图片
        imageView.setImageBitmap(newbitmap);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //TODO 导入Glide
//    public static void glideDisplay(Context context, ImageView imageView, String url) {
//        if (context instanceof Activity) {
//            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//        } else if (context instanceof FragmentActivity) {
//            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//        } else {
//            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//        }
//    }

//    public static void glideDisplay(Fragment fragment, ImageView imageView, String url) {
//        Glide.with(fragment).load(url)
//                .error(R.mipmap.ic_error)//默认错误图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//    }

    public static String takePhoto(Fragment fragment) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        File cameraFile = new File(DCIM, "Camera");
        if (!cameraFile.exists()) {
            cameraFile.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(cameraFile, fileName);
        String photoPath = file.getPath();
        Uri cameraUri = Uri.fromFile(file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        fragment.startActivityForResult(cameraIntent, 1);
        return photoPath;
    }
    public static String takePhoto(Activity activity) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        File cameraFile = new File(DCIM, "Camera");
        if (!cameraFile.exists()) {
            cameraFile.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(cameraFile, fileName);
        String photoPath = file.getPath();
        Uri cameraUri = Uri.fromFile(file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        activity.startActivityForResult(cameraIntent, 1);
        return photoPath;
    }

    public static void getContent(Fragment fragment) {
        //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        fragment.startActivityForResult(intent, 0);
    }

    public static void getContent(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, 0);
    }

    public static String getUsernameByJid(String jid) {
        return jid.split("@")[0];
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception ex) {

        }
        return 1.0f;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static boolean isPhoneNumber(String mobiles) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6-8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(mobiles);
        Log.d(TAG, "校验手机号码：" + matcher.matches());
        return matcher.matches();
    }

    public static boolean isCorrectPwd(String newPwd) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]{6,20}$");
        Matcher matcher = pattern.matcher(newPwd);
        Log.d(TAG, "校验密码格式：" + matcher.matches());
        return matcher.matches();
    }

    public static boolean isIDCard(String idcard) {
        Pattern pattern = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
        Matcher matcher = pattern.matcher(idcard);
        Log.d(TAG, "校验身份证：" + matcher.matches());
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
        Matcher matcher = pattern.matcher(email);
        Log.d(TAG, "校验邮箱：" + matcher.matches());
        return matcher.matches();
    }

    public static Long dateTotimestamp(String time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(time);
            Long timeStemp = date.getTime();
            return timeStemp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timestampTodate(long mill) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date(mill));
        return time;
    }

    public static String getLocalDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getDateByTimestamp(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getYearByTimestamp(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(time));
    }

    public static String getMonthByTimestamp(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(new Date(time));
    }

    public static String getDayByTimestamp(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(new Date(time));
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;
            default:
                // result = temp + "天前 ";
                result = timestampTodate(timesamp);
                break;
        }
        return result;
    }

    /**
     * 获取Android唯一码
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String androidID = teleManager.getDeviceId();
        if (androidID == null || androidID.length() == 0) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                androidID = wifiManager.getConnectionInfo().getMacAddress();
            }
        }
        return androidID;
    }
///TODO 导入Gson
//    public static <T> T json2Bean(String result, Class<T> clazz) {
//        Gson gson = new Gson();
//        T t = gson.fromJson(result.trim(), clazz);
//        return t;
//    }
//
//    public static <T> T json2List(String result, Type type) {
//        Gson gson = new Gson();
//        T t = gson.fromJson(result.trim(), type);
//        return t;
//    }
//
//    public static String toJson(Object object) {
//        Gson gson = new Gson();
//        return gson.toJson(object);
//    }

    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivity) {
            Log.d(TAG, "connectivity:" + connectivity);
        } else {
          if(Build.VERSION.SDK_INT>=21){
              Network[] allNetworks = connectivity.getAllNetworks();
              if(null!=allNetworks && allNetworks.length>0){
                  for(Network network:allNetworks) {
                      NetworkInfo networkInfo = connectivity.getNetworkInfo(network);
                      if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                          if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                              Log.d(TAG, "Wifi已连接");
                              WifiManager wifiManager = (WifiManager) context
                                      .getSystemService(Context.WIFI_SERVICE);
                              WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                              int rssi = wifiInfo.getRssi();
                              if (rssi > 0 && rssi < -50) {
                                  Toast.makeText(context, "wifi信号较差,使用起来可能不够流畅！", Toast.LENGTH_SHORT).show();
                              }
                              return 1;
                          } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                              String extraInfo = networkInfo.getExtraInfo();
                              if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                  return 2;
                              }
                              return 3;
                          }
                      }else {
                          Log.d(TAG, "NetworkState:" +networkInfo.getState());
                      }
                  }
              }else {
                  Log.d(TAG, "allNetworks:" +(allNetworks==null?-1:allNetworks.length));
              }
          }else {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (null != info && info.length>0) {
                  for (NetworkInfo networkInfo :info) {
                      if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                          if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                              Log.d(TAG, "Wifi已连接");
                              WifiManager wifiManager = (WifiManager) context
                                      .getSystemService(Context.WIFI_SERVICE);
                              WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                              int rssi = wifiInfo.getRssi();
                              if (rssi > 0 && rssi < -50) {
                                  Toast.makeText(context, "wifi信号较差,使用起来可能不够流畅！", Toast.LENGTH_SHORT).show();
                              }
                              return 1;
                          } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                              String extraInfo = networkInfo.getExtraInfo();
                              if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                  return 2;
                              }
                              return 3;
                          }
                      }else {
                          Log.d(TAG, "NetworkState:" +networkInfo.getState());
                      }
                  }
              }else {
                  Log.d(TAG, "allNetworkInfos:" +(info==null?-1:info.length));
              }
          }
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int picScreenHeight = 0;
        picScreenHeight = (screenWidth * picHeight) / picWidth;
        return picScreenHeight;
    }

    private static Drawable createDrawable(Drawable d, Paint p) {
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap b = bd.getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
                bd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，
        return new BitmapDrawable(bitmap);
    }

    /**
     * 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果
     */
    public static StateListDrawable createSLD(Context context, Drawable drawable) {
        StateListDrawable bg = new StateListDrawable();
        int brightness = 50 - 127;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Drawable normal = drawable;
        Drawable pressed = createDrawable(drawable, paint);
        bg.addState(new int[]{android.R.attr.state_pressed,}, pressed);
        bg.addState(new int[]{android.R.attr.state_focused,}, pressed);
        bg.addState(new int[]{android.R.attr.state_selected}, pressed);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
        Bitmap image = null;
        AssetManager am = ct.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String getUploadtime(long created) {
        StringBuffer when = new StringBuffer();
        int difference_seconds;
        int difference_minutes;
        int difference_hours;
        int difference_days;
        int difference_months;
        long curTime = System.currentTimeMillis();
        difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
        if (difference_months > 0) {
            when.append(difference_months + "月");
        }
        difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
        if (difference_days > 0) {
            when.append(difference_days + "天");
        }
        difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
        if (difference_hours > 0) {
            when.append(difference_hours + "小时");
        }
        difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
        if (difference_minutes > 0) {
            when.append(difference_minutes + "分钟");
        }
        difference_seconds = (int) ((curTime % 60) - (created % 60));
        if (difference_seconds > 0) {
            when.append(difference_seconds + "秒");
        }
        return when.append("前").toString();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 检查Wifi连接状况
     *
     * @param context Wifi信号强弱判断： 得到的值是一个0到-100的区间值，是一个int型数据，
     *                其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差， 有可能连接不上或者掉线
     */
    public static boolean checkWifiConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);// 检测手机无线连接状况
        NetworkInfo networkInfo = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isAvailable()) {
            Log.d(TAG, "Wifi已连接");
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int rssi = wifiInfo.getRssi();
            if (rssi > 0 && rssi < -50) {
                Toast.makeText(context, "wifi信号较差,使用起来可能不够流畅！", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            Log.d(TAG, "Wifi连接失败");
            return false;
        }
    }

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            Log.d(TAG, "网络状态：" + networkInfo + " / " + networkInfo.isAvailable());
            return true;
        } else {
            Log.d(TAG, "网络状态：" + false);
            return false;
        }
    }

    /**
     * 将给定的输入流内容输出到制定的输出流中
     *
     * @throws IOException
     */
    public static void getInputStream(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(in);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        byte[] buf = new byte[1024];  //缓存
        int len = -1;
        while ((len = bin.read(buf)) != -1) {
            bout.write(buf, 0, len);   //如果输入流还没有读到结尾，则把读到的内容输出到输出流中
        }
        bout.flush();
    }

    /**
     * 替换地址中的特殊字符
     *
     * @param url
     * @return
     */
    public static String replaceUrlSpecialChar(String url) {
        String new_url = url.replace("%3A", ":").replace("%2F", "/")
                .replace("%3F", "?").replace("%3D", "=")
                .replace("%26", "&").replace("%2C", ",")
                .replace("%20", " ").replace("+", "%20")
                .replace("%2B", "+").replace("%23", "#")
                .replace("#", "%23");
        Log.d(TAG, "查找好友URL：" + new_url);
        return new_url;
    }
}
