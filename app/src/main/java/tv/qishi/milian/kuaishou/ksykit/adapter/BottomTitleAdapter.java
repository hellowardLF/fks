package tv.qishi.milian.kuaishou.ksykit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import tv.qishi.milian.R;

/**
 * EditActivity底部功能导航栏的RecyclerView的Adapter
 */

public class BottomTitleAdapter extends RecyclerView.Adapter<BottomTitleAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mData;
    private MyViewHolder mPreHolder;
    private int mPreIndex = -1;
    private OnItemClickListener mListener;

    public int getItemNumber(int position) {
        //        String[] items = {"1美颜", "2滤镜", "3水印", "4变速", "5时长裁剪", "6画布裁剪", "7音乐", "8变声", "9混响",
//                "10涂鸦", "11动态贴纸", "12贴纸", "13字幕"};
//        String[] items = { "1滤镜", "2变速", "3时长裁剪5", "4音乐7", " "6字幕13"};//
        int i=-1;
        switch (position) {
            case 0:
                i=1;
                break;
            case 1:
                i=3;
                break;
            case 2:
                i=4;
                break;
            case 3:
                i=6;
                break;
            case 4:
                i=12;
                break;
        }


        return i;
    }

    public interface OnItemClickListener {
        void onClick(int curIndex, int preIndex);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public BottomTitleAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_title_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.title.setText(mData.get(position));
        if (mPreIndex == position) {
            mPreHolder = holder;
            holder.setActivated(true);
        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreHolder != null && position != mPreHolder.getPosition()) {
                    mPreHolder.setActivated(false);
                }
                if (position == 2 && holder.title.isActivated()) {
                    holder.setActivated(false);
                } else {
                    holder.setActivated(true);
                }
                int itemNumber = getItemNumber(position);
                if (mListener != null) {
                    mListener.onClick(itemNumber, getItemNumber(mPreIndex));
                }
                mPreHolder = holder;
                mPreIndex = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clear() {
        if (mPreHolder != null) {
            mPreHolder.setActivated(false);
            mPreIndex = -1;
            mPreHolder = null;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        View indicator;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_edit_title);
            indicator = view.findViewById(R.id.item_title_indicator);
        }

        public void setActivated(boolean active) {
            if (active) {
                title.setActivated(true);
                indicator.setActivated(true);
            } else {
                title.setActivated(false);
                indicator.setActivated(false);
            }
        }
    }
}
