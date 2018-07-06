package bar.barcode.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bar.barcode.R;
import bar.barcode.bindView.BindApi;
import bar.barcode.bindView.BindID;
import bar.barcode.bindView.OnClick;
import bar.barcode.constant.Constants;
import bar.barcode.util.PreferencesUtils;
import bar.barcode.util.ToastUtil;
import bar.barcode.util.Utils;

@BindID(R.layout.server_config)
public class ServerConfig extends Dialog {
    @BindID(R.id.cancle)
    TextView cancle;
    @BindID(R.id.et_ip)
    EditText et_ip;
    @BindID(R.id.confim)
    TextView confim;

    public ServerConfig(@NonNull Context context) {
        super(context, R.style.custom_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_config);
        BindApi.BindIDEasy(this);
        BindApi.BindOnClick(this);
        initData();
    }

    private void initData() {
        String string = PreferencesUtils.getString(getContext(), Constants.ET_IP);
        if (!TextUtils.isEmpty(string)) {
            et_ip.setText(string);
        } else {
            et_ip.setHint(R.string.input_ori_psw);
        }
    }

    @Keep
    @OnClick({R.id.cancle, R.id.confim})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                dismiss();
                break;
            case R.id.confim:
                String s = Utils.ifStartEndWith(et_ip.getText().toString().trim());
                String ip = Utils.chageBigToLow(s);
                PreferencesUtils.putString(getContext(), Constants.ET_IP, ip);
//                ToastUtils.showStr(R.string.save_success, ConfigToast.INFO);
                ToastUtil.showToast(getContext(), "保存成功", ToastUtil.Showstate.RIGHT);
                dismiss();
                break;
        }
    }

}
