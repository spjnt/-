package bar.barcode.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxui.view.wheelhorizontal.AbstractWheel;
import com.vondear.rxui.view.wheelhorizontal.ArrayWheelAdapter;
import com.vondear.rxui.view.wheelhorizontal.OnWheelScrollListener;
import com.vondear.rxui.view.wheelhorizontal.WheelHorizontalView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import bar.barcode.R;
import bar.barcode.bindView.BindApi;
import bar.barcode.bindView.BindID;
import bar.barcode.bindView.OnClick;
import bar.barcode.db.UpHistory;
import bar.barcode.recyle.HistoryAdapter;
import bar.barcode.ui.dialog.CleanCache;
import bar.barcode.util.TimeUtil;


@BindID(R.layout.activity_advice)
public class HistoryActivity extends AppCompatActivity {
    @BindID(R.id.whell_year_day)
    WheelHorizontalView year_day;
    @BindID(R.id.iv_back_his)
    ImageView iv_back;
    @BindID(R.id.iv_clean_all)
    TextView iv_clean_all;
    @BindID(R.id.rl_history)
    RecyclerView recyclerView;


    HistoryAdapter adapter;
    private List<UpHistory> histories;
    private GridLayoutManager mLayoutManager;
    private int lastVisibleItem = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final int PAGE_COUNT = 30;
    private String[] arr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        BindApi.BindIDEasy(this);
        BindApi.BindOnClick(this);
        initCacheData(TimeUtil.getNowTime());
        initTimeData();
        initRecyleView();
    }

    @Keep
    @OnClick({R.id.iv_back_his, R.id.iv_clean_all})
    private void onTag(View view) {
        switch (view.getId()) {
            case R.id.iv_back_his:
                finish();
                break;
            case R.id.iv_clean_all:
                new CleanCache(HistoryActivity.this, new CleanCache.Clean_Cache() {
                    @Override
                    public void clean(String string) {
                        if (string.equals("clean")) {
                            int currentItem = year_day.getCurrentItem();
                            String s = arr[currentItem];
                            LitePal.deleteAll(UpHistory.class, "up_time = ?", s);
                            if (recyclerView != null) recyclerView.setAdapter(null);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).show();
                break;
        }
    }


    private void initRecyleView() {
        adapter = new HistoryAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initCacheData(String time) {
        histories = LitePal.where("up_time =?", time).find(UpHistory.class);
    }


    private void initTimeData() {
        arr = new String[7];
        int CurrentIndex = 0;
        for (int i = 6; i >= 0; i--) {
            arr[i] = TimeUtil.getPastDate(i);
            CurrentIndex = i;
        }
        ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(this, arr);
        ampmAdapter.setItemResource(R.layout.item_wheel_year_month);
        ampmAdapter.setItemTextResource(R.id.tv_year);
        year_day.setViewAdapter(ampmAdapter);

        year_day.setCurrentItem(CurrentIndex);

        year_day.addScrollingListener(new OnWheelScrollListener() {
            String before;
            String behind;

            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                before = arr[wheel.getCurrentItem()];
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                behind = arr[wheel.getCurrentItem()];
                if (histories != null) histories.clear();
                if (adapter != null) adapter = null;
                if (!before.equals(behind)) {
                    String s = arr[wheel.getCurrentItem()];
                    initCacheData(s);
                    initRecyleView();
                }
            }
        });
    }

    private List<UpHistory> getDatas(final int firstIndex, final int lastIndex) {
        List<UpHistory> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (histories != null && histories.size() > 0) {
                if (i < histories.size()) {
                    UpHistory history = new UpHistory();
                    history.setUp_time(histories.get(i).getUp_time());
                    history.setCode(histories.get(i).getCode());
                    resList.add(history);
                }
            }

        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<UpHistory> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

}
