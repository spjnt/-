package bar.barcode.util;


import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import bar.barcode.R;

public class ToastUtil {



    private static Toast toast;

    /*
     * 默认显示图片,使用设定背景
     * */
    public static void showToast(Context context, String content, Showstate showstate) {
        if (toast == null) {
            toast = new Toast(context);
            LinearLayout layout = getLayout(context, content, true, showstate);
            toast.setView(layout);//只需要把layout设置进入Toast

        } else {
            LinearLayout layout = getLayout(context, content, true, showstate);
            toast.setView(layout);//只需要把layout设置进入Toast
        }
        toast.show();
    }

    /*
     * 是否显示图片
     * */
    public static void showToast(Context context, String content, boolean showImage, Showstate showstate) {
        if (toast == null) {
            toast = new Toast(context);
            LinearLayout layout = getLayout(context, content, showImage, showstate);
            toast.setView(layout);//只需要把layout设置进入Toast

        } else {
            LinearLayout layout = getLayout(context, content, showImage, showstate);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);//只需要把layout设置进入Toast
        }
        toast.show();
    }

    /*
     * 支持自定义背景,是否显示图片
     * */
    public static void showToast(Context context, String content, boolean showImage, Showstate showstate, int drawable) {
        if (toast == null) {
            toast = new Toast(context);
            LinearLayout layout = getLayout(context, content, showImage, showstate, drawable);
            toast.setView(layout);//只需要把layout设置进入Toast

        } else {
            LinearLayout layout = getLayout(context, content, showImage, showstate, drawable);
            toast.setView(layout);//只需要把layout设置进入Toast
        }
        toast.show();
    }

    private static LinearLayout getLayout(Context context, String content, boolean showImage, Showstate showstate) {

        TextView textView = getText(context, content);
        ImageView imageView = getImage(context, showImage, showstate);

        LinearLayout linear = getLinear(context, showstate);
        if (showImage) {
            linear.addView(imageView);//先添加image
        }
        linear.addView(textView);//再添加text
        return linear;
    }

    private static LinearLayout getLayout(Context context, String content, boolean showImage, Showstate showstate, int drawable) {

        TextView textView = getText(context, content);
        ImageView imageView = getImage(context, showImage, showstate);

        LinearLayout linear = getLinear(context, drawable);
        if (showImage) {
            linear.addView(imageView);//先添加image
        }
        linear.addView(textView);//再添加text
        return linear;
    }

    /*
     * 自定义布局
     * */
    private static LinearLayout getLinear(Context context, int drawable) {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackground(context.getResources().getDrawable(drawable));
        layout.setOrientation(LinearLayout.HORIZONTAL);//设置LinearLayout垂直
        layout.setPadding(20, 20, 20, 20);
        layout.setGravity(Gravity.CENTER_VERTICAL);//设置LinearLayout里面内容中心分布
        return layout;
    }

    /*
     * 使用原始布局
     * */

    private static LinearLayout getLinear(Context context, Showstate showstate) {
        LinearLayout layout = new LinearLayout(context);
        switch (showstate) {
            case FAIL:
                layout.setBackground(context.getResources().getDrawable(R.drawable.corner_red_bg));
                break;
            case RIGHT:
                layout.setBackground(context.getResources().getDrawable(R.drawable.corner_gray_bg));
                break;
            case WARNING:
                layout.setBackground(context.getResources().getDrawable(R.drawable.corner_orange_bg));
                break;
            default:


        }
        layout.setOrientation(LinearLayout.HORIZONTAL);//设置LinearLayout垂直
        layout.setPadding(20, 20, 20, 20);
        layout.setGravity(Gravity.CENTER_VERTICAL);//设置LinearLayout里面内容中心分布
        return layout;
    }

    private static TextView getText(Context context, String content) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        etParam.setMargins(20, 0, 20, 0);
        textView.setLayoutParams(etParam);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setPadding(10, 0, 0, 0);
        textView.setTextSize(16);
        textView.setText(content);
        return textView;
    }

    private static ImageView getImage(Context context, boolean showImage, Showstate showstate) {
        ImageView imageView = new ImageView(context);
        if (showImage) {
            switch (showstate) {
                case RIGHT:
                    imageView.setImageResource(R.mipmap.right);
                    break;
                case WARNING:
                    imageView.setImageResource(R.mipmap.warning);
                    break;
                case FAIL:
                    imageView.setImageResource(R.mipmap.fail);
                    break;
                default:

            }
        }
        LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        etParam.setMargins(10, 0, 0, 0);
        imageView.setLayoutParams(etParam);
        return imageView;
    }

    public enum Showstate {
        WARNING,
        FAIL,
        RIGHT
    }
}
