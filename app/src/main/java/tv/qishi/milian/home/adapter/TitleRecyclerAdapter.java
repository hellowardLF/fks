package tv.qishi.milian.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.event.EmptyEvent;
import tv.qishi.milian.event.IndexEvent;
import tv.qishi.milian.home.intf.OnRecyclerViewItemClickListener;

/**
 * Created by hxj on 2017/11/6.
 */

public class TitleRecyclerAdapter extends RecyclerView.Adapter<TitleRecyclerAdapter.ViewHolder> {
    private List<String> data;
    private LayoutInflater mInflater;
    private Activity mActivity;
    private int index;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
        this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
    }

    public TitleRecyclerAdapter(List<String> mData, Activity mActivity) {
        this.mActivity = mActivity;
        mInflater = LayoutInflater.from(mActivity);
        data = mData;
    }

    @Override
    public TitleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_horizontal, parent, false));
    }

    @Override
    public void onBindViewHolder(final TitleRecyclerAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(data.get(position));
        for (String ignored : data) {
            holder.tv.setTextColor(ContextCompat.getColor(mActivity, R.color.grey));
        }
        if (position == index) {
            holder.tv.setTextColor(ContextCompat.getColor(mActivity, R.color.colorTheme));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index=position;
                notifyDataSetChanged();
                mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
