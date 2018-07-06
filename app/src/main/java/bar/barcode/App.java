package bar.barcode;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.vondear.rxtool.RxTool;

import org.litepal.LitePal;




public class App extends Application {
    private static Context context;//上下文

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        LitePal.initialize(this);
        LitePal.getDatabase();
        RxTool.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
