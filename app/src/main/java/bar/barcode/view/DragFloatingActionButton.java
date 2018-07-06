package bar.barcode.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import bar.barcode.util.DisplayUtil;

public class DragFloatingActionButton extends FloatingActionButton {
    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int navigationBarHeight;
    private long startTime = 0;
    private long endTime = 0;

    public DragFloatingActionButton(Context context) {
        super(context);
        init();
    }

    public DragFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //底部导航栏高度
        navigationBarHeight = DisplayUtil.getNavigationBarHeight((Activity) getContext());
        //顶部状态栏高度
        statusHeight = DisplayUtil.getStatusHeight((Activity) getContext());
        //屏幕宽度
        screenWidth = DisplayUtil.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        //屏幕高度
        screenHeight = DisplayUtil.getScreenHeight(getContext()) - navigationBarHeight;


    }

    private int lastX;
    private int lastY;
    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int rawX = (int) ev.getRawX();
        int rawY = (int) ev.getRawY();


        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
//                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                int b = getBottom() + dy;
                int l = getLeft() + dx;

                int r = getRight() + dx;
                int t = getTop() + dy;


                if (l < 0) {
                    l = 0;
                    r = l + getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;

                    t = b - getHeight();
                }

                layout(l, t, r, b);
                lastX = (int) ev.getRawX();
                lastY = (int) ev.getRawY();
                postInvalidate();
              /*  //这里修复一些华为手机无法触发点击事件的问题
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0){
                    isDrag = false;
                    break;
                }
                float x = getX()+dx;
                float y = getY() +dy;
                //检测是否到达边缘，左上右下
                x=x<0?0:x>screenWidth-getWidth()?screenWidth-getWidth():x;
                y=y<0?0:y>screenHeight-getHeight()?screenHeight-getHeight()-getBottom():y;
              // y = y<statusHeight? statusHeight:y>screenHeight- getHeight()?screenHeight - getHeight()-NavigationBarHeight:y-NavigationBarHeight;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;*/
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if ((endTime - startTime) > 0.1 * 1000L) {
                    isDrag = true;
                } else {
                    isDrag = false;
                }
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    if (rawX > screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(200)
                                .xBy(screenWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(200);
                        oa.start();
                    }
                }
                break;
        }
        return isDrag || super.onTouchEvent(ev);
    }
}
