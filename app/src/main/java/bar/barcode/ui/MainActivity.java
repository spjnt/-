package bar.barcode.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Keep;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synqe.Barcode.ImageType.EarMark;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import bar.barcode.db.UpHistory;
import bar.barcode.R;
import bar.barcode.bindView.BindApi;
import bar.barcode.bindView.BindID;
import bar.barcode.bindView.OnClick;
import bar.barcode.menu.DrawerAdapter;
import bar.barcode.menu.DrawerItem;
import bar.barcode.menu.SimpleItem;
import bar.barcode.menu.SpaceItem;
import bar.barcode.recyle.UpAdapter;
import bar.barcode.util.DecodeUtil;

import bar.barcode.util.TimeUtil;
import bar.barcode.util.TxtUtil;
import bar.barcode.util.Update;
import bar.barcode.util.Utils;
import bar.barcode.view.DragFloatingActionButton;

import static bar.barcode.util.Utils.Logs;

@BindID(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_HISTORY = 1;
    private static final int POS_VERSION = 0;

    private static final int POS_LOGOUT = 3;

    private SlidingRootNav slidingRootNav;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    public ArrayList<String> code_list = new ArrayList<String>();
    private UpAdapter myAdapter;
    @BindID(R.id.recyle_view)
    RecyclerView rl_code;
    @BindID(R.id.tv_result)
    TextView resultTv;
    @BindID(R.id.drag_btn)
    DragFloatingActionButton drag_btn;
    @BindID(R.id.toolbar)
    Toolbar toolbar;
    @BindID(R.id.tv_total)
    TextView tv_total;
    @BindID(R.id.iv_clean)
    ImageView iv_clean;
    @BindID(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindID(R.id.btn_submit)
    Button btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindApi.BindIDEasy(this);
        BindApi.BindOnClick(this);
        toolbar.setTitle("Scanner");
        rl_bottom.bringToFront();
        drag_btn.bringToFront();
        setSupportActionBar(toolbar);
        initRecyleView();
        initSlidinNav(toolbar, savedInstanceState);
    }


    private void initRecyleView() {
        List<UpHistory> all = LitePal.findAll(UpHistory.class);
        if (all != null && all.size() > 0) LitePal.deleteAll(UpHistory.class);
        for (int i = 0; i < 100; i++) {
            code_list.add(i + "0000000000000");
        }
       /* for (int i = 100; i < 200; i++) {
            UpHistory history = new UpHistory();
            history.setCode("1111111111111111" + i);

            history.setUp_time(TimeUtil.getPastDate(1));
            history.save();
            all.add(history);
        }
        for (int i = 300; i < 400; i++) {
            UpHistory history = new UpHistory();
            history.setCode("2222222222222222" + i);

            history.setUp_time(TimeUtil.getPastDate(2));
            history.save();
            all.add(history);
        }
        for (int i = 400; i < 410; i++) {
            UpHistory history = new UpHistory();
            history.setCode("33333333" + i);

            history.setUp_time(TimeUtil.getPastDate(3));
            history.save();
            all.add(history);
        }*/
        tv_total.setText(formatColor(code_list.size()));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rl_code.setLayoutManager(layoutManager);
        myAdapter = new UpAdapter(this, code_list);
        rl_code.setAdapter(myAdapter);
        myAdapter.setItemClickListener(new UpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                code_list.remove(position);
                myAdapter.notifyDataSetChanged();
                if (code_list.size() > 0) {
                    tv_total.setText(formatColor(code_list.size()));
                } else {
                    iv_clean.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initSlidinNav(Toolbar toolbar, Bundle savedInstanceState) {
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_VERSION),
                createItemFor(POS_HISTORY),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText("一二三");
    }

    @Keep
    @OnClick({R.id.drag_btn, R.id.iv_clean, R.id.btn_submit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.drag_btn:
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
                break;
            case R.id.iv_clean:
                if (code_list != null) code_list.clear();
                if (myAdapter != null) myAdapter.notifyDataSetChanged();
                if (iv_clean != null) iv_clean.setVisibility(View.GONE);
                if (tv_total != null) tv_total.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("bundle");
            byte[] byteArray = bundle.getByteArray(CaptureActivity.EXTRA_RESULT);
            try {
                EarMark ear = DecodeUtil.getEar(byteArray);
                int animalType = ear.AnimalType;
                int producerCode = ear.ProducerCode;
                String earMarkCode = ear.EarMarkCode;
                int earMarkNumber = ear.EarMarkNumber;
                int regionCode = ear.RegionCode;
                int regionSerial = ear.RegionSerial;
                Log.e("ear:", animalType + ":" + producerCode + ":" + earMarkCode + ":" + earMarkNumber + ":" + regionCode + ":" + regionSerial + ":");
                String txt = TxtUtil.Txt(getAssets().open("Region.txt"), ear.RegionSerial);
                resultTv.setText(ear.RegionSerial + ":" + txt);
            } catch (IOException e) {
                String message = e.getMessage();
                Logs(true, "message", message);
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        if (position == POS_VERSION) {
            Update.updateDiy(this);
        }
        if (position == POS_HISTORY) {
            Utils.goToNextUI(HistoryActivity.class);
        }
        slidingRootNav.closeMenu();

    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.circle))
                .withSelectedTextTint(color(R.color.circle));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    /*
     * 头数
     * */
    public Spanned formatColor(int size) {
        String str = String.valueOf(size);
        return Html.fromHtml("<font color='#563d40'  size='5'>" + "共有" + "</font>" + "&nbsp;&nbsp" + "<font color='#a7002b'>" + str + "&nbsp;&nbsp" + "</font>" + "<font color='#563d40'  size='5'>" + "个" + "</font>");

    }


}
