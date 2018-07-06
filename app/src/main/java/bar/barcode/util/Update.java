package bar.barcode.util;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bar.barcode.http.UpdateAppHttpUtil;
import barcode.bar.update_app.UpdateAppBean;
import barcode.bar.update_app.UpdateAppManager;
import barcode.bar.update_app.UpdateCallback;
import barcode.bar.update_app.listener.ExceptionHandler;
import barcode.bar.update_app.listener.IUpdateDialogFragmentListener;
import barcode.bar.update_app.utils.AppUpdateUtils;

public class Update {
    private static String mUpdateUrl = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json.txt";

    public static void updateDiy(Activity activity) {

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Map<String, String> params = new HashMap<String, String>();

        params.put("appKey", "ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f");
        params.put("appVersion", AppUpdateUtils.getVersionName(activity));
        params.put("key1", "value2");
        params.put("key2", "value3");


        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(activity)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(mUpdateUrl)
                //全局异常捕获
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(false)

                .setParams(params)

                .setTopPic(bar.barcode.R.mipmap.top_8)
                //为按钮，进度条设置颜色。
                .setThemeColor(0xffffac5d)
                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                    @Override
                    public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                        //用户点击关闭按钮，取消了更新，如果是下载完，用户取消了安装，则可以在 onActivityResult 监听到。

                    }
                })
                //不自动，获取
//                .setIgnoreDefParams(true)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            final String newVersion = jsonObject.optString("new_version");


                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate("Yes")
                                    //（必须）新版本号，
                                    .setNewVersion(newVersion)
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObject.optString("apk_file_url"))

                                    .setUpdateLog(jsonObject.optString("update_log"))
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(false)
                                    //设置md5，可以不设置
                                    .setNewMd5(jsonObject.optString("new_md5"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        String newVersion = updateApp.getNewVersion();
                        updateAppManager.showDialogFragment();
//                        if (!newVersion.equals("0.8.3")) {


//                        } else {
//                            ToastUtils.showStr("暂无新版本更新", ConfigToast.WARING);
//                        }
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        CProgressDialogUtils.showProgressDialog(activity);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(activity);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String error) {
                        Toast.makeText(activity, "没有新版本", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
