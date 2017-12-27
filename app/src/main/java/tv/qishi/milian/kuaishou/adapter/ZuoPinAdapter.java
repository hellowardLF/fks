package tv.qishi.milian.kuaishou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tv.qishi.milian.R;
import tv.qishi.milian.kuaishou.PlayActivity;
import tv.qishi.milian.kuaishou.model.HomeInfoModel;
import tv.qishi.milian.kuaishou.model.MyVideoItemModle;
import tv.qishi.milian.kuaishou.model.MyVideoModle;

/**
 * Created by hxj on 2017/11/14.
 */

public class ZuoPinAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ZuoPinAdapter";
    private Context mContext;
    private List<MyVideoItemModle> list;

    public ZuoPinAdapter(Context mContext, List<MyVideoItemModle> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                viewHolder = new MyVideoViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.adapter_item_myvideo, parent, false));
                break;
            case 1:
                viewHolder = new MyFouseViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.item_zuopin, parent, false));
                break;
        }
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyVideoItemModle myVideoItemModle = list.get(position);
        SimpleDateFormat format =  new SimpleDateFormat("MM-dd HH:mm");
        if (holder instanceof MyVideoViewHolder){
            MyVideoViewHolder viewHolder= (MyVideoViewHolder) holder;
            Glide.with(mContext).load(myVideoItemModle.getVideo_thumb()).centerCrop().into(viewHolder.img);
            viewHolder.title.setText(myVideoItemModle.getVideo_title());
            String format1 = format.format(new Date(Integer.parseInt(myVideoItemModle.getCreate_time())));
            viewHolder.creattime.setText(format1);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext, PlayActivity.class);
                    mIntent .putExtra("video_id", myVideoItemModle.getId());
                    mContext.startActivity(mIntent);
                }
            });
        }else {
            MyFouseViewHolder viewHolder= (MyFouseViewHolder) holder;
            viewHolder.title.setText(myVideoItemModle.getVideo_title());
            viewHolder.creatTime.setText(format.format(new Date(Integer.parseInt(myVideoItemModle.getCreate_time()))));
            viewHolder.name.setText(myVideoItemModle.getUser_nicename());
            if (!TextUtils.isEmpty(myVideoItemModle.getVideo_time())){
                viewHolder.videoTime.setText(getTiem(Integer.parseInt(myVideoItemModle.getVideo_time())/1000));
            }
            viewHolder.commentNumber.setText(myVideoItemModle.getComments()+"评论");
            Glide.with(mContext).load(myVideoItemModle.getVideo_thumb()).centerCrop().into(viewHolder.videoImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext, PlayActivity.class);
                    mIntent .putExtra("video_id", myVideoItemModle.getVideo_id());
                    mContext.startActivity(mIntent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(list.get(position).getAvatar())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + list.size());
        return list.size();
    }

    public String getTiem(int video_time) {
        String time="";
        String ss = String.valueOf(video_time % 60);
        String mm = String.valueOf(video_time /60% 60);
        time=(mm.length()==1?"0"+mm:mm)+":"+(ss.length()==1?"0"+ss:ss);
        return time;
    }

    class MyVideoViewHolder extends RecyclerView.ViewHolder {
        TextView title, creattime;
        ImageView img;

        public MyVideoViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.myvideo_title);
            creattime= (TextView) itemView.findViewById(R.id.myvideo_creattime);
            img= (ImageView) itemView.findViewById(R.id.myvideo_pic);
        }
    }

    class MyFouseViewHolder extends RecyclerView.ViewHolder {
        TextView title,name,creatTime,videoTime,commentNumber;
        ImageView videoImg;
        public MyFouseViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.zuoping_title);
            name= (TextView) itemView.findViewById(R.id.zuoping_name);
            creatTime= (TextView) itemView.findViewById(R.id.zuoping_creattime);
            videoTime= (TextView) itemView.findViewById(R.id.zuoping_videotime);
            commentNumber= (TextView) itemView.findViewById(R.id.zuoping_comment);
            videoImg= (ImageView) itemView.findViewById(R.id.zuoping_img);
        }
    }
}
