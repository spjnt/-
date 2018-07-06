package bar.barcode.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import bar.barcode.R;
import bar.barcode.bindView.BindApi;
import bar.barcode.bindView.BindID;
import bar.barcode.bindView.OnClick;
import bar.barcode.loading.LoadingView;
import bar.barcode.ui.dialog.ServerConfig;
import bar.barcode.util.Utils;

@BindID(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    private boolean isShow = false;
    private boolean isRem = false;
    @BindID(R.id.iv_hide_show_psw)
    ImageView iv_hide_show_psw;
    @BindID(R.id.iv_rem_pass)
    ImageView iv_rem_psw;
    @BindID(R.id.btn_login)
    Button btn_login;
    @BindID(R.id.setting)
    ImageView iv_setting;
    @BindID(R.id.iv_clean_account)
    ImageView iv_clean_account;
    @BindID(R.id.iv_clean_psw)
    ImageView iv_clean_psw;
    @BindID(R.id.et_account)
    EditText et_account;
    @BindID(R.id.et_psw)
    EditText et_psw;
    @BindID(R.id.loadView)
    LoadingView loadView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BindApi.BindIDEasy(this);
        BindApi.BindOnClick(this);
    }

    @Keep
    @OnClick({R.id.iv_rem_pass, R.id.iv_hide_show_psw, R.id.btn_login,
            R.id.setting, R.id.iv_clean_psw, R.id.iv_clean_account})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rem_pass:
                isRem = !isRem;
                iv_rem_psw.setSelected(isRem);

                break;
            case R.id.iv_hide_show_psw:
                isShow = !isShow;
                iv_hide_show_psw.setSelected(isShow);
                if (isShow) {
                    et_psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_login:
                loadView.setVisibility(View.VISIBLE);
                loadView.setLoadingText(R.string.loading___, R.color.white);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.goToNextUI(MainActivity.class);
                        loadView.setVisibility(View.GONE);
                    }
                },2000);


                break;
            case R.id.setting:
                new ServerConfig(LoginActivity.this).show();
                break;
            case R.id.iv_clean_account:
                et_account.setText("");
                break;
            case R.id.iv_clean_psw:
                et_psw.setText("");
                break;
        }
    }
}
