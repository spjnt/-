package bar.barcode.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import bar.barcode.R;
import bar.barcode.loading.LoadingView;


public class ShapeLoadingDialog extends Dialog {

    private LoadingView mLoadingView;

    private Builder mBuilder;

    private ShapeLoadingDialog(Builder builder) {
        super(builder.mContext, R.style.custom_dialog);
        mBuilder = builder;
        setCancelable(mBuilder.mCancelable);
        setCanceledOnTouchOutside(mBuilder.mCanceledOnTouchOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);

        mLoadingView = (LoadingView) findViewById(R.id.load_view);

        mLoadingView.setDelay(mBuilder.mDelay);
//        mLoadingView.setLoadingText(mBuilder.mLoadText);

        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mLoadingView.setVisibility(View.GONE);
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public static class Builder {

        private Context mContext;

        private int mDelay = 80;

        private CharSequence mLoadText;

        private int textColr;

        private boolean mCancelable = true;

        private boolean mCanceledOnTouchOutside = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder delay(int delay) {
            mDelay = delay;
            return this;
        }

        public Builder loadText(CharSequence loadText) {
            mLoadText = loadText;

            return this;
        }

        public Builder loadText(int resId) {
            mLoadText = mContext.getString(resId);
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            mCancelable = cancelable;
            mCanceledOnTouchOutside = cancelable;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public ShapeLoadingDialog build() {
            return new ShapeLoadingDialog(this);
        }

        public ShapeLoadingDialog show() {
            ShapeLoadingDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }
}
