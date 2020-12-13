package util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.bruce.modulableapp.commonlib.BuildConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Author: Bruce
 * Email: zhelong0615@gmail.com
 * Date: 2015/10/10
 * Function: 获得设备相关信息
 */
public class AppUtil {

    public static boolean isRelease() {
        return "release".equals(BuildConfig.BUILD_TYPE);
    }

    public static String getInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        sb.append("\nDeviceId(MAC) = " + tm.getDeviceId());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        sb.append("\nVersionName = " + getVersionCode(context));
        sb.append("\nVersionCode = " + getVersionName(context));
        //        double[] lngLat = getLngLat(context);
        //        sb.append("\nLngLat = " + lngLat[0] + "," + lngLat[1]);
        sb.append("\nNetworkType = " + getNetworkType(context));
        sb.append("\nIP = " + getIPAddress(context));
        //        sb.append("\nBuild = " + getBuild());
        sb.append("\nFreeMem = " + getFreeMem(context));
        sb.append("\nTotalMem = " + getTotalMem(context));
        sb.append("\nCpuInfo = " + getCpuInfo());
        sb.append("\ngetCpuFreq = " + getCpuFreq());
        sb.append("\ngetMacInfo = " + getMacInfo());
        sb.append("\nchannelId = " + getChannelId(context));
        Log.e("info", sb.toString());
        return sb.toString();
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getDeviceId();
    }


    /**
     * 设备名称
     *
     * @return
     */
    public static String getDeviceName() {
        return Build.PRODUCT;
    }

    /**
     * 制造商
     *
     * @return
     */
    public static String getDeviceManufactory() {
        return Build.MANUFACTURER;
    }

    /**
     * 设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 国际化区域名称
     *
     * @param context
     * @return
     */
    public static String getInternationalCountryName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }


    public static String getIMEI(Context context) {
        return getDeviceId(context);
    }

    //    public static String getRequestId(Context context) {
    //    	return UUIDGenerator.getUUID();
    //    }

    public static String getIESI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getSubscriberId();
    }

    /**
     * 获得网络服务商名称
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    /**
     * 获得SIM卡序列号
     *
     * @param context
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return tm.getSimSerialNumber();
    }

    /**
     * 获得软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得软件版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    /**
     * 判断应用是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkBrowser(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获得屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenWH(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        int[] wh = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        wh[0] = metrics.widthPixels;
        wh[1] = metrics.heightPixels;
        return wh;
    }

    /**
     * 根据网络获得经纬度(获取不到)
     *
     * @param context
     * @return
     */
    //    public static double[] getLngLat(Context context) {
    //        String serviceString = Context.LOCATION_SERVICE;
    //        LocationManager locationManager = (LocationManager) context.getSystemService(serviceString);
    //        String provider = LocationManager.NETWORK_PROVIDER;
    //        Location location = locationManager.getLastKnownLocation(provider);
    //        double[] latLng = new double[2];
    //        if (location != null) {
    //            double lat = location.getLatitude();
    //            double lng = location.getLongitude();
    //            latLng[0] = lng;
    //            latLng[1] = lat;
    //        }
    //        return latLng;
    //    }

    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        String operatorName = "OTHER";
        if (operator != null) {
            if ("46000".equals(operator) || "46002".equals(operator)) {
                //                operatorName = "中国移动";
                operatorName = "CMCC";
            } else if ("46001".equals(operator)) {
                //                operatorName = "中国联通";
                operatorName = "CUCC";
            } else if ("46003".equals(operator)) {
                //                operatorName = "中国电信";
                operatorName = "CTC";
            }
        }
        return operatorName;
    }

    /**
     * 获得网络类型2G/3G/4G/Wifi
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String strSubTypeName = networkInfo.getSubtypeName();

                Log.e("best", "Network getSubtypeName : " + strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName) || "WCDMA".equalsIgnoreCase(strSubTypeName) || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = strSubTypeName;
                        }

                        break;
                }

                Log.e("best", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("best", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    /**
     * 获得IP方法一
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service != null ? wifi_service.getDhcpInfo() : null;
        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
        System.out.println("DHCP info gateway----->" + Formatter.formatIpAddress(dhcpInfo.gateway));
        System.out.println("DHCP info netmask----->" + Formatter.formatIpAddress(dhcpInfo.netmask));
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
        return Formatter.formatIpAddress(dhcpInfo.ipAddress);
    }

    /**
     * 获得IP方法二
     *
     * @param context
     * @return
     */
    public static String getIPAddress2(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        //判断wifi是否开启
        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = null;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
        }
        int ipAddress = 0;
        if (wifiInfo != null) {
            ipAddress = wifiInfo.getIpAddress();
        }
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    public static String getBuild() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nBuild.BOARD = " + Build.BOARD);
        sb.append("\nBuild.BOOTLOADER = " + Build.BOOTLOADER);
        sb.append("\nBuild.BRAND = " + Build.BRAND);
        sb.append("\nBuild.DEVICE = " + Build.DEVICE);
        sb.append("\nBuild.DISPLAY = " + Build.DISPLAY);
        sb.append("\nBuild.FINGERPRINT = " + Build.FINGERPRINT);
        //        sb.append("\nBuild.getRadioVersion = " + Build.getRadioVersion());
        sb.append("\nBuild.HARDWARE = " + Build.HARDWARE);
        sb.append("\nBuild.HOST = " + Build.HOST);
        sb.append("\nBuild.ID = " + Build.ID);
        sb.append("\nBuild.MANUFACTURER = " + Build.MANUFACTURER);
        sb.append("\nBuild.MODEL = " + Build.MODEL);
        sb.append("\nBuild.PRODUCT = " + Build.PRODUCT);
        sb.append("\nBuild.SERIAL = " + Build.SERIAL);
        sb.append("\nBuild.TAGS = " + Build.TAGS);
        sb.append("\nBuild.TYPE = " + Build.TYPE);
        //        sb.append("\nBuild.USER = " + Build.USER);
        sb.append("\nBuild.CPU_ABI = " + Build.CPU_ABI);
        sb.append("\nBuild.CPU_ABI2 = " + Build.CPU_ABI2);
        //        sb.append("\nBuild.SUPPORTED_64_BIT_ABIS = " + Build.SUPPORTED_64_BIT_ABIS);
        sb.append("\nBuild.TIME = " + Build.TIME);
        sb.append("\nBuild.VERSION.CODENAME = " + Build.VERSION.CODENAME);
        sb.append("\nBuild.VERSION.INCREMENTAL = " + Build.VERSION.INCREMENTAL);
        sb.append("\nBuild.VERSION.RELEASE = " + Build.VERSION.RELEASE);
        sb.append("\nBuild.VERSION.SDK = " + Build.VERSION.SDK);
        sb.append("\nBuild.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        sb.append("\nBuild.VERSION_CODES.BASE = " + Build.VERSION_CODES.BASE);

        return sb.toString();
    }

    private static final String TAG = AppUtil.class.getSimpleName();
    private static final String FILE_MEMORY = "/proc/meminfo";
    private static final String FILE_CPU = "/proc/cpuinfo";
    private static final String FILE_CPU_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    private static final String FILE_MAC_ADDRESS = "/sys/class/net/wlan0/address";

    /**
     * 获得可用内存（MB）
     *
     * @param context
     * @return
     */
    public static long getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        long free = info.availMem / 1024 / 1024;
        return free;
    }

    /**
     * 获得总内存大小
     *
     * @param context
     * @return
     */
    public static long getTotalMem(Context context) {
        try {
            FileReader fr = new FileReader(FILE_MEMORY);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            return Long.valueOf(array[1]) / 1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获得系统版本(例如5.1)
     *
     * @return
     */
    public static String getSysVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


//    /**
//     * 获取当前手机系统版本号
//     *
//     * @return  系统版本号
//     */
//    public static String getSystemVersion() {
//        return android.os.Build.VERSION.RELEASE;
//    }


    /**
     * 获得cpu型号
     *
     * @return
     */
    public static String getCpuModel() {
        return Build.HARDWARE;
    }

    /**
     * 获得CPU信息
     *
     * @return
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader(FILE_CPU);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
                Log.w(TAG, " ..... " + array[i]);
            }
            Log.w(TAG, text);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得CPU频率
     *
     * @return
     */
    public static String getCpuFreq() {
        try {
            FileReader fr = new FileReader(FILE_CPU_FREQ);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            return text;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得Mac地址
     *
     * @return
     */
    public static String getMacInfo() {
        try {
            FileReader fr = new FileReader(FILE_MAC_ADDRESS);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            text = text.replaceAll(":", "");
            Log.e("mac", text);
            return text;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
        //        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        //        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //        macAddress = wifiInfo.getMacAddress();
        //        return macAddress;

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "020000000002";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString().replaceAll(":", "");
        } catch (SocketException e) {
            e.printStackTrace();
            return "020000000002";
        }
        if (TextUtils.isEmpty(macAddress)) {
            return macAddress;
        } else {
            return macAddress.toUpperCase();
        }
    }


    /**
     * 设备名称
     *
     * @return
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public static String getModelName() {
        return Build.MODEL;
    }

    /**
     * 制造商
     *
     * @return
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceType() {
        return Build.DISPLAY;
    }

    public static String getOsType() {
        return "1";
    }

    /**
     * 获得CPU型号
     *
     * @return
     */
    public static String getHardware() {
        return Build.HARDWARE;
    }

    /**
     * 获得uuid
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        return Md5Util.md5(UUIDGenerator.exLowerCase(getMacInfo() + getDeviceId(context)));
    }


    public static String getApplicationMetaInt(Context context, String name) {
        ApplicationInfo appInfo = null;
        String value = "未知";
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            //            e.printStackTrace();
        }
        if (appInfo != null) {
            try {
                //                value = appInfo.metaData.getString(name);
                value = appInfo.metaData.getInt(name) + "";
            } catch (Exception e) {
                return value;
            }
        }
        return value;
    }

    public static String getApplicationMetaString(Context context, String name) {
        ApplicationInfo appInfo = null;
        String value = "未知";
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            //            e.printStackTrace();
        }
        if (appInfo != null) {
            try {
                //                value = appInfo.metaData.getString(name);
                value = appInfo.metaData.getString(name);
            } catch (Exception e) {
                return value;
            }
        }
        return value;
    }

    /**
     * 获得渠道ID
     *
     * @param context
     * @return
     */
    public static String getChannelId(Context context) {
        return getApplicationMetaString(context, "BUGLY_APP_CHANNEL");
    }

    /**
     * 判断APP是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
    }

    /**
     *         * 用来判断服务是否运行.
     *         * @param context
     *         * @param className 判断的服务名字
     *         * @return true 在运行 false 不在运行
     *         
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (serviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            String serviceName = serviceList.get(i).service.getClassName();
            if (serviceName.equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static boolean isTopActivity(Context context, String activity) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName().contains(activity);
    }

    public static void doStartApplicationWithPackageName(Context context, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    public static void setLight(Activity context, int lightes) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(lightes) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    public static void restartApp(Activity activity, int delayMillis) {
        Intent intent = activity.getPackageManager()
                .getLaunchIntentForPackage(activity.getApplication().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(activity.getApplicationContext()
                , 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delayMillis, restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

}
