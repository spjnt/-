package bar.barcode.recyle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bar.barcode.R;


public class UpAdapter extends RecyclerView.Adapter<UpAdapter.MyViewHolder> implements View.OnClickListener {
    private LayoutInflater inflater;
    private Context mContext;
    private List<String> mDatas;

    //创建构造参数
    public UpAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(context);
    }

    //创建ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_num, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    //绑定ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //为textview 赋值
        holder.tv.setText(mDatas.get(position));
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        //Log.i("TAG", "mDatas "+mDatas);

        return mDatas.size();

    }

    //新增item
    public void addData(int pos) {

        notifyItemInserted(pos);
    }

    //移除item
    public void deleateData(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv_delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv_cow_id);
            iv_delete = itemView.findViewById(R.id.iv_delete_cow_id);


        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}


