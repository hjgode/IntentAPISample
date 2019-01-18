package sample.honeywell.com.intentapisample;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public final class SystemPropertyAccess {
    private static final String TAG = SystemPropertyAccess.class.getSimpleName();

    public static String getProperty(String name, String defaultValue) {
        try {
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            String result = (String) SystemProperties.getMethod("get", new Class[]{String.class, String.class}).invoke(SystemProperties, new Object[]{name, defaultValue});
            if (result.equals("")) {
                return "Not Supported";
            }
            return result;
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to get system property", e);
            return null;
        }
    }

    public static void setProperty(String name, String value) {
        try {
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            SystemProperties.getMethod("set", new Class[]{String.class, String.class}).invoke(SystemProperties, new Object[]{name, value});
        } catch (Exception e) {
            Log.wtf(TAG, "Failed to set system property", e);
        }
    }

    public static String getHSMVersionInfo() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < PROPERTY_KEYWORDS.length; i++) {
            result.append(PROPERTY_KEYWORDS[i][1] + ": " + SystemPropertyAccess.getProperty(PROPERTY_KEYWORDS[i][0], null) + "\n");
        }
        return result.toString();
    }

    public static final String[][] PROPERTY_KEYWORDS;

    static {
        String[][] r0 = new String[30][];
        r0[0] = new String[]{"ro.hsm.assembly.date", "ID_FINAL_ASSEMBLY_DATE"};
        r0[1] = new String[]{"ro.hsm.asset.num", "ID_ASSET_NUMBER"};
        r0[2] = new String[]{"ro.hsm.bt.addr", "ID_BLUETOOTH_DEVICE_ADDRESS"};
        r0[3] = new String[]{"ro.hsm.cal.accel.zero_offset", "ID_CAL_ACCEL_ZERO_OFFSET"};
        r0[4] = new String[]{"ro.hsm.cal.gyro.zero_offset", "ID_CAL_GYRO_ZERO_OFFSET"};
        r0[5] = new String[]{"ro.hsm.calibration.accelerator", "ID_CALIBRATION_ACCELERATOR"};
        r0[6] = new String[]{"ro.hsm.calibration.gyro", "ID_CALIBRATION_GYRO"};
        r0[7] = new String[]{"ro.hsm.calibration.magnet", "ID_CALIBRATION_MAGNET"};
        r0[8] = new String[]{"ro.hsm.camera.enable_stamp", "ID_CAMERA_ENABLE_STAMP"};
        r0[9] = new String[]{"ro.hsm.extconf.num", "EXTCONFIG_NUM"};
        r0[10] = new String[]{"ro.hsm.extpart.num", "EXTPART_NUM"};
        r0[11] = new String[]{"ro.hsm.extserial.num", "EXTSERIAL_NUM"};
        r0[12] = new String[]{"ro.hsm.file.id", "FILE_ID"};
        r0[13] = new String[]{"ro.hsm.hw.has.gps", "HW_GPS"};
        r0[14] = new String[]{"ro.hsm.hw.rev", "HW_REV"};
        r0[15] = new String[]{"ro.hsm.mfg.ver", "MFG_VER"};
        r0[16] = new String[]{"ro.hsm.mm.feature", "MM_FEATURE"};
        r0[17] = new String[]{"ro.hsm.odm.num", "ODM_NUM"};
        r0[18] = new String[]{"ro.hsm.reset.reason", "RESET_REASON"};
        r0[19] = new String[]{"ro.hsm.imei.num", "ID_IMEI_NUMBER"};
        r0[20] = new String[]{"ro.hsm.meid.num", "ID_ME_NUMBER"};
        r0[21] = new String[]{"ro.hsm.model.num", "ID_MODEL_NUMBER"};
        r0[22] = new String[]{"ro.hsm.extserial.num", "ID_EX_SERIAL_NUMBER"};
        r0[23] = new String[]{"ro.hsm.extpart.num", "ID_EX_PART_NUMBER"};
        r0[24] = new String[]{"ro.hsm.extconf.num", "ID_EX_CONFIGURATION_NUMBER"};
        r0[25] = new String[]{"ro.hon.touch.ver.hw", "TOUCHSCREEN_HARDWARE_VER"};
        r0[26] = new String[]{"ro.hon.touch.ver.fw", "TOUCHSCREEN_FIRMWARE_VER"};
        r0[27] = new String[]{"ro.hon.touch.config", "TOUCHSCREEN_CONFIGURATION_VER"};
        r0[28] = new String[]{"ro.hsm.wireless.feature", "WIRELESS_FEATURE"};
        r0[29] = new String[]{"ro.hsm.wlan.addr", "ID_WLAN_MAC_ADDRESS"};
        PROPERTY_KEYWORDS = r0;
    }

    public static final String PKG_DCS_PREFIX = "com.intermec";
    public static final String PKG_DEMOS_PREFIX = "com.honeywell.demos";
    public static final String PKG_HONEYWELL_PREFIX = "com.honeywell";
    public static final String PKG_INTERMEC_PREFIX = "com.intermec";
    public static final String PKG_LICENSE_SERVICE_PREFIX = "com.honeywell.licenseservice";
    public static final String PKG_SYSTEM_TOOL_PREFIX = "com.honeywell.systemtools";
    public static final String PKG_TOOLS_PREFIX = "com.honeywell.tools";
    public static final String PKG_TOOL_SIP_PREFIX = "com.android.hsm";

    public static String getCommonESversion(Context context){
        String s="no EZConfig found";
        List<PackageInfo> pkgInfo = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pkgInfo.size(); i++) {
            PackageInfo info = (PackageInfo) pkgInfo.get(i);
            if (info.packageName.contains("com.honeywell.tools")) {
                if(info.packageName.endsWith("ezconfig")){
                    s = info.versionName;
                }
            }
        }
        return s;
    }
    public static String getPkgInfo(Context context) {
        StringBuffer result = new StringBuffer();
        StringBuffer demosInfo = new StringBuffer().append("demosInfo\n");
        StringBuffer toolsInfo = new StringBuffer().append("toolsInfo\n");
        StringBuffer appsInfo = new StringBuffer().append("appsInfo\n");
        List<PackageInfo> pkgInfo = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pkgInfo.size(); i++) {
            PackageInfo info = (PackageInfo) pkgInfo.get(i);
            //Log.e("package name", info.packageName);
            if (info.packageName.contains(PKG_SYSTEM_TOOL_PREFIX)) {
                toolsInfo.append(getAppName(info.packageName, PKG_SYSTEM_TOOL_PREFIX.length() + 1) + ": " + info.versionName + "\n");
            } else if (info.packageName.contains("com.honeywell.tools")) {
                String sippacakge = "com.honeywell.tools.nosip";
                String carrierpackage = "com.honeywell.tools.carrierselection";
                if (!(info.packageName.equals("com.honeywell.tools.reboot") || info.packageName.equals(carrierpackage) || info.packageName.equals(sippacakge))) {
                    toolsInfo.append(getAppName(info.packageName, "com.honeywell.tools".length() + 1) + ": " + info.versionName + "\n");
                }
            } else if (info.packageName.contains(PKG_TOOL_SIP_PREFIX)) {
                toolsInfo.append(getAppName(info.packageName, PKG_TOOL_SIP_PREFIX.length() + 1) + ": " + info.versionName + "\n");
            } else if (info.packageName.contains("com.honeywell.demos")) {
                demosInfo.append(getAppName(info.packageName, "com.honeywell.demos".length() + 1) + ": " + info.versionName + "\n");
            } else if (info.packageName.equals(PKG_LICENSE_SERVICE_PREFIX)) {
                toolsInfo.append(getAppName(info.packageName, PKG_HONEYWELL_PREFIX.length() + 1) + ": " + info.versionName + "\n");
            } else if (info.packageName.startsWith("com.intermec")) {
                toolsInfo.append(getAppName(info.packageName, "com.intermec".length() + 1) + ": " + info.versionName + "\n");
            } else {
                appsInfo.append(info.packageName + ": " + info.versionName + "\n");
            }
        }
        result.append("\n\nPOWER TOOLS AND DEMOS INFO\n");
        result.append("-------------------------\n");
        result.append(toolsInfo);
        result.append(demosInfo);
        return result.toString();
    }

    private static String getAppName(String pkgName, int subIndex) {
        if (pkgName == null || pkgName.length() == 0) {
            return "null";
        }
        return pkgName.substring(subIndex).toUpperCase(Locale.ENGLISH);
    }
    public static String getServicePack() {
        if (Build.DISPLAY.contains(".SP")) {
            return Build.DISPLAY;
        }
        return "No Service Pack";
    }

    public static String getBuilds(){

        String sV ="\n\nSYSTEM INFO\n" + "-------------------------\nPRODUCT: " + Build.PRODUCT + "\nBRAND: " + Build.BRAND + "\nSERIAL NUMBER: " + Build.SERIAL +
                "\nMODEL: " + Build.MODEL + "\nTYPE: " + Build.TYPE + "\nCPU_ABI: " + Build.CPU_ABI + "\nBUILD NUMBER: " + Build.DISPLAY + "\nINCREMENTAL: " + Build.VERSION.INCREMENTAL + "\nSERVICE_PACK: " +
                getServicePack() + "\nRELEASE: " + Build.VERSION.RELEASE + "\n";
        return sV;
    }
}