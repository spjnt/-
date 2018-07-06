package bar.barcode.util;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bar.barcode.R;

public class TxtUtil {

    private static Matcher m;

    public static String Txt(InputStream open, int serial) {
        List newList = new ArrayList<String>();
        int count = 0;//初始化 key值
        try {
            InputStreamReader isr = new InputStreamReader(open);
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if (!"".equals(lineTxt)) {
                    String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                    newList.add(count, reds);
                    count++;
                }
            }
            String s = (String) newList.get((serial - 1));
            String regEx = "[^0-9]";//正则表达式 ,提取纯数字
            Pattern p = Pattern.compile(regEx);
            m = p.matcher(s);
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m.replaceAll("");
    }

    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            TextView textView = new TextView(context);
            textView.setText("可爱的喵喵");
            margin(textView, 10, 0, 0, 0);
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.hide_psw);
            //组合文本加图片,可以设置线性布局
            LinearLayout layout = new LinearLayout(context);
            layout.setBackgroundColor(context.getResources().getColor(R.color.red));
            layout.setOrientation(LinearLayout.HORIZONTAL);//设置LinearLayout垂直

            layout.setGravity(Gravity.CENTER);//设置LinearLayout里面内容中心分布
            layout.addView(imageView);//先添加image
            layout.addView(textView);//再添加text

            toast.setView(layout);//只需要把layout设置进入Toast
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);

        }
        toast.show();
    }

    public static void margin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

}
