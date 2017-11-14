package com.jyx.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2015/12/13.
 */
    public final class SystemTool {
    public SystemTool() {
    }

    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    public static String getPhoneIMEI(Context cxt) {
        //"phone"
        TelephonyManager tm = (TelephonyManager) cxt.getSystemService( Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent("android.intent.action.SENDTO", smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);


    }

    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;
    }

    public static boolean checkOnlyWifi(Context context) {


        return PreferenceHelper.readBoolean(context, "kjframe_preference", "only_wifi") ? isWiFi(context) : true;
    }

    public static boolean isWiFi(Context cxt) {
        //"connectivity"
        ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo.State state = cm.getNetworkInfo(1).getState();
        return NetworkInfo.State.CONNECTED == state;
    }

    public static void hideKeyBoard(Activity aty) {
        //"input_method"
        ((InputMethodManager) aty.getSystemService(Context.INPUT_METHOD_SERVICE )).hideSoftInputFromWindow(aty.getCurrentFocus().getWindowToken(), 2);
    }

    public static boolean isBackground(Context context) {
        //"activity"
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE );
        List appProcesses = activityManager.getRunningAppProcesses();
        Iterator var4 = appProcesses.iterator();

        while (var4.hasNext()) {
            ActivityManager.RunningAppProcessInfo appProcess = (ActivityManager.RunningAppProcessInfo) var4.next();
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == 400) {
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static String getAppVersion(Context context) {
        String version = "0";

        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return version;
        } catch (PackageManager.NameNotFoundException var3) {

            return "";
//            throw new Exception(SystemTool.class.getName() + "the application not found");
        }
    }

    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent("android.intent.action.MAIN");
        mHomeIntent.addCategory("android.intent.category.HOME");
        mHomeIntent.addFlags(270532608);
        context.startActivity(mHomeIntent);
    }

    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo e = context.getPackageManager().getPackageInfo(pkgName, 64);
            return hexdigest(e.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException var3) {
            return "";
//            throw new KJException(SystemTool.class.getName() + "the " + pkgName + "\'s application not found");
        }
    }

    private static String hexdigest(byte[] paramArrayOfByte) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;

            for (int j = 0; i < 16; ++j) {
                byte k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[15 & k >>> 4];
                ++j;
                arrayOfChar[j] = hexDigits[k & 15];
                ++i;
            }

            return new String(arrayOfChar);
        } catch (Exception var8) {
            return "";
        }
    }

    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return (int) (mi.availMem / 1048576L);
    }

    public static int gc(Context cxt) {
        long i = (long) getDeviceUsableMemory(cxt);
        int count = 0;
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List serviceList = am.getRunningServices(100);
        if (serviceList != null) {
            Iterator process = serviceList.iterator();

            while (process.hasNext()) {
                ActivityManager.RunningServiceInfo processList = (ActivityManager.RunningServiceInfo) process.next();
                if (processList.pid != Process.myPid()) {
                    try {
                        Process.killProcess(processList.pid);
                        ++count;
                    } catch (Exception var16) {
                        var16.getStackTrace();
                    }
                }
            }
        }

        List var17 = am.getRunningAppProcesses();
        if (var17 != null) {
            Iterator e = var17.iterator();

            label44:
            while (true) {
                ActivityManager.RunningAppProcessInfo var18;
                do {
                    if (!e.hasNext()) {
                        break label44;
                    }

                    var18 = (ActivityManager.RunningAppProcessInfo) e.next();
                } while (var18.importance <= 200);

                String[] pkgList = var18.pkgList;
                String[] var13 = pkgList;
                int var12 = pkgList.length;

                for (int var11 = 0; var11 < var12; ++var11) {
                    String pkgName = var13[var11];
//                    KJLoger.debug("======正在杀死包名：" + pkgName);

                    try {
                        am.killBackgroundProcesses(pkgName);
                        ++count;
                    } catch (Exception var15) {
                        var15.getStackTrace();
                    }
                }
            }
        }

//        KJLoger.debug("清理了" + ((long) getDeviceUsableMemory(cxt) - i) + "M内存");
        return count;
    }

}
