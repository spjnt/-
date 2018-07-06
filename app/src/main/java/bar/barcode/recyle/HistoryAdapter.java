package bar.barcode.recyle;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bar.barcode.R;
import bar.barcode.db.UpHistory;



public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UpHistory> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public HistoryAdapter(List<UpHistory> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_history, null));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.load_loading_layout, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).tv_code.setText("编  号:" + datas.get(position).getCode());
//            ((NormalHolder) holder).tv_time.setText("上传日期:" + datas.get(position).getUp_time());
        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                } else {
                    ((FootHolder) holder).tips.setText("暂无数据展示");
                    ((FootHolder) holder).tips.setTextColor(context.getResources().getColor(R.color.triangle));
                    ((FootHolder) holder).tips.setTextSize(18);
                }
            } else {
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                } else {
                    ((FootHolder) holder).tips.setText("暂无数据展示");
                    ((FootHolder) holder).tips.setTextColor(context.getResources().getColor(R.color.triangle));
                    ((FootHolder) holder).tips.setTextSize(18);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public int getRealLastPosition() {
        return datas.size();
    }


    public void updateList(List<UpHistory> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        TextView tv_code;
        TextView tv_time;


        public NormalHolder(View itemView) {
            super(itemView);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tv_time = (TextView) itemView.findViewById(R.id.tv_up_time);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        datas = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }
}
