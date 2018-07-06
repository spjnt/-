package bar.barcode.util;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bar.barcode.App;

public class Utils {
    private static String ip;
    private static String biaodian;

    public static void goToNextUI(Class cls) {
        Intent intent = new Intent(App.getContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        App.getContext().startActivity(intent);

    }

    public static void Logs(boolean bol, String tag, String mes) {
        if (bol == true) {
            Log.e(tag, mes);
        }
    }

    public static String chageBigToLow(String string) {
        if (!TextUtils.isEmpty(string)) {
            if (string.contains("。")) {
                String lowBiao1 = string.replace("。", ".");
                biaodian = lowBiao1;

            } else if (string.contains("：")) {
                String replace = string.replace("：", ":");
                biaodian = replace;
            } else if (!string.contains("。") && !string.contains("：")) {
                biaodian = string;
            } else if (string.contains("。") && string.contains("：")) {
                String replace = string.replace("。", ".").replace("：", ":");
                biaodian = replace;
            }
        }
        return biaodian.trim();
    }

    public static String ifStartEndWith(String string) {
        if (!string.startsWith("http://")) {
            ip = "http://" + string;
        }
        if (!string.endsWith("/")) {
            ip = string + "/";
        }
        if (!string.startsWith("http://") && !string.endsWith("/")) {
            ip = "http://" + string + "/";
        }
        if (string.startsWith("http://") && string.endsWith("/")) {
            ip = string;
        }
        if (TextUtils.isEmpty(string)) {
            ip = "";
        }
        return ip;
    }


}
