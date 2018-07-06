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


@BindID(R.layout.clean_cache)
public class CleanCache extends Dialog {
    @BindID(R.id.cancle_cache)
    TextView cancle;

    @BindID(R.id.confim_cache)
    TextView confim;
    Clean_Cache cleanCache;

    public CleanCache(@NonNull Context context, Clean_Cache clean_cache) {
        super(context, R.style.custom_dialog);
        this.cleanCache = clean_cache;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clean_cache);
        BindApi.BindIDEasy(this);
        BindApi.BindOnClick(this);
        initData();
    }

    private void initData() {

    }

    @Keep
    @OnClick({R.id.cancle_cache, R.id.confim_cache})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle_cache:
                dismiss();
                break;
            case R.id.confim_cache:
                cleanCache.clean("clean");
                dismiss();
                break;
        }
    }

    public interface Clean_Cache {
        void clean(String string);
    }
}
